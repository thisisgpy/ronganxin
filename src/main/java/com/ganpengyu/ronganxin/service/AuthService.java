package com.ganpengyu.ronganxin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ganpengyu.ronganxin.beanmapper.UserBeanMapper;
import com.ganpengyu.ronganxin.common.Constants;
import com.ganpengyu.ronganxin.common.component.JwtService;
import com.ganpengyu.ronganxin.common.component.RedisService;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.common.util.CodecUtils;
import com.ganpengyu.ronganxin.dao.SysResourceDao;
import com.ganpengyu.ronganxin.dao.SysRoleResourceDao;
import com.ganpengyu.ronganxin.dao.SysUserDao;
import com.ganpengyu.ronganxin.dao.SysUserRoleDao;
import com.ganpengyu.ronganxin.model.SysResource;
import com.ganpengyu.ronganxin.model.SysRoleResource;
import com.ganpengyu.ronganxin.model.SysUser;
import com.ganpengyu.ronganxin.model.SysUserRole;
import com.ganpengyu.ronganxin.web.dto.auth.AssignRoleResourceDto;
import com.ganpengyu.ronganxin.web.dto.auth.AssignUserRoleDto;
import com.ganpengyu.ronganxin.web.dto.auth.UserLoginDto;
import com.ganpengyu.ronganxin.web.dto.resource.SysResourceDto;
import com.ganpengyu.ronganxin.web.dto.user.LoginUserDto;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 认证授权服务
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/4
 */
@Service
public class AuthService {

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Resource
    private SysRoleResourceDao sysRoleResourceDao;

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SysResourceDao sysResourceDao;

    @Resource
    private JwtService jwtService;

    @Resource
    private UserBeanMapper userBeanMapper;

    @Resource
    private ResourceService resourceService;

    @Resource
    private RedisService redisService;

    /**
     * 为用户分配角色
     *
     * @param assignUserRoleDto 用户角色分配数据传输对象，包含用户ID和角色ID列表
     * @return 分配成功返回true，失败抛出异常
     */
    @Transactional
    public boolean assignUserRole(AssignUserRoleDto assignUserRoleDto) {
        // 删除用户原有的角色关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", assignUserRoleDto.getUserId());
        int rows = sysUserRoleDao.deleteByQuery(queryWrapper);
        CheckUtils.check(rows > 0, "删除用户角色关系失败");

        // 如果有新的角色需要分配，则批量插入新的用户角色关系
        if (null != assignUserRoleDto.getRoleIds() && !assignUserRoleDto.getRoleIds().isEmpty()) {
            List<SysUserRole> relations = new ArrayList<>();
            for (Long roleId : assignUserRoleDto.getRoleIds()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assignUserRoleDto.getUserId());
                sysUserRole.setRoleId(roleId);
                relations.add(sysUserRole);
            }
            rows = sysUserRoleDao.insertBatch(relations);
            CheckUtils.check(rows == relations.size(), "添加用户角色关系失败");
        }
        return true;
    }

    /**
     * 为角色分配资源权限
     *
     * @param assignRoleResourceDto 角色资源分配数据传输对象，包含角色ID和资源ID列表
     * @return 分配成功返回true，失败则抛出异常
     */
    public boolean assignRoleResource(AssignRoleResourceDto assignRoleResourceDto) {
        // 删除角色原有的资源关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id", assignRoleResourceDto.getRoleId());
        int rows = sysRoleResourceDao.deleteByQuery(queryWrapper);
        CheckUtils.check(rows > 0, "删除角色资源关系失败");

        // 如果有新的资源需要分配
        if (null != assignRoleResourceDto.getResourceIds() && !assignRoleResourceDto.getResourceIds().isEmpty()) {
            // 构建新的角色资源关系列表
            List<SysRoleResource> relations = new ArrayList<>();
            for (Long resourceId : assignRoleResourceDto.getResourceIds()) {
                SysRoleResource sysRoleResource = new SysRoleResource();
                sysRoleResource.setRoleId(assignRoleResourceDto.getRoleId());
                sysRoleResource.setResourceId(resourceId);
                relations.add(sysRoleResource);
            }
            // 批量插入新的角色资源关系
            rows = sysRoleResourceDao.insertBatch(relations);
            CheckUtils.check(rows == relations.size(), "添加角色资源关系失败");
        }
        return true;
    }

    /**
     * 根据用户ID查询资源编码列表
     *
     * @param userId 用户ID
     * @return 资源编码列表
     */
    public List<String> findResourceCodesByUserId(Long userId) {
        // 从缓存读取资源编码列表
        List<String> resourceCodes = redisService.get(Constants.getAuthResourceCodesKey(userId), new TypeReference<List<String>>() {
        });
        // 查询数据库
        if (resourceCodes == null || resourceCodes.isEmpty()) {
            List<SysResource> resources = sysResourceDao.findResourceByUserId(userId);
            resourceCodes = resources.stream().map(SysResource::getCode).toList();
            // 放入缓存
            redisService.set(Constants.getAuthResourceCodesKey(userId), resourceCodes, Constants.AUTH_TTL, TimeUnit.SECONDS);
        }
        return resourceCodes;
    }

    /**
     * 检查用户是否具有指定资源的权限
     *
     * @param userId       用户ID
     * @param resourceCode 资源编码
     * @return 如果用户具有该资源权限则返回true，否则返回false
     */
    public boolean hasPermission(Long userId, String resourceCode) {
        List<String> resourceCodes = this.findResourceCodesByUserId(userId);
        return resourceCodes.contains(resourceCode);
    }

    /**
     * 用户登录功能
     *
     * @param userLoginDto 用户登录传输对象，包含手机号和密码
     * @return LoginUserDto 登录用户信息传输对象，包含用户信息、token和菜单资源
     */
    public LoginUserDto login(UserLoginDto userLoginDto) {
        String mobile = userLoginDto.getMobile();
        String password = userLoginDto.getPassword();

        // 参数验证
        CheckUtils.check(StringUtils.hasText(mobile), "手机号不能为空");
        CheckUtils.check(StringUtils.hasText(password), "密码不能为空");

        LoginUserDto loginUserDto = new LoginUserDto();

        // 密码加密处理
        String encryptPassword = CodecUtils.encryptPassword(password);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile)
                .eq("password", encryptPassword)
                .eq("status", 1); // 建议使用常量替代硬编码
        SysUser sysUser = sysUserDao.selectOneByQuery(queryWrapper);
        CheckUtils.check(sysUser != null, "账号或密码错误");
        Long userId = sysUser.getId();

        // 设置用户基本信息
        loginUserDto.setUserInfo(userBeanMapper.toSysUserDto(sysUser));
        String token = jwtService.createToken(userId);
        loginUserDto.setToken(token);

        // 获取用户菜单资源
        List<SysResourceDto> menus = resourceService.findResourceByUserId(userId);
        loginUserDto.setMenus(menus);

        // 缓存 token
        redisService.set(Constants.getAuthTokenKey(userId), token, Constants.AUTH_TTL, TimeUnit.SECONDS);
        // 缓存用户信息
        sysUser.setPassword(null);
        redisService.set(Constants.getAuthUserInfoKey(userId), sysUser, Constants.AUTH_TTL, TimeUnit.SECONDS);
        return loginUserDto;
    }


    /**
     * 退出登录
     *
     * @param userId 用户ID
     * @return 退出成功返回true，失败返回false
     */
    public boolean logout(Long userId) {
        redisService.delete(Constants.getAuthTokenKey(userId));
        redisService.delete(Constants.getAuthUserInfoKey(userId));
        redisService.delete(Constants.getAuthResourceCodesKey(userId));
        return true;
    }

}

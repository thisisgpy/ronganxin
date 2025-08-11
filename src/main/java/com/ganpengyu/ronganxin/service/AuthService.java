package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.common.Constants;
import com.ganpengyu.ronganxin.common.component.LocalCache;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.common.util.JsonUtils;
import com.ganpengyu.ronganxin.dao.SysResourceDao;
import com.ganpengyu.ronganxin.dao.SysRoleResourceDao;
import com.ganpengyu.ronganxin.dao.SysUserRoleDao;
import com.ganpengyu.ronganxin.model.SysResource;
import com.ganpengyu.ronganxin.model.SysRoleResource;
import com.ganpengyu.ronganxin.model.SysUserRole;
import com.ganpengyu.ronganxin.web.dto.auth.AssignRoleResourceDto;
import com.ganpengyu.ronganxin.web.dto.auth.AssignUserRoleDto;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private SysResourceDao sysResourceDao;

    @Resource
    private LocalCache<String, String> cache;

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
        List<SysResource> resources = sysResourceDao.findResourceByUserId(userId);
        return resources.stream().map(SysResource::getCode).collect(Collectors.toList());
    }

    /**
     * 检查用户是否具有指定资源的权限
     *
     * @param userId       用户ID
     * @param resourceCode 资源编码
     * @return 如果用户具有该资源权限则返回true，否则返回false
     */
    public boolean hasPermission(Long userId, String resourceCode) {
        List<String> resourceCodes;
        String codesJson = cache.get(Constants.getCacheResourceCodesKey(userId));
        if (StringUtils.hasText(codesJson)) {
            // 从缓存中获取用户资源编码列表
            resourceCodes = JsonUtils.fromJsonToList(codesJson, String.class);
        } else {
            // 获取用户拥有的所有资源编码列表
            resourceCodes = this.findResourceCodesByUserId(userId);
            cache.put(Constants.getCacheResourceCodesKey(userId), JsonUtils.toJson(resourceCodes), Constants.CACHE_AUTH_TTL);
        }
        // 判断用户资源列表中是否包含指定的资源编码
        return resourceCodes.contains(resourceCode);
    }


    /**
     * 退出登录
     *
     * @param userId 用户ID
     * @return 退出成功返回true，失败返回false
     */
    public boolean logout(Long userId) {
        // 删除 uid -> token 缓存
        String cacheUidTokenKey = Constants.getCacheUidTokenKey(userId);
        String token = cache.get(cacheUidTokenKey);
        cache.remove(cacheUidTokenKey);
        // 删除 token -> 用户信息缓存
        String cacheTokenUserKey = Constants.getCacheTokenUserKey(token);
        cache.remove(cacheTokenUserKey);

        return true;
    }

}

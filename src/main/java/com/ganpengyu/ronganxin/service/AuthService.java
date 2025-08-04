package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.dao.SysResourceDao;
import com.ganpengyu.ronganxin.dao.SysRoleDao;
import com.ganpengyu.ronganxin.dao.SysRoleResourceDao;
import com.ganpengyu.ronganxin.dao.SysUserRoleDao;
import com.ganpengyu.ronganxin.model.SysRole;
import com.ganpengyu.ronganxin.model.SysRoleResource;
import com.ganpengyu.ronganxin.model.SysUserRole;
import com.ganpengyu.ronganxin.web.dto.auth.AssignRoleResourceDto;
import com.ganpengyu.ronganxin.web.dto.auth.AssignUserRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.SysRoleDto;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private SysRoleDao sysRoleDao;

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
        return sysResourceDao.findResourceCodesByUserId(userId);
    }

    public boolean hasPermission(Long userId, String resourceCode) {
        // TODO 这里需要接入缓存
        List<String> resourceCodes = this.findResourceCodesByUserId(userId);
        return resourceCodes.contains(resourceCode);
    }




}

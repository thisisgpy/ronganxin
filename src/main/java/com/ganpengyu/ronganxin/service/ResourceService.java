package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.ResourceBeanMapper;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.dao.SysResourceDao;
import com.ganpengyu.ronganxin.model.SysResource;
import com.ganpengyu.ronganxin.web.dto.resource.CreateResourceDto;
import com.ganpengyu.ronganxin.web.dto.resource.SysResourceDto;
import com.ganpengyu.ronganxin.web.dto.resource.UpdateResourceDto;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源服务
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Service
public class ResourceService {

    @Resource
    private SysResourceDao sysResourceDao;

    @Resource
    private ResourceBeanMapper resourceBeanMapper;

    /**
     * 创建资源
     *
     * @param createResourceDto 创建资源的数据传输对象，包含资源的基本信息
     * @return 创建成功返回true，创建失败抛出异常
     */
    @Transactional
    public boolean createResource(CreateResourceDto createResourceDto) {
        // 检查资源编码是否已存在，如果存在则抛出异常
        SysResource duplicateCodeResource = this.findSysResourceByCode(createResourceDto.getCode(), null);
        CheckUtils.check(duplicateCodeResource == null, "资源编码已存在");

        // 转换DTO对象为系统资源实体，并设置创建人和创建时间
        SysResource sysResource = resourceBeanMapper.toSysResource(createResourceDto);
        sysResource.setCreateBy(UserContext.getUsername());
        sysResource.setCreateTime(LocalDateTime.now());

        // 插入资源记录到数据库
        int rows = sysResourceDao.insertSelective(sysResource);
        CheckUtils.check(rows != 0, "新增资源失败");
        return true;
    }

    /**
     * 更新资源信息
     *
     * @param updateResourceDto 资源更新数据传输对象，包含要更新的资源信息
     * @return 更新成功返回true，更新失败则抛出异常
     */
    @Transactional
    public boolean updateResource(UpdateResourceDto updateResourceDto) {
        // 查找要更新的资源是否存在
        SysResource existingResource = this.findSysResourceById(updateResourceDto.getId());
        CheckUtils.check(existingResource != null, "资源不存在");

        // 如果提供了资源编码，则进行唯一性校验
        if (StringUtils.hasText(updateResourceDto.getCode())) {
            // 检查资源编码是否已存在（排除当前资源自身）
            SysResource duplicateCodeResource = this.findSysResourceByCode(updateResourceDto.getCode(), updateResourceDto.getId());
            CheckUtils.check(duplicateCodeResource == null, "资源编码已存在");
        }
        // 执行资源信息更新
        resourceBeanMapper.updateSysResource(updateResourceDto, existingResource);
        existingResource.setUpdateBy(UserContext.getUsername());
        existingResource.setUpdateTime(LocalDateTime.now());
        int rows = sysResourceDao.update(existingResource);
        CheckUtils.check(rows != 0, "更新资源失败");
        return true;
    }

    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 删除成功返回true
     */
    @Transactional
    public boolean removeResource(Long id) {
        // 查找要删除的资源
        SysResource existingResource = this.findSysResourceById(id);
        CheckUtils.check(existingResource != null, "资源不存在");

        // 标记资源为已删除状态
        existingResource.setIsDeleted(true);
        existingResource.setUpdateBy(UserContext.getUsername());
        existingResource.setUpdateTime(LocalDateTime.now());

        // 更新资源状态
        int rows = sysResourceDao.update(existingResource);
        CheckUtils.check(rows != 0, "删除资源失败");

        return true;
    }

    /**
     * 根据父级资源ID查找子资源列表
     *
     * @param parentId 父级资源ID
     * @return 子资源DTO列表
     */
    public List<SysResourceDto> findChildren(Long parentId) {
        // 构造查询条件：父级ID等于指定值、未删除、按排序字段升序、按创建时间降序
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", parentId)
                .eq("is_deleted", false)
                .orderBy("sort", true)
                .orderBy("create_time", true);
        List<SysResource> sysResources = sysResourceDao.selectListByQuery(queryWrapper);
        // 如果查询结果为空，返回空列表
        if (null == sysResources || sysResources.isEmpty()) {
            return new ArrayList<>();
        }
        // 转换为DTO对象并返回
        return resourceBeanMapper.toSysResourceDtoList(sysResources);
    }

    /**
     * 查找资源树结构
     * 该方法用于查询系统资源并构建成树形结构，便于前端展示层级菜单或资源管理
     *
     * @return List<SysResourceDto> 资源树结构列表，每个节点包含其子节点信息
     */
    public List<SysResourceDto> findResourceTree() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted", false)
                .orderBy("sort", true)
                .orderBy("create_time", true);
        List<SysResource> sysResources = sysResourceDao.selectListByQuery(queryWrapper);
        List<SysResourceDto> sysResourceDtoList = resourceBeanMapper.toSysResourceDtoList(sysResources);

        Map<Long, List<SysResourceDto>> map = new HashMap<>();
        List<SysResourceDto> roots = new ArrayList<>();

        // 构建父子映射关系
        for (SysResourceDto dto : sysResourceDtoList) {
            Long parentId = dto.getParentId();
            if (parentId == null || parentId.equals(0L)) {
                roots.add(dto);
            } else {
                map.computeIfAbsent(parentId, k -> new ArrayList<>()).add(dto);
            }
        }

        // 递归构建树结构
        for (SysResourceDto root : roots) {
            buildResourceTree(root, map);
        }
        map.clear();

        return roots;
    }

    /**
     * 根据用户ID查询资源列表并构建成树形结构
     *
     * @param userId 用户ID
     * @return 树形结构的资源列表，如果用户没有资源则返回空列表
     */
    public List<SysResourceDto> findResourceByUserId(Long userId) {
        List<SysResource> resources = sysResourceDao.findResourceByUserId(userId);
        if (null == resources || resources.isEmpty()) {
            return new ArrayList<>();
        }
        List<SysResourceDto> sysResourceDtoList = resourceBeanMapper.toSysResourceDtoList(resources);

        Map<Long, List<SysResourceDto>> map = new HashMap<>();
        List<SysResourceDto> roots = new ArrayList<>();

        // 构建父子映射关系
        for (SysResourceDto dto : sysResourceDtoList) {
            Long parentId = dto.getParentId();
            if (parentId == null || parentId.equals(0L)) {
                roots.add(dto);
            } else {
                map.computeIfAbsent(parentId, k -> new ArrayList<>()).add(dto);
            }
        }

        // 递归构建树形结构
        for (SysResourceDto root : roots) {
            buildResourceTree(root, map);
        }
        map.clear();
        return roots;
    }


    public SysResourceDto findSysResourceDtoById(Long id) {
        SysResource sysResource = this.findSysResourceById(id);
        CheckUtils.check(sysResource != null, "资源不存在");
        return resourceBeanMapper.toSysResourceDto(sysResource);
    }

    public List<SysResourceDto> findResourceByRoleId(Long roleId) {
        List<SysResource> sysResources = sysResourceDao.findResourceByRoleId(roleId);
        if (null == sysResources || sysResources.isEmpty()) {
            return new ArrayList<>();
        }
        return resourceBeanMapper.toSysResourceDtoList(sysResources);
    }

    /**
     * 递归构建资源树结构
     */
    private void buildResourceTree(SysResourceDto parent, Map<Long, List<SysResourceDto>> map) {
        List<SysResourceDto> children = map.get(parent.getId());
        if (children != null) {
            parent.setChildren(children);
            for (SysResourceDto child : children) {
                buildResourceTree(child, map);
            }
        }
    }

    private SysResource findSysResourceByCode(String code, Long excludeId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", code);
        if (null != excludeId) {
            queryWrapper.ne("id", excludeId);
        }
        return sysResourceDao.selectOneByQuery(queryWrapper);
    }

    private SysResource findSysResourceById(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id).eq("is_deleted", false);
        return sysResourceDao.selectOneByQuery(queryWrapper);
    }

}

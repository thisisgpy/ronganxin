package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.RoleBeanMapper;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.dao.SysRoleDao;
import com.ganpengyu.ronganxin.model.SysRole;
import com.ganpengyu.ronganxin.web.dto.role.CreateRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.QueryRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.SysRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.UpdateRoleDto;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 角色服务
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Service
public class RoleService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private RoleBeanMapper roleBeanMapper;

    /**
     * 创建角色
     *
     * @param createRoleDto 创建角色的数据传输对象，包含角色名称、编码等信息
     * @return 创建成功返回true，失败则抛出异常
     */
    @Transactional
    public boolean createRole(CreateRoleDto createRoleDto) {
        // 检查角色名称是否已存在
        long countByName = this.countByName(createRoleDto.getName(), null);
        CheckUtils.check(countByName == 0, "角色名称已存在");

        // 检查角色编码是否已存在
        long countByCode = this.countByCode(createRoleDto.getCode(), null);
        CheckUtils.check(countByCode == 0, "角色编码已存在");

        // 转换DTO为实体对象并设置创建信息
        SysRole sysRole = roleBeanMapper.toSysRole(createRoleDto);
        sysRole.setCreateBy(UserContext.getUsername());
        sysRole.setCreateTime(LocalDateTime.now());

        // 插入角色数据
        int rows = sysRoleDao.insertSelective(sysRole);
        CheckUtils.check(rows != 0, "新增角色失败");

        return true;
    }


    /**
     * 更新角色信息
     *
     * @param updateRoleDto 包含要更新的角色信息的数据传输对象，包含角色ID、名称、编码等信息
     * @return 更新成功返回true，失败则抛出异常
     */
    @Transactional
    public boolean updateRole(UpdateRoleDto updateRoleDto) {
        // 查找要更新的角色是否存在
        SysRole existingRole = this.findSysRoleById(updateRoleDto.getId());
        CheckUtils.check(null != existingRole, "角色不存在");

        // 检查角色名称是否已存在（如果提供了新名称）
        if (null != updateRoleDto.getName()) {
            long countByName = this.countByName(updateRoleDto.getName(), existingRole.getId());
            CheckUtils.check(countByName <= 0, "角色名称已存在");
        }

        // 检查角色编码是否已存在（如果提供了新编码）
        if (null != updateRoleDto.getCode()) {
            long countByCode = this.countByCode(updateRoleDto.getCode(), existingRole.getId());
            CheckUtils.check(countByCode <= 0, "角色编码已存在");
        }

        // 更新角色信息并保存到数据库
        this.roleBeanMapper.updateSysRole(updateRoleDto, existingRole);
        existingRole.setUpdateBy(UserContext.getUsername());
        existingRole.setUpdateTime(LocalDateTime.now());
        int rows = sysRoleDao.update(existingRole);
        CheckUtils.check(rows != 0, "更新角色失败");
        return true;
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 删除成功返回true
     */
    public boolean removeRole(Long id) {
        // 查找要删除的角色
        SysRole sysRole = this.findSysRoleById(id);
        CheckUtils.check(sysRole != null, "角色不存在");

        // 标记角色为已删除状态
        sysRole.setIsDeleted(true);
        sysRole.setUpdateBy(UserContext.getUsername());
        sysRole.setUpdateTime(LocalDateTime.now());

        // 更新角色信息
        int rows = sysRoleDao.update(sysRole);
        CheckUtils.check(rows != 0, "删除角色失败");

        return true;
    }

    /**
     * 查询所有角色信息并转换为DTO列表
     *
     * @return 角色DTO列表
     */
    public List<SysRoleDto> findRoleDtoList() {
        // 查询所有角色数据
        List<SysRole> sysRoles = sysRoleDao.selectAll();
        // 将角色实体列表转换为DTO列表
        return roleBeanMapper.toSysRoleDtoList(sysRoles);
    }

    /**
     * 分页查询角色信息
     *
     * @param queryRoleDto 查询条件对象，包含分页参数和查询条件
     * @return 分页结果对象，包含角色信息列表和分页信息
     */
    public PageResult<SysRoleDto> findRoleDtoPage(QueryRoleDto queryRoleDto) {
        CheckUtils.check(null != queryRoleDto, "查询参数不能为空");

        // 处理分页参数，默认页码为1，页面大小为10
        int pageNo = queryRoleDto.getPageNo() <= 0 ? 1 : queryRoleDto.getPageNo();
        int pageSize = queryRoleDto.getPageSize() <= 0 ? 10 : queryRoleDto.getPageSize();

        // 构造查询条件，过滤未删除数据并按创建时间倒序排列
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderBy("create_time", false);
        if (StringUtils.hasText(queryRoleDto.getName())) {
            queryWrapper.like("name", queryRoleDto.getName());
        }

        // 执行分页查询
        Page<SysRole> page = sysRoleDao.paginate(pageNo, pageSize, queryWrapper);
        if (page == null) {
            page = new Page<>(); // 防止空指针
        }

        // 4. 数据转换
        List<SysRole> records = page.getRecords();
        List<SysRoleDto> rows = roleBeanMapper.toSysRoleDtoList(records != null ? records : Collections.emptyList());

        // 5. 构造返回结果
        PageResult<SysRoleDto> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setPageNo(page.getPageNumber());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalCount(page.getTotalRow());
        pageResult.setTotalPages(page.getTotalPage());

        return pageResult;
    }

    /**
     * 根据ID查找系统角色DTO
     *
     * @param id 角色ID
     * @return 系统角色DTO对象
     */
    public SysRoleDto findSysRoleDtoById(Long id) {
        // 查找系统角色实体
        SysRole sysRole = this.findSysRoleById(id);
        // 检查角色是否存在
        CheckUtils.check(sysRole != null, "角色不存在");
        // 转换为DTO对象并返回
        return roleBeanMapper.toSysRoleDto(sysRole);
    }

    /**
     * 根据用户ID查询角色信息
     *
     * @param userId 用户ID
     * @return 角色信息列表，如果未找到则返回空列表
     */
    public List<SysRoleDto> findRoleByUserId(Long userId) {
        // 查询用户关联的角色信息
        List<SysRole> roles = sysRoleDao.findRoleByUserId(userId);
        // 如果查询结果为空，则返回空列表
        if (null == roles || roles.isEmpty()) {
            return Collections.emptyList();
        }
        // 将角色实体转换为DTO对象并返回
        return roleBeanMapper.toSysRoleDtoList(roles);
    }


    private long countByName(String name, Long excludeId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);
        if (null != excludeId) {
            queryWrapper.ne("id", excludeId);
        }
        return sysRoleDao.selectCountByQuery(queryWrapper);
    }

    private long countByCode(String code, Long excludeId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", code);
        if (null != excludeId) {
            queryWrapper.ne("id", excludeId);
        }
        return sysRoleDao.selectCountByQuery(queryWrapper);
    }

    private SysRole findSysRoleById(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id).eq("is_deleted", false);
        return sysRoleDao.selectOneByQuery(queryWrapper);
    }

}

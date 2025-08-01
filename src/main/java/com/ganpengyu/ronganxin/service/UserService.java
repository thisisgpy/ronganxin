package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.UserBeanMapper;
import com.ganpengyu.ronganxin.common.RaxException;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.common.util.CodecUtils;
import com.ganpengyu.ronganxin.dao.SysUserDao;
import com.ganpengyu.ronganxin.model.SysUser;
import com.ganpengyu.ronganxin.web.dto.*;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 系统用户服务
 *
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */
@Service
public class UserService {

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private UserBeanMapper userBeanMapper;

    @Value("${app.defaultPwd}")
    private String defaultPassword;

    /**
     * 创建新用户
     *
     * @param createUserDto 包含用户创建信息的数据传输对象
     * @return 创建成功返回true，失败则抛出异常
     */
    @Transactional
    public boolean createUser(CreateUserDto createUserDto) {
        SysUser existingUser = this.findUserByMobile(createUserDto.getMobile());
        CheckUtils.check(existingUser == null, "手机号已存在");

        // 将DTO转换为系统用户实体对象
        SysUser sysUser = userBeanMapper.toSysUser(createUserDto);

        // 设置用户密码，使用默认密码并加密
        sysUser.setPassword(CodecUtils.encryptPassword(defaultPassword));

        // 设置创建人和创建时间
        sysUser.setCreateBy(UserContext.getUsername());
        sysUser.setCreateTime(LocalDateTime.now());

        // 插入用户数据到数据库
        int rows = sysUserDao.insertSelective(sysUser);

        // 检查插入操作是否成功
        CheckUtils.check(rows != 0, "新增用户失败");
        return true;
    }

    /**
     * 更新用户信息
     *
     * @param updateUserDto 包含用户更新信息的数据传输对象，包含用户ID、手机号等信息
     * @return 更新成功返回true，失败则抛出异常
     */
    @Transactional
    public boolean updateUser(UpdateUserDto updateUserDto) {
        // 根据用户ID查找用户并验证用户是否存在
        SysUser sysUser = this.findUserById(updateUserDto.getId());
        CheckUtils.check(sysUser != null, "用户不存在");

        // 使用DTO更新用户信息
        userBeanMapper.updateSysUser(updateUserDto, sysUser);

        // 如果提供了手机号，则验证手机号唯一性
        if (StringUtils.hasText(updateUserDto.getMobile())) {
            SysUser existingUser = this.findUserByMobile(updateUserDto.getMobile());
            CheckUtils.check(existingUser == null || existingUser.getId().equals(sysUser.getId()), "手机号已存在");
        }

        sysUser.setUpdateBy(UserContext.getUsername());
        sysUser.setUpdateTime(LocalDateTime.now());

        // 执行数据库更新操作并检查更新结果
        int rows = sysUserDao.update(sysUser);
        CheckUtils.check(rows != 0, "更新用户失败");

        return true;
    }

    @Transactional
    public boolean resetPassword(Long id) {
        // 根据用户ID查找用户并验证用户是否存在
        SysUser sysUser = this.findUserById(id);
        CheckUtils.check(sysUser != null, "用户不存在");

        // 设置用户密码，使用默认密码并加密
        sysUser.setPassword(CodecUtils.encryptPassword(defaultPassword));
        sysUser.setIsDefaultPassword(true);
        sysUser.setUpdateBy(UserContext.getUsername());
        sysUser.setUpdateTime(LocalDateTime.now());
        int rows = sysUserDao.update(sysUser);
        CheckUtils.check(rows != 0, "重置用户密码失败");
        return true;
    }

    @Transactional
    public boolean changePassword(ChangePasswordDto changePasswordDto) {
        SysUser sysUser = this.findUserById(changePasswordDto.getId());
        CheckUtils.check(sysUser != null, "用户不存在");
        String encryptedOld = CodecUtils.encryptPassword(changePasswordDto.getOldPassword());
        CheckUtils.check(encryptedOld.equals(sysUser.getPassword()), "旧密码错误");

        sysUser.setPassword(CodecUtils.encryptPassword(changePasswordDto.getNewPassword()));
        sysUser.setUpdateBy(UserContext.getUsername());
        sysUser.setUpdateTime(LocalDateTime.now());
        int rows = sysUserDao.update(sysUser);
        CheckUtils.check(rows != 0, "修改密码失败");
        return true;
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除成功返回true
     */
    @Transactional
    public boolean removeUser(Long id) {
        // 根据用户ID查找用户并验证用户是否存在
        SysUser sysUser = this.findUserById(id);
        CheckUtils.check(sysUser != null, "用户不存在");

        // 软删除用户，将删除标志设置为true
        sysUser.setIsDeleted(true);
        sysUser.setUpdateBy(UserContext.getUsername());
        sysUser.setUpdateTime(LocalDateTime.now());

        int rows = sysUserDao.update(sysUser);
        CheckUtils.check(rows != 0, "删除用户失败");
        return true;
    }

    /**
     * 分页查询用户信息
     *
     * @param queryUserDto 查询条件封装对象，包含分页参数及查询条件：
     *                     - pageNo: 当前页码（从1开始），默认为1
     *                     - pageSize: 每页记录数，默认为10
     *                     - orgId: 组织ID，用于筛选指定组织下的用户
     *                     - mobile: 手机号模糊匹配条件
     *                     - name: 用户姓名模糊匹配条件
     *                     - status: 用户状态精确匹配条件
     * @return 返回分页结果对象 PageResult<SysUserDto>，包含以下字段：
     * - rows: 当前页数据列表，类型为 SysUserDto
     * - pageNo: 当前页码
     * - pageSize: 每页大小
     * - totalCount: 总记录数
     * - totalPages: 总页数
     * @throws RaxException 当传入的查询参数 queryUserDto 为空时抛出异常
     */
    public PageResult<SysUserDto> findUserPage(QueryUserDto queryUserDto) {
        // 1. 参数校验
        if (queryUserDto == null) {
            throw new RaxException("查询参数不能为空");
        }

        int pageNo = queryUserDto.getPageNo() <= 0 ? 1 : queryUserDto.getPageNo();
        int pageSize = queryUserDto.getPageSize() <= 0 ? 10 : queryUserDto.getPageSize();

        // 2. 构建查询条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderBy("create_time", false);
        if (queryUserDto.getOrgId() != null) {
            queryWrapper.eq("org_id", queryUserDto.getOrgId());
        }
        if (StringUtils.hasText(queryUserDto.getMobile())) {
            queryWrapper.like("mobile", queryUserDto.getMobile());
        }
        if (StringUtils.hasText(queryUserDto.getName())) {
            queryWrapper.like("name", queryUserDto.getName());
        }
        if (queryUserDto.getStatus() != null) {
            queryWrapper.eq("status", queryUserDto.getStatus());
        }

        // 3. 分页查询
        Page<SysUser> page = sysUserDao.paginate(pageNo, pageSize, queryWrapper);
        if (page == null) {
            page = new Page<>(); // 防止空指针
        }

        // 4. 数据转换
        List<SysUser> records = page.getRecords();
        List<SysUserDto> rows = userBeanMapper.toSysUserDtoList(records != null ? records : Collections.emptyList());

        // 5. 构造返回结果
        PageResult<SysUserDto> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setPageNo(page.getPageNumber());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalCount(page.getTotalRow());
        pageResult.setTotalPages(page.getTotalPage());

        return pageResult;
    }

    /**
     * 根据手机号码查找用户信息
     *
     * @param mobile 手机号码
     * @return 用户信息对象，如果未找到则返回null
     */
    public SysUser findUserByMobile(String mobile) {
        // 构造查询条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile).eq("is_deleted", false);
        // 执行查询并返回结果
        return sysUserDao.selectOneByQuery(queryWrapper);
    }

    /**
     * 根据用户ID查找用户DTO对象
     *
     * @param id 用户ID
     * @return 用户DTO对象
     */
    public SysUserDto findUserDtoById(Long id) {
        // 根据ID查找用户实体对象
        SysUser sysUser = this.findUserById(id);
        // 检查用户是否存在
        CheckUtils.check(sysUser != null, "用户不存在");
        // 将用户实体对象转换为DTO对象并返回
        return userBeanMapper.toSysUserDto(sysUser);
    }


    /**
     * 根据用户ID查找用户信息
     *
     * @param id 用户ID
     * @return 用户信息对象
     */
    private SysUser findUserById(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", id).eq("is_deleted", false);
        return sysUserDao.selectOneByQuery(queryWrapper);
    }

}

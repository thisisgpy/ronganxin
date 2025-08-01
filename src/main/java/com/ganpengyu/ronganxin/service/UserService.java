package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.UserBeanMapper;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.common.util.CodecUtils;
import com.ganpengyu.ronganxin.dao.SysUserDao;
import com.ganpengyu.ronganxin.model.SysUser;
import com.ganpengyu.ronganxin.web.dto.CreateUserDto;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 系统用户服务
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

    @Transactional
    public boolean createUser(CreateUserDto createUserDto) {
        SysUser sysUser = userBeanMapper.toSysUser(createUserDto);
        sysUser.setPassword(CodecUtils.encryptPassword(defaultPassword));
        sysUser.setCreateBy(UserContext.getUsername());
        sysUser.setCreateTime(LocalDateTime.now());
        int rows = sysUserDao.insertSelective(sysUser);
        CheckUtils.check(rows != 0, "新增用户失败");
        return true;
    }


}

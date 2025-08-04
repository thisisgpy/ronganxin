package com.ganpengyu.ronganxin.dao;

import com.ganpengyu.ronganxin.model.SysRole;
import com.mybatisflex.core.BaseMapper;

import java.util.List;

/**
 * 角色
 *
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

public interface SysRoleDao extends BaseMapper<SysRole> {

    List<SysRole> findRoleByUserId(Long userId);

}
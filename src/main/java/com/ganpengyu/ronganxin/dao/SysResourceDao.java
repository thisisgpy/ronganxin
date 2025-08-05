package com.ganpengyu.ronganxin.dao;

import com.ganpengyu.ronganxin.model.SysResource;
import com.mybatisflex.core.BaseMapper;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

public interface SysResourceDao extends BaseMapper<SysResource> {

    List<SysResource> findResourceByUserId(Long userId);

    List<SysResource> findResourceByRoleId(Long roleId);

}
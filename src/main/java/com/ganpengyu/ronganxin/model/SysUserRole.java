package com.ganpengyu.ronganxin.model;

import lombok.Data;

/**
 * 
 * 
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

@Data
public class SysUserRole {
    /**
    * 用户角色关系ID
    */
    private Long id;

    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 角色ID
    */
    private Long roleId;
}
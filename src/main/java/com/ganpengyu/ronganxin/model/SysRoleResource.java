package com.ganpengyu.ronganxin.model;

import lombok.Data;

/**
 * 
 * 
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

@Data
public class SysRoleResource {
    /**
    * 角色资源关系ID
    */
    private Long id;

    /**
    * 角色ID
    */
    private Long roleId;

    /**
    * 资源ID
    */
    private Long resourceId;
}
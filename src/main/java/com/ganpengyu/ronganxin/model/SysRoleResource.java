package com.ganpengyu.ronganxin.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

@Data
@Table("sys_role_resource")
public class SysRoleResource {
    /**
     * 角色资源关系ID
     */
    @Id(keyType = KeyType.Auto)
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
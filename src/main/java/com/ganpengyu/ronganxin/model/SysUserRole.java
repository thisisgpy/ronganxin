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
@Table("sys_user_role")
public class SysUserRole {
    /**
     * 用户角色关系ID
     */
    @Id(keyType = KeyType.Auto)
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
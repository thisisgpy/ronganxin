package com.ganpengyu.ronganxin.model;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户
 *
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */
@Data
@Table("sys_user")
public class SysUser {

    /**
     * 用户ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 性别. 男, 女
     */
    private String gender;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否初始化密码. 0:否, 1:是
     */
    private Boolean isDefaultPassword;

    /**
     * 状态. 0:禁用, 1:启用
     */
    private Integer status;

    /**
     * 是否删除. 0:否, 1:是
     */
    @Column(isLogicDelete = true)
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 信息更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 信息更新人
     */
    private String updateBy;
}
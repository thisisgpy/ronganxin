package com.ganpengyu.ronganxin.model;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * 
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

@Data
public class SysRole {
    /**
    * 角色ID
    */
    private Long id;

    /**
    * 角色编码
    */
    private String code;

    /**
    * 角色名称
    */
    private String name;

    /**
    * 角色备注
    */
    private String comment;

    /**
    * 是否删除. 0:否, 1:是
    */
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
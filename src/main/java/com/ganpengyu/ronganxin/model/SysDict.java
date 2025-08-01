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
public class SysDict {
    /**
    * 字典ID
    */
    private Long id;

    /**
    * 字典编码
    */
    private String code;

    /**
    * 字典名称
    */
    private String name;

    /**
    * 字典备注
    */
    private String comment;

    /**
    * 是否启用. 0: 禁用, 1: 启用
    */
    private Boolean isEnabled;

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
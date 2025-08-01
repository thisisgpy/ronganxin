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
public class SysDictItem {
    /**
    * 字典项ID
    */
    private Long id;

    /**
    * 字典ID
    */
    private Long dictId;

    /**
    * 字典编码
    */
    private String dictCode;

    /**
    * 字典项标签
    */
    private String label;

    /**
    * 字典项值
    */
    private String value;

    /**
    * 字典项备注
    */
    private String comment;

    /**
    * 字典项排序
    */
    private Integer sort;

    /**
    * 父级字典项ID. 0表示没有父级字典项
    */
    private Long parentId;

    /**
    * 是否启用,不对子级生效. 0: 禁用, 1: 启用
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
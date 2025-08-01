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
public class AssetFixed {
    /**
    * 固定资产ID
    */
    private Long id;

    /**
    * 固定资产名称
    */
    private String name;

    /**
    * 所属组织ID
    */
    private Long orgId;

    /**
    * 是否删除. 0: 否, 1: 是
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
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
public class SysOrg {
    /**
    * 组织ID
    */
    private Long id;

    /**
    * 组织编码. 4位一级. 0001,00010001,000100010001,以此类推
    */
    private String code;

    /**
    * 组织名称
    */
    private String name;

    /**
    * 组织名称简称
    */
    private String nameAbbr;

    /**
    * 组织备注
    */
    private String comment;

    /**
    * 父级组织ID. 0表示没有父组织
    */
    private Long parentId;

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
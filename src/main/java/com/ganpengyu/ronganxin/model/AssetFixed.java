package com.ganpengyu.ronganxin.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

@Data
@Table(value = "asset_fixed")
public class AssetFixed {

    /**
     * 固定资产ID
     */
    @Id(keyType = KeyType.None)
    private Long id;

    /**
     * 固定资产名称
     */
    private String name;

    /**
     * 固定资产编号
     */
    private String code;

    /**
     * 固定资产地址
     */
    private String address;

    /**
     * 固定资产账面价值，以分计算
     */
    private Long bookValue;

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
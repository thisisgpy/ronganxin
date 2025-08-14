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
@Table("sys_bank")
public class SysBank {

    /**
     * 联行号
     */
    @Id(keyType = KeyType.None)
    private String code;

    /**
     * 银行名称
     */
    private String name;

    /**
     * 银行名称简称
     */
    private String nameAbbr;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 支行名称
     */
    private String branchName;
}
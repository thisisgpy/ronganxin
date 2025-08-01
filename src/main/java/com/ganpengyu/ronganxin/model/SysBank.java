package com.ganpengyu.ronganxin.model;

import lombok.Data;

/**
 * 
 * 
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

@Data
public class SysBank {
    /**
    * 联行号
    */
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
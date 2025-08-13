package com.ganpengyu.ronganxin.common.component;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
public enum AuthMatchType {

    /**
     * 只需要登录即可访问
     */
    NONE,

    /**
     * 同时具备指定的权限编码才允许访问
     */
    ALL,

    /**
     * 具备任一权限编码即可访问
     */
    ANY,

}

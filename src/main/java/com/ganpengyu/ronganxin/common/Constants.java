package com.ganpengyu.ronganxin.common;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/11
 */
public class Constants {

    /**
     * uid -> 用户信息
     */
    public static String CACHE_USER_PREFIX = "user:uid:";

    /**
     * uid -> token
     */
    public static String CACHE_TOKEN_PREFIX = "token:uid:";

    /**
     * uid -> resource codes
     */
    public static String CACHE_RESOURCE_CODES_PREFIX = "resource:codes:uid:";

    /**
     * 缓存认证信息的生存时间（TTL）
     * 单位：秒
     * 默认值：6小时（60 * 60 * 6 = 21600秒）
     */
    public static long CACHE_AUTH_TTL = 60 * 60 * 6;

    public static String getCacheUserKey(Long uid) {
        return CACHE_USER_PREFIX + uid;
    }

    public static String getCacheTokenKey(Long uid) {
        return CACHE_TOKEN_PREFIX + uid;
    }

    public static String getCacheResourceCodesKey(Long uid) {
        return CACHE_RESOURCE_CODES_PREFIX + uid;
    }

}

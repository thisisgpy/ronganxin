package com.ganpengyu.ronganxin.common;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/11
 */
public class Constants {

    /**
     * 缓存中token到用户信息的键前缀
     */
    public static String CACHE_TOKEN_USER_PREFIX = "token:tu:";

    /**
     * 缓存中用户ID到token的键前缀
     */
    public static String CACHE_UID_TOKEN_PREFIX = "token:ut:";

    /**
     * 缓存中用户资源码的键前缀
     */
    public static String CACHE_RESOURCE_CODES_PREFIX = "resource:codes:";

    /**
     * 缓存认证信息的生存时间（TTL）
     * 单位：秒
     * 默认值：6小时（60 * 60 * 6 = 21600秒）
     */
    public static long CACHE_AUTH_TTL = 60 * 60 * 6;

    /**
     * 根据token生成缓存中token到用户信息的键
     *
     * @param token 用户token
     * @return 缓存键字符串
     */
    public static String getCacheTokenUserKey(String token) {
        return CACHE_TOKEN_USER_PREFIX + token;
    }

    /**
     * 根据用户ID生成缓存中用户ID到token的键
     *
     * @param uid 用户ID
     * @return 缓存键字符串
     */
    public static String getCacheUidTokenKey(Long uid) {
        return CACHE_UID_TOKEN_PREFIX + uid;
    }

    /**
     * 根据用户ID生成缓存中用户资源码的键
     *
     * @param uid 用户ID
     * @return 缓存键字符串
     */
    public static String getCacheResourceCodesKey(Long uid) {
        return CACHE_RESOURCE_CODES_PREFIX + uid;
    }


}

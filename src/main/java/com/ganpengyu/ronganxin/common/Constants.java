package com.ganpengyu.ronganxin.common;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/11
 */
public class Constants {

    public static final String AUTH_PREFIX = "auth:";

    public static final String AUTH_TOKEN = AUTH_PREFIX + "%s:" + "token";

    public static final String AUTH_USER_INFO = AUTH_PREFIX + "%s:" + "userInfo";

    public static final String AUTH_RESOURCE_CODES = AUTH_PREFIX + "%s:" + "codes";

    public static final Long AUTH_TTL = 60 * 60 * 6L;

    public static String getAuthTokenKey(Long userId) {
        return String.format(AUTH_TOKEN, userId);
    }

    public static String getAuthUserInfoKey(Long userId) {
        return String.format(AUTH_USER_INFO, userId);
    }

    public static String getAuthResourceCodesKey(Long userId) {
        return String.format(AUTH_RESOURCE_CODES, userId);
    }

}

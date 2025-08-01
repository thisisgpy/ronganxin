package com.ganpengyu.ronganxin.common.util;

import com.ganpengyu.ronganxin.common.RaxException;

/**
 * 逻辑检查工具
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
public class CheckUtils {

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new RaxException(message);
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

}

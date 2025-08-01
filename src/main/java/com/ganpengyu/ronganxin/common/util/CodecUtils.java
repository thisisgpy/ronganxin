package com.ganpengyu.ronganxin.common.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
public class CodecUtils {

    /**
     * 加密
     *
     * @param plainPassword 明文密码
     * @return 密文密码
     */
    public static String encryptPassword(String plainPassword) {
        return DigestUtils.sha512Hex(plainPassword);
    }

}

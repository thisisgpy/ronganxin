package com.ganpengyu.ronganxin.common.context;

import com.ganpengyu.ronganxin.web.dto.user.LoginUserDto;

/**
 * 用户信息上下文
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
public class UserContext {

    private static final ThreadLocal<LoginUserDto> contextHolder = new ThreadLocal<>();

    public static void setContext(LoginUserDto user) {
        contextHolder.set(user);
    }

    public static String getUsername() {
        return contextHolder.get().getUserInfo().getName();
    }

    public static Long getUserId() {
        return contextHolder.get().getUserInfo().getId();
    }

    public static String getMobile() {
        return contextHolder.get().getUserInfo().getMobile();
    }

    public static void removeContext() {
        contextHolder.remove();
    }

}

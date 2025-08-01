package com.ganpengyu.ronganxin.common.context;

/**
 * 用户信息上下文
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
public class UserContext {

    private static final ThreadLocal<LoginUser> contextHolder = new ThreadLocal<>();

    public static void setContext(LoginUser user) {
        contextHolder.set(user);
    }

    public static String getUsername() {
        return contextHolder.get().getUsername();
    }

    public static void removeContext() {
        contextHolder.remove();
    }

}

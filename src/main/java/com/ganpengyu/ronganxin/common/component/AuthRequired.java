package com.ganpengyu.ronganxin.common.component;

import java.lang.annotation.*;

/**
 * 接口权限校验注解。被注解的方法，一定需要用户处于登录状态。
 * 如果一个接口只需要登录状态，不需要任何权限配置就可以访问，那么 value 属性可以不配置。
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AuthRequired {

    /**
     * 资源权限码，当 matchType = NONE 时，这里配置的任何权限都将被忽略
     */
    String[] value() default {};

    /**
     * 权限匹配类型，默认为 ALL，表示必须所有权限匹配
     */
    AuthMatchType matchType() default AuthMatchType.ALL;

}

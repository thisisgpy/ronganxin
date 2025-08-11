package com.ganpengyu.ronganxin.common.component;

import java.lang.annotation.*;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Auth {

    /**
     * 资源权限码
     */
    String[] value() default {};

}

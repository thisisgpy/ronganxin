package com.ganpengyu.ronganxin.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口统一返回数据模型
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
@Data
public class RaxResult<T> implements Serializable {

    private boolean success;

    private String message;

    private T data;

    public RaxResult(T data) {
        this.success = true;
        this.data = data;
    }

    public RaxResult(String message) {
        this.success = false;
        this.message = message;
    }

    public static <T> RaxResult<T> ok(T data) {
        return new RaxResult<>(data);
    }

    public static <T> RaxResult<T> error(String message) {
        return new RaxResult<>(message);
    }

}

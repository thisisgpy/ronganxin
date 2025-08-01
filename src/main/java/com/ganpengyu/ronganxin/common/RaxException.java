package com.ganpengyu.ronganxin.common;

/**
 * 统一业务异常
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
public class RaxException extends RuntimeException {

    public RaxException() {
    }

    public RaxException(String message) {
        super(message);
    }

    public RaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RaxException(Throwable cause) {
        super(cause);
    }

    public RaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

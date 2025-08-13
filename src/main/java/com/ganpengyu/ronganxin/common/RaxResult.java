package com.ganpengyu.ronganxin.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口统一返回数据模型
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
@Data
@NoArgsConstructor
@Schema(name = "RaxResult")
public class RaxResult<T> implements Serializable {

    @Schema(name = "请求是否成功")
    private boolean success;

    @Schema(name = "请求失败信息")
    private String message;

    @Schema(name = "请求数据")
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

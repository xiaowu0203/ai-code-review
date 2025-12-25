package com.ai.codereview.dto;

import lombok.Data;

/**
 * 通用返回结果类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Data
public class CommonResult<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public CommonResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public CommonResult(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public CommonResult(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<>(200, "操作成功");
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "操作成功", data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(200, message, data);
    }

    public static <T> CommonResult<T> failed() {
        return new CommonResult<>(500, "操作失败");
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(500, message);
    }

    public static <T> CommonResult<T> failed(Integer code, String message) {
        return new CommonResult<>(code, message);
    }
}
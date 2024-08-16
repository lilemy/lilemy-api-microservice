package com.lilemy.common.exception;


import com.lilemy.common.common.ResultCode;

/**
 * 抛异常工具类
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition        异常条件
     * @param runtimeException 运行异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition  异常条件
     * @param resultCode 状态码
     */
    public static void throwIf(boolean condition, ResultCode resultCode) {
        throwIf(condition, new BusinessException(resultCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition  异常条件
     * @param resultCode 状态码
     * @param message    异常信息
     */
    public static void throwIf(boolean condition, ResultCode resultCode, String message) {
        throwIf(condition, new BusinessException(resultCode, message));
    }
}

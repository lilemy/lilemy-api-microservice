package com.lilemy.common.common;

/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 成功封装类
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param resultCode 状态码
     * @param <T>        数据类型
     * @return 失败封装类
     */
    public static <T> BaseResponse<T> error(ResultCode resultCode) {
        return new BaseResponse<>(resultCode);
    }

    /**
     * 失败
     *
     * @param code    状态码
     * @param message 信息
     * @param <T>     数据类型
     * @return 失败封装类
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse(code, null, message);
    }

    /**
     * 失败
     *
     * @param resultCode 状态码
     * @param <T>        数据类型
     * @return 失败封装类
     */
    public static <T> BaseResponse<T> error(ResultCode resultCode, String message) {
        return new BaseResponse<>(resultCode.getCode(), null, message);
    }
}

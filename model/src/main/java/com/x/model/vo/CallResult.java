package com.x.model.vo;

import lombok.Data;

@Data
public class CallResult<T>  {

    private Boolean success;
    private Integer code;
    public String message;
    private T data;

    public CallResult() {}

    public CallResult(boolean isSuccess, int code, String message, T resultObject) {
        this.success = isSuccess;
        this.code = code;
        this.message = message;
        this.data = resultObject;
    }
    
    public static <T> CallResult<T> success() {
        return new CallResult(true, 0, "Success", (Object) null);
    }

    public static <T> CallResult<T> success(T resultObject) {
        return new CallResult(true, 0, "Success", resultObject);
    }

    public static <T> CallResult<T> success(int code, String msg, T resultObject) {
        return new CallResult(true, code, msg, resultObject);
    }

    public static <T> CallResult<T> failure() {
        return new CallResult(false, -1, "system error", (Object) null);
    }

    public static <T> CallResult<T> failure(String msg) {
        return new CallResult(false, -1, msg, (Object) null);
    }

    public static <T> CallResult<T> failure(int code) {
        return new CallResult(false, code, "system error", (Object) null);
    }

    public static <T> CallResult<T> failure(int code, String msg) {
        return new CallResult(false, code, msg, (Object) null);
    }
}

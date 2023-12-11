package com.wms.exception;

/*
 自定义时的运行异常
*/
public class BusinessException extends RuntimeException {
    // 只创建异常对象
    public BusinessException() {
        super();
    }

    // 创建异常对象同时指定异常信息
    public BusinessException(String message) {
        super(message);
    }
}

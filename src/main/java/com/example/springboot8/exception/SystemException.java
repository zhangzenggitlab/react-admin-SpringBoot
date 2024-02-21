package com.example.springboot8.exception;

// 自定义系统异常,直接抛出数据

import lombok.Data;
import org.springframework.stereotype.Component;

//@Component
public class SystemException extends RuntimeException {
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public SystemException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public SystemException(Integer code, String message, Throwable cause) {
        super(message, cause);
        System.out.println("sssss");
        this.code = code;
    }

}

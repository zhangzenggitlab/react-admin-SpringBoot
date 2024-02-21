package com.example.springboot8.common;

import lombok.Data;

import java.util.HashMap;

@Data
public class ResponseResult{
    private int code;
    private String msg;
    private Object data;

    public ResponseResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public HashMap<String, Object> response(int code, String msg, Object data) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", code);
        hashMap.put("msg", msg);
        hashMap.put("data", data);
        return hashMap;
    }
}

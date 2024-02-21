package com.example.springboot8.constants;

import java.util.HashMap;
import java.util.Map;

public class HttpCode {

    public final static int CODE_SUCCEES = 200;              // 成功

    public final static int CODE_FAIL = 400;                 // 参数错误等

    public final static int CODE_UNAUTHORIZED = 401;         // 没有登录

    public final static int CODE_FORBIDDEN = 403;            // 权限不足

    public final static int CODE_SERVICEERROR = 500;         // 操作失败
    public final static Map<Integer, String> constants = new HashMap<Integer, String>() {{
        put(CODE_SUCCEES, "成功");
        put(CODE_FAIL, "参数错误");
        put(CODE_UNAUTHORIZED, "请先登录登录");
        put(CODE_FORBIDDEN, "权限不足");
        put(CODE_SERVICEERROR, "操作失败");
    }};


}

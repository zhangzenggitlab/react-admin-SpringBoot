package com.example.springboot8.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共工具类
 */
public class CommonUtil {

   static public Boolean inArray(List arrayList, Object value) {
        Boolean bool = false;

        for (int i = 0; i < arrayList.size(); i++) {
            if (value == arrayList.get(i)) {
                bool = true;
                break;
            }
        }
        return bool;
    }
}

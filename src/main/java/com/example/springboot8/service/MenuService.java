package com.example.springboot8.service;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.Menu;

public interface MenuService {
    ResponseResult list(int page,int pageSize,String name,String status);

    ResponseResult all();

    ResponseResult menuById(Menu menu);

    ResponseResult delMenuById(int id);

    ResponseResult add(Menu menu);

    ResponseResult updateMenuById(Menu menu);

    ResponseResult menuRouter();
}

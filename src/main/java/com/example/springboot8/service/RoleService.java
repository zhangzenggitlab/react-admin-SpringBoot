package com.example.springboot8.service;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.Menu;
import com.example.springboot8.entity.Role;

public interface RoleService {
    // 分页获取
    ResponseResult list(int page, int pageSize);

    // 获取树状全部数据
    ResponseResult allTree();


    // 根据id删除
    ResponseResult delRoleById(int id);

    // 新增一条数据
    ResponseResult add(Role role);

    // 根据id更新一条数据
    ResponseResult updateRoleById(Role role);

}

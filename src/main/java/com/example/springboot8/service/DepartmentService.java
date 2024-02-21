package com.example.springboot8.service;

import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.Department;

public interface DepartmentService  {

    // 分页获取
    ResponseResult list(int page, int pageSize, String name,int status);

    // 获取树状数据(全部)
    ResponseResult all();

    // 根据id删除
    ResponseResult delById(int id);

    // 根据id查询更新数据
    ResponseResult updateDepartmentById(Department department);

    // 插入一条数据
    ResponseResult add(Department department);
}

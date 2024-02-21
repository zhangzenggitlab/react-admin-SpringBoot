package com.example.springboot8.service;

import com.example.springboot8.common.ResponseResult;

import java.util.List;

public interface MenuRoleService {

    /**
     * 菜单绑定角色
     * @param roleId 角色id
     * @param menuIds  菜单id数组
     * @return
     */
    ResponseResult add(int roleId, List menuIds);

    // 根据角色id查询菜单
    ResponseResult menuRoleByRoleId(int roleId);
}

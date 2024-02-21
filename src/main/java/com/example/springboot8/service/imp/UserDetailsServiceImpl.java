package com.example.springboot8.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot8.controller.LoginUser;
import com.example.springboot8.entity.*;
import com.example.springboot8.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// 重写spring security 的登录方法

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(lambdaQueryWrapper);

        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }

        // 查询当前用户角色所有数据操作权限
        List<String> permission = new ArrayList<>();

        if (user.getId() == 1) {
            permission.add("admin");
            return new LoginUser(user, permission);
        }

        // 1 查询绑定的角色id
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(UserRole::getUserId, user.getId());
        lambdaQueryWrapper1.select(UserRole::getRoleId);

        List roleList = userRoleMapper.selectObjs(lambdaQueryWrapper1);

        if (roleList.size() == 0) {
            return new LoginUser(user, permission);
        }

        // 查询角色标识符
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.in(Role::getId, roleList);
        roleLambdaQueryWrapper.select(Role::getPermission);
        List rolePermission = roleMapper.selectObjs(roleLambdaQueryWrapper);


        for (int i = 0; i < rolePermission.size(); i++) {
            permission.add((String) rolePermission.get(i));
        }


        // 根据角色id查询菜单id
        LambdaQueryWrapper<MenuRole> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.in(MenuRole::getRoleId, roleList);
        lambdaQueryWrapper2.select(MenuRole::getMenuId);

        List menuRoleList = menuRoleMapper.selectObjs(lambdaQueryWrapper2);

        if (menuRoleList.size() == 0) {
            return new LoginUser(user, permission);
        }

        // 根据菜单id查询对应的权限标识符
        LambdaQueryWrapper<Menu> lambdaQueryWrapper3 = new LambdaQueryWrapper<Menu>();
        lambdaQueryWrapper3.in(Menu::getId, menuRoleList);
        lambdaQueryWrapper3.in(Menu::getStatus, 1);
        lambdaQueryWrapper3.eq(Menu::getType, 3);
        lambdaQueryWrapper3.select(Menu::getPermission);

        List permissionList = menuMapper.selectObjs(lambdaQueryWrapper3);

        for (int i = 0; i < permissionList.size(); i++) {
            permission.add((String) permissionList.get(i));
        }

        return new LoginUser(user, permission);
    }


}

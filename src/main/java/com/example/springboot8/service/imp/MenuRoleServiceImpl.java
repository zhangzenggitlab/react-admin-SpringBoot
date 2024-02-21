package com.example.springboot8.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.entity.Menu;
import com.example.springboot8.entity.MenuRole;
import com.example.springboot8.mapper.MenuMapper;
import com.example.springboot8.mapper.MenuRoleMapper;
import com.example.springboot8.service.MenuRoleService;
import com.example.springboot8.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Transactional
    @Override
    public ResponseResult add(int roleId, List menuIds) {

        LambdaQueryWrapper<MenuRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MenuRole::getRoleId, roleId);
        lambdaQueryWrapper.select(MenuRole::getMenuId, MenuRole::getId);

        List<MenuRole> MenuRole = menuRoleMapper.selectList(lambdaQueryWrapper);

        // 已拥有roleId列表
        Map<Integer, MenuRole> menuRoleMap = new HashMap<>();                           // String => roleId

        for (int i = 0; i < MenuRole.size(); i++) {
            menuRoleMap.put(MenuRole.get(i).getMenuId(), MenuRole.get(i));
        }

        // 对比数组不同
        // 1. 检查数据库没有的就新增
        for (int i = 0; i < menuIds.size(); i++) {
            // 不存在则说明是新增的角色
            if (menuRoleMap.get(menuIds.get(i)) == null) {
                MenuRole menuRole = new MenuRole();
                menuRole.setMenuId((int) menuIds.get(i));
                menuRole.setRoleId(roleId);
                menuRoleMapper.insert(menuRole);
            }
        }

        // 2.上传没有的就删除数据库
        menuRoleMap.forEach((key, value) -> {
            Boolean bool = CommonUtil.inArray(menuIds, key);

            if (!bool) {
                menuRoleMapper.deleteById(value.getId());
            }
        });

        return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);
    }

    @Override
    public ResponseResult menuRoleByRoleId(int roleId) {
        LambdaQueryWrapper<MenuRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MenuRole::getRoleId, roleId);
//        lambdaQueryWrapper.eq(MenuRole::getParentId, roleId);
        lambdaQueryWrapper.select(MenuRole::getMenuId);

        List menuId = new ArrayList<>();
        List menuIds = menuRoleMapper.selectObjs(lambdaQueryWrapper);

        // 如果有子数据则删除(配合前端显示)
        for (int i = 0; i < menuIds.size(); i++) {
            LambdaQueryWrapper<Menu> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(Menu::getParentId, menuIds.get(i));
            List<Menu> menuRole = menuMapper.selectList(lambdaQueryWrapper2);


            if (menuRole.size() == 0) {
                menuId.add(menuIds.get(i));
            }
        }

        return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), menuId);

    }
}

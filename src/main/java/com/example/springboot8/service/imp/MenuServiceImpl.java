package com.example.springboot8.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot8.common.ResponsePage;
import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.entity.Menu;
import com.example.springboot8.entity.MenuRole;
import com.example.springboot8.entity.UserRole;
import com.example.springboot8.mapper.MenuMapper;
import com.example.springboot8.mapper.MenuRoleMapper;
import com.example.springboot8.mapper.UserRoleMapper;
import com.example.springboot8.service.MenuService;
import com.example.springboot8.utils.GetUserIdByToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private GetUserIdByToken getUserIdByToken;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Override
    public ResponseResult list(int page, int pageSize, String name, String status) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(Menu::getStatus, 1);

        if (!name.isEmpty()) {
            lambdaQueryWrapper.eq(Menu::getName, name);
        }

        if (!"".equals(status) && !"0".equals(status)) {
            lambdaQueryWrapper.eq(Menu::getStatus, status);
        }

        lambdaQueryWrapper.eq(Menu::getParentId, 0);

        lambdaQueryWrapper.orderByAsc(Menu::getSort);

        IPage ipage = new Page(page, pageSize);
        List<Map<String, Object>> list = menuMapper.selectMapsPage(ipage, lambdaQueryWrapper).getRecords();

        for (int i = 0; i < list.size(); i++) {
            List children = findChildrenById((int) list.get(i).get("id"));
            if (children.size() > 0) {
                list.get(i).put("children", children);
            }
        }

        return new ResponseResult(200, "查询成功", new ResponsePage((int) ipage.getCurrent(), (int) ipage.getPages(), (int) ipage.getTotal(), list));
    }

    @Override
    public ResponseResult all() {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getStatus, 1);
        lambdaQueryWrapper.eq(Menu::getParentId, 0);

        lambdaQueryWrapper.select(Menu::getId, Menu::getName);

        List<Map<String, Object>> menu = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).put("children", findChildrenIdAndNameById((int) menu.get(i).get("id")));
        }

        return new ResponseResult(200, "查询成功", menu);
    }

    @Override
    public ResponseResult menuById(Menu menu) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Menu::getId, menu.getId());

        Boolean bool = update(menu, lambdaQueryWrapper);


        if (bool) return new ResponseResult(200, "成功", null);

        return new ResponseResult(500, "失败", null);
    }

    @Override
    public ResponseResult delMenuById(int id) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Menu::getId, id);

        // 事务处理
        Boolean bool = removeById(id);

        delByParentId(id);

        // 删除子节点
        if (bool) return new ResponseResult(200, "成功", null);

        return new ResponseResult(500, "失败", null);
    }

    @Override
    public ResponseResult add(Menu menu) {

        Boolean bool = save(menu);

        if (bool) return new ResponseResult(200, "成功", null);

        return new ResponseResult(500, "失败", null);
    }

    @Override
    public ResponseResult updateMenuById(Menu menu) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Menu::getId, menu.getId());
        Boolean bool = update(menu, lambdaQueryWrapper);

        if (bool) return new ResponseResult(200, "成功", null);

        return new ResponseResult(500, "失败", null);
    }


    // 递归删除子节点数据id，逐渐删除
    private void delByParentId(int id) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId, id);

        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            delByParentId((Integer) list.get(i).get("id"));
        }

        removeById(id);
    }

    private List<Map<String, Object>> findChildrenById(int id) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId, id);
//        lambdaQueryWrapper.eq(Menu::getStatus, 1);
        lambdaQueryWrapper.orderByAsc(Menu::getSort);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> children = findChildrenById((Integer) list.get(i).get("id"));
            if (children.size() > 0) {
                list.get(i).put("children", children);
            }
        }

        return list;
    }

    private List<Map<String, Object>> findChildrenIdAndNameById(int id) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId, id);
        lambdaQueryWrapper.eq(Menu::getStatus, 1);

        lambdaQueryWrapper.select(Menu::getId, Menu::getName);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> list2 = findChildrenIdAndNameById((Integer) list.get(i).get("id"));
            list.get(i).put("children", list2);
        }

        return list;
    }

    /**
     * 查询当前登录用户的菜单路由
     *
     * @return
     */
    public ResponseResult menuRouter() {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getStatus, 1);
        lambdaQueryWrapper.eq(Menu::getParentId, 0);
        lambdaQueryWrapper.orderByAsc(Menu::getSort);
        // 查询目录和菜单
        List types = new ArrayList();
        types.add(1);
        types.add(2);
        lambdaQueryWrapper.in(Menu::getType, types);
        lambdaQueryWrapper.select(Menu::getId, Menu::getType, Menu::getName, Menu::getPath, Menu::getIcon, Menu::getHideMenu);

        int userId = getUserIdByToken.getUserIdByToken();

        // 超级管理员直接返回
        if (userId == 1) {
            List<Map<String, Object>> list = menuMapper.selectMaps(lambdaQueryWrapper);
            List menuRoleList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {

                List list1 = findChildrenRouterById((int) list.get(i).get("id"), menuRoleList,true);
                if (list1.size() > 0) {
                    list.get(i).put("children", list1);
                }
                list.get(i).put("key", list.get(i).get("path"));
                list.get(i).put("menu", list.get(i).get("path"));
                list.get(i).put("label", list.get(i).get("name"));
                list.get(i).remove("id");
            }
            return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), list);
        }

        // 查询当前用户角色所有数据操作权限
        List<String> permission = new ArrayList<>();

        // 1 查询角色
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(UserRole::getUserId, userId);
        lambdaQueryWrapper1.select(UserRole::getRoleId);

        List roleList = userRoleMapper.selectObjs(lambdaQueryWrapper1);

        if (roleList.size() == 0) {
            return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);
        }

        // 根据角色id查询菜单id
        LambdaQueryWrapper<MenuRole> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.in(MenuRole::getRoleId, roleList);
        lambdaQueryWrapper2.select(MenuRole::getMenuId);

        // 如果拥有子页面权限那一定有父页面权限，所以需要查一下父页面id
        List menuRoleList = menuRoleMapper.selectObjs(lambdaQueryWrapper2);

        LambdaQueryWrapper<MenuRole> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper4.in(MenuRole::getId, menuRoleList);

        List parentMenu = menuRoleMapper.selectObjs(lambdaQueryWrapper4);

        for (int i = 0; i < parentMenu.size(); i++) {
            menuRoleList.add(parentMenu.get(i));
        }

        if (menuRoleList.size() == 0) {
            return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);
        }


        // 根据菜单id查询对应的权限标识符
        LambdaQueryWrapper<Menu> lambdaQueryWrapper3 = new LambdaQueryWrapper<Menu>();
        lambdaQueryWrapper3.in(Menu::getId, menuRoleList);
        lambdaQueryWrapper3.in(Menu::getType, types);
        lambdaQueryWrapper3.eq(Menu::getStatus, 1);
        lambdaQueryWrapper3.eq(Menu::getParentId, 0);
        lambdaQueryWrapper3.orderByAsc(Menu::getSort);

        lambdaQueryWrapper3.select(Menu::getId, Menu::getType, Menu::getName, Menu::getPath, Menu::getIcon, Menu::getHideMenu);

        List<Map<String, Object>> permissionList = menuMapper.selectMaps(lambdaQueryWrapper3);


        for (int i = 0; i < permissionList.size(); i++) {
            List list1 = findChildrenRouterById((int) permissionList.get(i).get("id"), menuRoleList,false);
            if (list1.size() > 0) {
                permissionList.get(i).put("children", list1);
            }
            permissionList.get(i).put("key", permissionList.get(i).get("path"));
            permissionList.get(i).put("menu", permissionList.get(i).get("path"));
            permissionList.get(i).put("label", permissionList.get(i).get("name"));
            permissionList.get(i).remove("id");
        }

        return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), permissionList);
    }


    /**
     * 查询自子菜单路由
     * @param id
     * @param menuRoleList
     * @param isSuperAdmin  是否是超级管理员
     * @return
     */
    private List<Map<String, Object>> findChildrenRouterById(int id, List menuRoleList,Boolean isSuperAdmin) {

        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId, id);
        lambdaQueryWrapper.eq(Menu::getStatus, 1);

        // 查询目录和菜单
        List types = new ArrayList();
        types.add(1);
        types.add(2);
        lambdaQueryWrapper.in(Menu::getType, types);


        // 不是管理员就需要判断是否拥有这个权限
        if(!isSuperAdmin){
            lambdaQueryWrapper.in(Menu::getId, menuRoleList);
        }

        lambdaQueryWrapper.orderByAsc(Menu::getSort);
        lambdaQueryWrapper.select(Menu::getId, Menu::getType, Menu::getName, Menu::getPath, Menu::getIcon, Menu::getHideMenu);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> list2 = findChildrenRouterById((Integer) list.get(i).get("id"),menuRoleList,isSuperAdmin);
            if (list2.size() > 0) {
                list.get(i).put("children", list2);
            }
            list.get(i).put("key", list.get(i).get("path"));
            list.get(i).put("menu", list.get(i).get("path"));
            list.get(i).put("label", list.get(i).get("name"));
            list.get(i).remove("id");
        }

        return list;
    }

}

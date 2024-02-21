package com.example.springboot8.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot8.common.ResponsePage;
import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.entity.Role;
import com.example.springboot8.mapper.RoleMapper;
import com.example.springboot8.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseResult list(int page, int pageSize) {

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getParentId,0);
        IPage ipage = new Page(page, pageSize);

        lambdaQueryWrapper.select(Role::getName, Role::getStatus, Role::getId, Role::getPermission, Role::getDescription, Role::getParentId);

        List<Map<String, Object>> list = roleMapper.selectMapsPage(ipage, lambdaQueryWrapper).getRecords();

        for (int i = 0; i < list.size(); i++) {
            // 查询子节点
            List<Map<String, Object>> list2 = findChildrenById((Integer) list.get(i).get("id"));
            if (list2.size() > 0){
                list.get(i).put("children", list2);
            }

        }

        return new ResponseResult(200, "查询成功", new ResponsePage((int) ipage.getCurrent(), (int) ipage.getPages(), (int) ipage.getTotal(), list));
    }

    @Override
    public ResponseResult allTree() {

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getStatus, 1);
        lambdaQueryWrapper.eq(Role::getParentId, 0);
        lambdaQueryWrapper.select(Role::getId, Role::getName);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("children", findChildrenIdAndNameById((int) list.get(i).get("id")));
        }

        return new ResponseResult(200, "查询成功", list);
    }

    @Override
    public ResponseResult delRoleById(int id) {

        int del = roleMapper.deleteById(id);
        delByParentId(id);

        if (del == 1) return new ResponseResult(200, "成功", null);

        return new ResponseResult(400, "失败", null);
    }

    @Override
    public ResponseResult add(Role role) {

        // 权限标识唯一，插入之前查询一下
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getPermission, role.getPermission());

        Role permits = roleMapper.selectOne(lambdaQueryWrapper);
        if (permits != null) return new ResponseResult(500, "标识符重复", null);

        Role role1 = new Role();
        role1.setName(role.getName());
        role1.setStatus(role.getStatus());
        role1.setDescription(role.getDescription());
        role1.setPermission(role.getPermission());
        role1.setParentId(role.getParentId());

        Boolean bool = save(role1);

        if (bool) return new ResponseResult(200, "成功", null);

        return new ResponseResult(500, "失败", null);
    }

    @Override
    public ResponseResult updateRoleById(Role role) {

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getId, role.getId());

        Role role1 = new Role();
        role1.setName(role.getName());
        role1.setStatus(role.getStatus());
        role1.setDescription(role.getDescription());
        role1.setPermission(role.getPermission());

        int del = roleMapper.update(role1, lambdaQueryWrapper);

        if (del == 1) return new ResponseResult(200, "成功", null);

        return new ResponseResult(400, "失败", null);

    }

    private List<Map<String, Object>> findChildrenIdAndNameById(int id) {

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getParentId, id);
        lambdaQueryWrapper.eq(Role::getStatus, 1);

        lambdaQueryWrapper.select(Role::getId, Role::getName);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> list2 = findChildrenIdAndNameById((Integer) list.get(i).get("id"));
            list.get(i).put("children", list2);
        }

        return list;
    }

    // 递归删除子节点数据id，逐渐删除
    private void delByParentId(int id) {

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getParentId, id);

        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            delByParentId((Integer) list.get(i).get("id"));
        }

        removeById(id);
    }

    private List<Map<String, Object>> findChildrenById(int id) {

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getParentId, id);
//        lambdaQueryWrapper.eq(Role::getStatus, 1);

        lambdaQueryWrapper.select(Role::getName, Role::getStatus, Role::getId, Role::getPermission, Role::getDescription, Role::getParentId);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> list2 = findChildrenById((Integer) list.get(i).get("id"));
            if (list2.size() > 0){
                list.get(i).put("children", list2);
            }
        }

        return list;
    }

}

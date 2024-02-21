package com.example.springboot8.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.entity.UserRole;
import com.example.springboot8.mapper.UserRoleMapper;
import com.example.springboot8.service.UserRoleService;
import com.example.springboot8.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserServiceImp userServiceImp;

    @Transactional
    @Override
    public ResponseResult add(int userId, List roleIds) {
        // 对比上传的和现有的角色，进行操作更新
        // getUserIdByToken.getUserIdByToken()
        LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserRole::getUserId, userId);
        lambdaQueryWrapper.select(UserRole::getRoleId, UserRole::getId);

        List<UserRole> userRoles = userRoleMapper.selectList(lambdaQueryWrapper);

        // 已拥有roleId列表
        Map<Integer, UserRole> userRoleMap = new HashMap<>();        // String => roleId

        Map<Integer, Integer> delRoleId = new HashMap<>();           // 需要删除的账号

        for (int i = 0; i < userRoles.size(); i++) {
            userRoleMap.put(userRoles.get(i).getRoleId(), userRoles.get(i));
        }

        // 对比数组不同
        // 1. 检查数据库没有的就新增
        for (int i = 0; i < roleIds.size(); i++) {
            // 不存在则说明是新增的角色
            if (userRoleMap.get(roleIds.get(i)) == null) {
                UserRole userRole = new UserRole();
                userRole.setRoleId((int) roleIds.get(i));
                userRole.setUserId(userId);
                userRoleMapper.insert(userRole);
            }
        }

        // 2.上传没有的就删除数据库
        userRoleMap.forEach((key, value) -> {
            Boolean bool = CommonUtil.inArray(roleIds, key);

            if (!bool) {
                userRoleMapper.deleteById(value.getId());
            }
        });


         // 踢掉当前修改过权限的用户在线状态
        userServiceImp.logoutByUserId(userId);


        return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);
    }
}

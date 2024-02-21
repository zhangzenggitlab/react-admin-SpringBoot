package com.example.springboot8.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot8.common.ResponsePage;
import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.controller.LoginUser;
import com.example.springboot8.entity.Department;
import com.example.springboot8.entity.User;
import com.example.springboot8.entity.UserRole;
import com.example.springboot8.mapper.DepartmentMapper;
import com.example.springboot8.mapper.RoleMapper;
import com.example.springboot8.mapper.UserMapper;
import com.example.springboot8.mapper.UserRoleMapper;
import com.example.springboot8.service.UserService;
import com.example.springboot8.utils.CommonUtil;
import com.example.springboot8.utils.GetUserIdByToken;
import com.example.springboot8.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GetUserIdByToken getUserIdByToken;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Override
    public ResponseResult login(User user) {

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            String jtw = jwtUtils.createJWT(loginUser.getUser().getId());

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("token", jtw);

            // 存入hash redis
            BoundHashOperations ops = redisTemplate.boundHashOps("token" + loginUser.getUser().getId());
            ops.put(loginUser.getUser().getId(), loginUser);

            return new ResponseResult(200, "登录成功", hashMap);
        } catch (Exception e) {
            System.out.println("登录失败异常:" + e);
            return new ResponseResult(400, "账号与密码不匹配", null);
        }

    }

    @Override
    public ResponseResult logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        BoundHashOperations ops = redisTemplate.boundHashOps("token" + jwtUtils.getJWT((String) authentication.getPrincipal()));
        int redisId = Integer.parseInt(jwtUtils.getJWT((String) authentication.getPrincipal()));
        ops.delete(redisId);

        redisTemplate.delete("token" + redisId);
        return new ResponseResult(200, "退出成功", null);
    }

    @Override
    public ResponseResult userList(Map<String, Object> params) {

        int pageSize = (Integer) params.get("pageSize");
        IPage page = new Page((Integer) params.get("page"), pageSize);

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (Objects.nonNull(params.get("name")) && params.get("name") != "") {
            lambdaQueryWrapper.eq(User::getName, params.get("name"));
        }

        if (Objects.nonNull(params.get("username")) && params.get("username") != "") {
            lambdaQueryWrapper.eq(User::getUsername, params.get("username"));
        }

        if (Objects.nonNull(params.get("phone")) && params.get("phone") != "") {
            lambdaQueryWrapper.eq(User::getPhone, params.get("phone"));
        }

        if (Objects.nonNull(params.get("mail")) && params.get("mail") != "") {
            lambdaQueryWrapper.eq(User::getMail, params.get("mail"));
        }

        if (Objects.nonNull(params.get("status")) && (int) params.get("status") != 0) {
            lambdaQueryWrapper.eq(User::getStatus, params.get("status"));
        }

        if (Objects.nonNull(params.get("departmentId"))) {
            // 所有子部门数据也要查询出来
            List list = new ArrayList<>();
            List list2 = departmentService.getChildrenIdsByParentId((int) params.get("departmentId"), list);
            list2.add(params.get("departmentId"));

            if (list2.size() > 0) {
                lambdaQueryWrapper.in(User::getDepartmentId, list2);
            }
        }

        // 不要查询自己
        lambdaQueryWrapper.ne(User::getId,getUserIdByToken.getUserIdByToken());

        lambdaQueryWrapper.select(User::getId, User::getStatus, User::getName, User::getUsername, User::getMail, User::getCreateTime, User::getAvatar, User::getPhone, User::getDepartmentId);
        List<Map<String, Object>> users = userMapper.selectMapsPage(page, lambdaQueryWrapper).getRecords();

        // 查询用户权限

        for (int i = 0; i < users.size(); i++) {
            LambdaQueryWrapper<UserRole> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(UserRole::getUserId, users.get(i).get("id"));
            lambdaQueryWrapper1.select(UserRole::getRoleId);

            List roles = userRoleMapper.selectObjs(lambdaQueryWrapper1);
            users.get(i).put("roles", roles);
        }

        return new ResponseResult(200, "查询成功", new ResponsePage((int) page.getCurrent(), (int) page.getPages(), (int) page.getTotal(), users));
    }

    @Override
    public ResponseResult getUserInfo() {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, getUserIdByToken.getUserIdByToken());

        lambdaQueryWrapper.select(User::getId, User::getName, User::getUsername, User::getMail, User::getCreateTime, User::getAvatar, User::getPhone, User::getDepartmentId);

        Map<String, Object> user = getMap(lambdaQueryWrapper);

        if (Objects.nonNull(user)) {
            // 查询部门名称
            Department department = departmentMapper.selectById((int) user.get("departmentId"));

            if (Objects.nonNull(department) && department.getName() != null) {
                user.put("department", department.getName());
            }

            // 查询角色id
            LambdaQueryWrapper<UserRole> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(UserRole::getUserId, getUserIdByToken.getUserIdByToken());
            lambdaQueryWrapper2.select(UserRole::getRoleId);
            List roleIds = userRoleMapper.selectObjs(lambdaQueryWrapper2);


            if (Objects.nonNull(department) && department.getName() != null) {
                user.put("roles", roleIds);
            }
        }

        return new ResponseResult(200, "查询成功", user);
    }

    @Override
    public ResponseResult editUser(User user) {

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId,getUserIdByToken.getUserIdByToken());
        User editUser = new User();
        editUser.setName(user.getName());
        editUser.setMail(user.getMail());
        editUser.setAvatar(user.getAvatar());
        editUser.setPhone(user.getPhone());

        int success = userMapper.update(user,userLambdaQueryWrapper);

        if (success == 1) {
            return new ResponseResult(200, "更新成功", null);
        }
        return new ResponseResult(400, "更新失败", null);
    }

    @Override
    public ResponseResult add(User user) {

        User user1 = new User();
        user1.setMail(user.getMail());
        user1.setName(user.getName());
        user1.setPhone(user.getPhone());
        user1.setUsername(user.getUsername());
        user1.setStatus(user.getStatus());
        user1.setDepartmentId(user.getDepartmentId());

        // 检查账号是否被占用
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());

        User user2 = userMapper.selectOne(lambdaQueryWrapper);

        if (!Objects.isNull(user2)) {
            return new ResponseResult(HttpCode.CODE_SERVICEERROR, "账号已被占用", null);
        }

//        // 查询默认角色
//        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        roleLambdaQueryWrapper.eq(Role::getStatus, 1);
//
//        Role role1 = roleMapper.selectOne(roleLambdaQueryWrapper);
//
//        if (Objects.isNull(role1)) {
//            return new ResponseResult(400, "请先设定一个默认角色", null);
//        }
//
//        // 设置默认角色
//        if (Objects.nonNull(role1)) {
//            role1.getId();
//        }

        // 密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user1.setPassword(passwordEncoder.encode("123456"));     // 默认密码

        Boolean bool = save(user1);
        if (bool) {
            for (int i = 0; i < user.getRoles().size(); i++) {
                UserRole role = new UserRole();
                role.setRoleId((int) user.getRoles().get(i));
                role.setUserId(user1.getId());

                userRoleMapper.insert(role);
            }


            return new ResponseResult(200, "成功", null);
        }

        return new ResponseResult(400, "更新失败", null);
    }

    @Override
    public ResponseResult updatePassword(String oldPassword, String newPassword) {

        if (Objects.isNull(oldPassword) || oldPassword.length() == 0) {
            return new ResponseResult(400, "请输入旧密码", null);
        }

        if (Objects.isNull(newPassword) || newPassword.length() == 0) {
            return new ResponseResult(400, "请输入新密码", null);
        }

        if (oldPassword.equals(newPassword)) {
            return new ResponseResult(400, "新旧密码不能一致", null);
        }

        // 验证密码是否正确
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        lambdaQueryWrapper.eq(User::getPassword, passwordEncoder.encode(newPassword));

        lambdaQueryWrapper.eq(User::getId, getUserIdByToken.getUserIdByToken());
        User user = userMapper.selectById(getUserIdByToken.getUserIdByToken());

        if (Objects.isNull(user)) {
            return new ResponseResult(HttpCode.CODE_SERVICEERROR, HttpCode.constants.get(HttpCode.CODE_SERVICEERROR), null);
        }

        // 正确则修改密码
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            User updatePwd = new User();
            updatePwd.setPassword(passwordEncoder.encode(newPassword));
            updatePwd.setId(getUserIdByToken.getUserIdByToken());
            Boolean bool = updateById(updatePwd);

            if (bool) {
                // 删除本地redis重新登录
                UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

                BoundHashOperations ops = redisTemplate.boundHashOps("token" + jwtUtils.getJWT((String) authentication.getPrincipal()));
                int redisId = Integer.parseInt(jwtUtils.getJWT((String) authentication.getPrincipal()));
                ops.delete(redisId);

                redisTemplate.delete("token" + redisId);

                return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);
            }

            return new ResponseResult(HttpCode.CODE_SERVICEERROR, HttpCode.constants.get(HttpCode.CODE_SERVICEERROR), null);
        }

        return new ResponseResult(HttpCode.CODE_SERVICEERROR, HttpCode.constants.get(HttpCode.CODE_SERVICEERROR), null);
    }

    @Override
    public ResponseResult deleteById(int id) {
        Boolean bool = removeById(id);

        if (bool) {
            return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);
        }
        return new ResponseResult(HttpCode.CODE_SERVICEERROR, HttpCode.constants.get(HttpCode.CODE_SERVICEERROR), null);
    }

    /**
     * 更新id用户信息
     *
     * @param user
     * @return
     */
    @Transactional
    @Override
    public ResponseResult editUserById(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("name", user.getName());
        updateWrapper.set("mail", user.getMail());
        updateWrapper.set("status", user.getStatus());
        updateWrapper.set("username", user.getUsername());
        updateWrapper.set("phone", user.getPhone());
        updateWrapper.set("department_id", user.getDepartmentId());

        // 部门
        updateWrapper.eq("id", user.getId());
        List roleIds = user.getRoles();
        editUserRole(user.getId(), roleIds);

        boolean success = update(updateWrapper);

        logoutByUserId(user.getId());

        if (success) {
            return new ResponseResult(200, "更新成功", null);
        }
        return new ResponseResult(400, "更新失败", null);
    }


    private void editUserRole(int userId, List roleIds){
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
    }


    /**
     * 踢掉某个用户在线状态(重新登录)
     * @param userId
     */
    protected void logoutByUserId(int userId){
        BoundHashOperations ops = redisTemplate.boundHashOps("token" + userId);
        ops.delete(userId);
        redisTemplate.delete("token" + userId);

    }
}

package com.example.springboot8.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot8.common.ResponsePage;
import com.example.springboot8.common.ResponseResult;
import com.example.springboot8.constants.HttpCode;
import com.example.springboot8.entity.Department;
import com.example.springboot8.mapper.DepartmentMapper;
import com.example.springboot8.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ResponseResult list(int page, int pageSize, String name,int status) {
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (status != 0) {
            lambdaQueryWrapper.eq(Department::getStatus, status);
        }

        // 如果出现name搜索，则查询全部，不用区分tree
        if (!name.equals("")) {
            lambdaQueryWrapper.eq(Department::getName, name);
        }else{
            lambdaQueryWrapper.eq(Department::getParentId, 0);
        }

        IPage ipage = new Page(page, pageSize);

        lambdaQueryWrapper.select(Department::getName,Department::getStatus, Department::getCreateTime,Department::getEmail, Department::getPrincipal, Department::getId, Department::getPhone, Department::getParentId);
        List<Map<String, Object>> list = departmentMapper.selectMapsPage(ipage, lambdaQueryWrapper).getRecords();

        // 不要查子children
        if (name.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                List<Map<String, Object>> list2 = findChildrenById((Integer) list.get(i).get("id"),name,status);
                if(list2.size() > 0){
                    list.get(i).put("children", list2);
                }
            }
        }


        return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), new ResponsePage((int) ipage.getCurrent(), (int) ipage.getPages(), (int) ipage.getTotal(), list));
    }

    @Override
    public ResponseResult all() {

        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getStatus, 1);
        lambdaQueryWrapper.eq(Department::getParentId, 0);

        lambdaQueryWrapper.select(Department::getId, Department::getName, Department::getParentId);

        List<Map<String, Object>> menu = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < menu.size(); i++) {
            menu.get(i).put("children", findChildrenIdAndNameById((int) menu.get(i).get("id")));
        }

        return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), menu);
    }

    @Override
    public ResponseResult delById(int id) {
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getId, id);

        int del = departmentMapper.deleteById(id);

        delByParentId(id);

        if (del == 1)
            return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);

        return new ResponseResult(HttpCode.CODE_FAIL, HttpCode.constants.get(HttpCode.CODE_FAIL), null);
    }

    @Override
    public ResponseResult updateDepartmentById(Department department) {

        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getId, department.getId());

        Department department1 = new Department();
        department1.setName(department.getName());
        department1.setPhone(department.getPhone());
        department1.setPrincipal(department.getPrincipal());
        department1.setParentId(department.getParentId());
        department1.setStatus(department.getStatus());


        int bool = departmentMapper.update(department, lambdaQueryWrapper);

        if (bool == 1)
            return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);

        return new ResponseResult(HttpCode.CODE_SERVICEERROR, HttpCode.constants.get(HttpCode.CODE_SERVICEERROR), null);
    }

    @Override
    public ResponseResult add(Department department) {

        Boolean bool = save(department);

        if (bool) return new ResponseResult(HttpCode.CODE_SUCCEES, HttpCode.constants.get(HttpCode.CODE_SUCCEES), null);

        return new ResponseResult(HttpCode.CODE_SERVICEERROR, HttpCode.constants.get(HttpCode.CODE_SERVICEERROR), null);
    }

    private List<Map<String, Object>> findChildrenIdAndNameById(int id) {

        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getParentId, id);
        lambdaQueryWrapper.eq(Department::getStatus, 1);

        lambdaQueryWrapper.select(Department::getId, Department::getName, Department::getParentId);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> list2 = findChildrenIdAndNameById((Integer) list.get(i).get("id"));
            list.get(i).put("children", list2);
        }

        return list;
    }

    private List<Map<String, Object>> findChildrenById(int id,String name, int status) {


        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getParentId, id);

        if (!name.equals("")) {
            lambdaQueryWrapper.eq(Department::getName, name);
        }

        if (status != 0) {
            lambdaQueryWrapper.eq(Department::getStatus, status);
        }


        lambdaQueryWrapper.select(Department::getName,Department::getStatus, Department::getCreateTime, Department::getEmail, Department::getPrincipal, Department::getId, Department::getPhone, Department::getParentId);
        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> list2 = findChildrenById((Integer) list.get(i).get("id"),name,status);
            if(list2.size() > 0){
                list.get(i).put("children", list2);
            }
        }

        return list;
    }


    // 递归删除子节点数据id，逐渐删除
    private void delByParentId(int id) {

        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getParentId, id);

        List<Map<String, Object>> list = listMaps(lambdaQueryWrapper);

        for (int i = 0; i < list.size(); i++) {
            delByParentId((Integer) list.get(i).get("id"));
        }

        removeById(id);
    }


    /**
     * 根据父id查询子数据
     *
     * @param parentId
     * @return
     * @list 所有子节点
     */
    protected List getChildrenIdsByParentId(int parentId, List list) {
        LambdaQueryWrapper<Department> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Department::getParentId,parentId);

        List list1 = departmentMapper.selectObjs(lambdaQueryWrapper);

        for (int i = 0; i < list1.size(); i++){
            list.add(list1.get(i));
            getChildrenIdsByParentId((int)list1.get(i),list);
        }

        System.out.println(list1);
        return list;
    }


}

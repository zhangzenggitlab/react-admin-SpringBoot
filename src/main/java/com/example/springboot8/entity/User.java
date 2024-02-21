package com.example.springboot8.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;                                      // 名称

    @NotBlank(message = "请填写账号")
    private String username;                                  // 登录账号

   // @TableField(select = false)
    private String password;                                  // 登录密码

    private String mail;

    private Integer status;                                   // 1正常 2 禁用

    private Timestamp createTime;                             // 创建时间

    private String phone;

    private String avatar;

    private DateTime lastLoginTime;

    private String lastLoginIp;

    @TableLogic(value = "1",delval = "2")                     // 1 未删除 2 删除
    private Integer isDelete;

    private Integer departmentId;                             // 部门id

    @TableField(exist = false)
    private List roles;                                        // 权限数组
}

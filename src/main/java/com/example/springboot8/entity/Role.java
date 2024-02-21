package com.example.springboot8.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
public class Role {

    @TableId(type = IdType.AUTO)
    private Integer Id;

    @NotBlank(message = "请填写角色名称")
    private String name;                            // 角色名称

    @NotBlank(message = "请填写权限标识")
    private String permission;                      // 角色标识

    private String description;

    private int status;

    @TableLogic(value = "1", delval = "2")           // 1 未删除 2 删除
    private Integer isDelete;

    private Timestamp createTime;                   // 创建时间

    private Integer parentId;
}

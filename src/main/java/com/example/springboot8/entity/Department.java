package com.example.springboot8.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Department {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer status;

    private Integer parentId;

    private String phone;

    private String email;

    private String principal;

    @TableLogic(value = "1", delval = "2")           // 1 未删除 2 删除
    private Integer isDelete;

    private Timestamp createTime;                    // 创建时间
}

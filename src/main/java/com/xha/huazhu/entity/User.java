package com.xha.huazhu.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
public class User extends BaseEntity {

    private Long qq;
    private String userName;
    //1管理员 2普通用户
    private Integer userType;

    public static final int ADMIN = 1;
    public static final int USER = 2;
}

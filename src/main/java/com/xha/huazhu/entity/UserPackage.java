package com.xha.huazhu.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
public class UserPackage extends BaseEntity {

    private Integer userId;
    private Integer foodId;
    private Integer count;
}

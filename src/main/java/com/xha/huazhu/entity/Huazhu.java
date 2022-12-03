package com.xha.huazhu.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
public class Huazhu extends BaseEntity {


    //花猪状态 1 正常 2 饥饿
    private Integer huazhuStatus;

    //花猪上次饥饿时间
    private String lastTime;

    //花猪每日饥饿次数
    private Integer times;

    //花猪饥饿起始时间
    private String startTime;

    //花猪饥饿结束时间
    private String endTime;

    public static final int STATUS_NORMAL = 1;

    public static final int STATUS_HUNGER = 2;

}

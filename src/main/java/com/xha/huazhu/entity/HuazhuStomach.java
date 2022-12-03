package com.xha.huazhu.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
public class HuazhuStomach extends BaseEntity {


    private Integer huazhuId;

    //花猪需要吃的食物
    private Integer foodId;

    //花猪需要食物的个数
    private Integer foodCount;
    
}

package com.xha.huazhu.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
public class Record extends BaseEntity {


    private Integer huazhuId;

    //投喂的食物
    private Integer foodId;

    //投喂人
    private Integer userId;

    //投喂数量
    private Integer foodCount;
}

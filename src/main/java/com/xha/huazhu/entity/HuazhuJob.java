package com.xha.huazhu.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
public class HuazhuJob extends BaseEntity {

    private String eventTime;

    //事件状态 1未执行 2已执行
    private Integer eventStatus;

}

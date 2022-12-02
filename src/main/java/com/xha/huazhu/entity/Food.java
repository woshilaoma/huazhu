package com.xha.huazhu.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString(callSuper = true)
public class Food extends BaseEntity{

    private String foodName;

}

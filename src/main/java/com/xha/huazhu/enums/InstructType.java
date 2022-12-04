package com.xha.huazhu.enums;

import java.util.ArrayList;
import java.util.List;

public enum InstructType {

    sign("签到", "signInstructService"),
    error("未知指令", "errorInstructService"),
    help("帮助", "helpInstructService"),
    addFood("添加食物", "addFoodInstructService"),
    removeFood("删除食物", "removeFoodInstructService"),
    foodList("查看背包", "foodListInstructService"),
    feedList("投喂", "feedInstructService"),
    eatFoodList("吃什么", "eatFoodListInstructService"),
    rank("排行榜", "rankInstructService");

    private String instruct;
    private String serviceBeanName;

    InstructType(String instruct, String serviceBeanName) {
        this.instruct = instruct;
        this.serviceBeanName = serviceBeanName;
    }

    public String getInstruct() {
        return instruct;
    }

    public String getServiceBeanName() {
        return serviceBeanName;
    }

    public static InstructType valueOfByInstruct(String instruct) {
        for (InstructType instructType : values()) {
            if (instructType.getInstruct().equals(instruct)) {
                return instructType;
            }
        }
        return error;
    }


    public static List<InstructType> valuesList() {
        List<InstructType> result = new ArrayList<>();
        for (InstructType instructType : values()) {
            if (instructType == error) {
                continue;
            }
            if (instructType == addFood) {
                continue;
            }
            if (instructType == removeFood) {
                continue;
            }
            result.add(instructType);

        }

        return result;
    }
}

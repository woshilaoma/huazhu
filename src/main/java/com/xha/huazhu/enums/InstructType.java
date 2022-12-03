package com.xha.huazhu.enums;

public enum InstructType {

    sign("签到", "signInstructService"),
    error("未知指令", "errorInstructService");


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
}

package com.cad.data.enums;

public enum LogTypeEnum {

    ALARMEVENT("alarmevent", "车辆报警信息");

    private String code;
    private String name;

    LogTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

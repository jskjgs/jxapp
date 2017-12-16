package com.jishi.reservation.service.enumPackage;

/**
 * Created by zbs on 2017/8/10.
 */

public enum EnableEnum {

    EFFECTIVE(0, "正常"),
    INVALID(1, "禁用"),
    DELETE(99, "删除");

    private int code;
    private String desc;

    EnableEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

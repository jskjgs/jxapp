package com.jishi.reservation.service.enumPackage;

/**
 * Created by zbs on 2017/8/11.
 */
public enum SuccessEnum {

    SUCCESS(0, "成功"),
    FAILED(1, "失败");

    private int code;
    private String status;

    SuccessEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String desc) {
        this.status = status;
    }
}

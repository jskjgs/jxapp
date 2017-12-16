package com.jishi.reservation.service.enumPackage;

/**
 * Created by sloan on 2017/9/3.
 */
public enum DisplayEnum {

    HIDE(1,"隐藏"),
    SHOW(0,"显示");

    private Integer code;
    private String status;

    DisplayEnum(Integer code,String status){
        this.code = code;
        this.status = status;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

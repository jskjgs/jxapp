package com.jishi.reservation.service.enumPackage;

/**
 * Created by sloan on 2017/9/12.
 */
public enum PayEnum {

    ALI(1,"支付宝"),
    WEIXIN(2,"微信");

    private int code;
    private String type;

    PayEnum(int code, String type){
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String timeInteval) {
        this.type = type;
    }
}

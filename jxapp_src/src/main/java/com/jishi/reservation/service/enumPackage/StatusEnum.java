package com.jishi.reservation.service.enumPackage;

/**
 * Created by zbs on 2017/8/11.
 */
public enum  StatusEnum {

    REGISTER_STATUS_NO_PAYMENT(1, "订单（预约）未支付"),
    REGISTER_STATUS_PAYMENT(0, "订单（预约）已支付"),
    REGISTER_STATUS_CANCEL(2, "订单（预约)取消");

    private int code;
    private String desc;

    StatusEnum(int code, String desc) {
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

package com.jishi.reservation.service.enumPackage;

/**
 * Created by zbs on 2017/8/11.
 */
public enum OrderStatusEnum {

    PAYED(0, "已支付"),
    WAIT_PAYED(1, "等待支付"),
    CANCELED(2, "订单取消");

    private int code;
    private String desc;

    OrderStatusEnum(int code, String desc) {
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

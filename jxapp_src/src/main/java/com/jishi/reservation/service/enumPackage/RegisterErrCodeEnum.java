package com.jishi.reservation.service.enumPackage;

/**
 * Created by zbs on 2017/8/11.
 */
public enum RegisterErrCodeEnum {

    RIGHT(0,"订单正常"),
    DOCTOR_FULL(1, "该医生挂号号源已满，请选择其他医生。"),
    ORDER_STATE_NOT_MATCH(2, "该订单不是待支付状态或不是挂号订单，请检查。"),
    PATIENT_NOT_MATCH(3, "病人信息和挂号类别不符，不能挂号。"),
    LIMIT_FOR_PATIENT(4,"该病人与该号源相冲突，不能挂号。"),
    ORDER_NUMBER_NOT_MATCH_ACCOUNT(5,"该笔订单与操作用户不符，不能继续"),
    ORDER_NOT_EXIST(6,"订单信息不存在。");


    private int code;
    private String desc;

    RegisterErrCodeEnum(int code, String desc) {
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

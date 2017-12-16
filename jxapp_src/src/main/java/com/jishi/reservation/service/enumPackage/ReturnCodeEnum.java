package com.jishi.reservation.service.enumPackage;

/**
 * Created by zbs on 2017/8/10.
 */

public enum ReturnCodeEnum {

    SUCCESS(200, "成功"),
    NOT_LOGIN(401, "登陆信息已过期，请重新登陆！"),
    FAILED(406, "失败"),
    ERR(500, "系统错误"),

    /** 门诊相关 1100-1199 **/
    OUTPATIENT_ERR_CONFIRM_ORDER_NULL(1100, "缴费记录不存在"),
    OUTPATIENT_ERR_CONFIRM_PAYMENT_NULL(1101, "订单不存在"),
    OUTPATIENT_ERR_CONFIRM_ORDER_NOT_MATCH(1102, "订单不匹配"),
    OUTPATIENT_ERR_CONFIRM_NO_PAY(1103, "订单未支付"),
    OUTPATIENT_ERR_CONFIRM_ORDER_TYPE_NOT_MATCH(1104, "订单类型不匹配"),
    OUTPATIENT_ERR_CONFIRM_FAILED(1105, "支付确认失败"),
    OUTPATIENT_ERR_GENERATE_ORDER_ATTR_NULL(1106, "订单相关信息缺失"),

    /** 支订单相关 1200-1249 **/
    ORDER_ERR_NOT_FOUND(1200, "订单不存在"),
    ORDER_ERR_AMOUNT_NOT_MACH(1201, "订单金额不匹配"),
    ORDER_ERR_PAYED(1202, "订单已支付"),
    ORDER_ERR_CANCLED(1203, "订单已取消"),
    ORDER_ERR_WAIT_PAYED(1204, "订单待支付"),

    /** 微信支付相关 1250-1299 **/
    WEICHART_PAY_ERR_SIGN_CHECK_FAILED(1250, "微信支付签名校验失败"),
    WEICHART_PAY_ERR_UNIFIEDORDER_FAILED(1251, "微信支付生成订单失败"),
    WEICHART_PAY_ERR_NOTIFY_RETURN_ERR(1252, "微信支付向通知接口返回失败"),

    /** IM相关 5130-5139 **/
    IM_ERR_GET_ACCOUNT_FAILED(5130, "生成IM账号失败"),
    IM_ERR_CREATE_ACCOUNT_FAILED(5131, "生成IM账号失败"),

    /** 账号相关  **/

    UNKNOWN_ERR(1000, "未知错误"),
    VALID_CODE_WRONG(5109,"验证码错误");
    private int code;
    private String desc;

    ReturnCodeEnum(int code, String desc) {
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

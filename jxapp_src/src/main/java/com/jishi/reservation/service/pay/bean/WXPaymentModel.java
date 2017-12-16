package com.jishi.reservation.service.pay.bean;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付回调model
 * Created by zbs on 16/8/30.
 */
@Data
public class WXPaymentModel {
    //返回状态码
    public String return_code;
    //返回信息
    public String return_msg;
    //应用ID
    public String appid;
    //商户号
    public String mch_id;
    //设备号
    public String device_info;
    //随机字符串
    public String nonce_str;
    //签名
    public String sign;
    //业务结果
    public String result_code;
    //错误代码
    public String err_code;
    //错误代码描述
    public String err_code_des;
    //用户标识
    public String openid;
    //是否关注公众账号
    public String is_subscribe;
    //交易类型
    public String trade_type;
    //付款银行
    public String bank_type;
    //总金额
    public Integer total_fee;
    //货币种类
    public String fee_type;
    //现金支付金额
    public Integer cash_fee;
    //现金支付货币类型
    public String cash_fee_type;
    //代金券或立减优惠金额
    public Integer coupon_fee;
    //代金券或立减优惠使用数量
    public Integer coupon_count;
    //代金券或立减优惠ID
    // public String coupon_id_$n;
    //单个代金券或立减优惠支付金额
    // public Integer coupon_fee_$n
    //微信支付订单号
    public String transaction_id;
    //商户订单号
    public String out_trade_no;
    //商家数据包
    public String attach;
    //支付完成时间
    public String time_end;

    public String getOut_trade_no(){
        if(this.out_trade_no != null && !this.out_trade_no.equals(""))
             return this.out_trade_no.split("hotr")[0];
        return null;
    }


    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}

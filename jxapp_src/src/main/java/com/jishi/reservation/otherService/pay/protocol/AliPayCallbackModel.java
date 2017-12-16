package com.jishi.reservation.otherService.pay.protocol;

import lombok.Data;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sloan on 2017/10/19.
 */

@Data
public class AliPayCallbackModel {

    private String gmt_create; //订单创建时间
    private String buyer_email; //支付者的邮箱账号
    private String notify_time;
    private String seller_email; //商家邮箱账号
    private String quantity;   //数量
    private String  subject; //商品名称
    private String use_coupon; //是否使用优惠券
    private String sign; //签名
    private String discount;    //优惠金额
    private String body;    //商品描述
    private String buyer_id;    //购买者id
    private String notify_id;
    private String notify_type;     //trade_status_sync
    private String payment_type;
    private String out_trade_no;    //我们自己系统内的订单号
    private String price;   //订单金额
    private String trade_status;    //订单状态 WAIT_BUYER_PAY TRADE_SUCCES
    private String total_fee;
    private String trade_no;
    private String sign_type;   //加密方式
    private String is_total_fee_adjust;
    private String total_amount;

    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String, String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String obj;
            try {
                obj = (String) field.get(this);
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

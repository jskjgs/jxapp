package com.jishi.reservation.service.pay.bean;

import com.doraemon.base.util.MD5Encryption;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 支付宝回调对象
 * Created by zbs on 16/8/17.
 */
@Data
public class TBPaymentModel {

    //通知时间
    public String notify_time;
    //通知类型
    public String notify_type;
    //通知校验ID
    public String notify_id;
    //签名方式
    public String sign_type;
    //签名
    @NotNull(message = "签名为空")
    public String sign;
    //订单号
    @NotNull(message = "订单号为空")
    public String out_trade_no;
    //商品名称
    public String subject;
    //支付类型
    public String payment_type;
    //支付宝交易号
    @NotNull(message = "支付宝交易号为空")
    public String trade_no;
    //交易状态
    public String trade_status;
    //卖家支付宝ID
    @NotNull(message = "卖家支付宝ID为空")
    public String seller_id;
    //卖家支付宝账号
    @NotNull(message = "卖家支付宝账号为空")
    public String seller_email;
    //买家ID
    @NotNull(message = "买家ID为空")
    public String buyer_id;
    //买家支付宝账号
    @NotNull(message = "买家支付宝账号为空")
    public String buyer_email;
    //交易金额
    @NotNull(message = "交易金额为空")
    public BigDecimal total_fee;
    //购买数量
    public Integer quantity;
    //商品单价
    public BigDecimal price;
    //商品描述
    public String body;
    //交易创建时间
    public String gmt_create;
    //交易付款时间
    public String gmt_payment;
    //是否调整总价
    public String is_total_fee_adjust;
    //是否使用红包的买家
    public String use_coupon;
    //折扣
    public String discount;
    //退款状态
    public String refund_status;
    //退款时间
    public Date gmt_refund;
    //实际支付金额
    public BigDecimal total_amount;
    //开发者id
    public String app_id;


    public String getMD5() throws NoSuchAlgorithmException {
        StringBuffer sb = new StringBuffer();
        sb.append("body="+getBody()+"&");
        sb.append("buyer_email="+getBuyer_email()+"&");
        sb.append("buyer_id="+getBuyer_id()+"&");
        sb.append("extra_common_param="+""+"&");
        sb.append("gmt_create="+getGmt_create()+"&");
        sb.append("gmt_payment="+getGmt_payment()+"&");
        sb.append("is_total_fee_adjust="+getIs_total_fee_adjust()+"&");
        sb.append("notify_id="+getNotify_id()+"&");
        sb.append("notify_time="+getNotify_time()+"&");
        sb.append("notify_type="+getNotify_type()+"&");
        sb.append("out_trade_no="+getOut_trade_no()+"&");
        sb.append("price="+getPrice()+"&");
        sb.append("quantity="+getQuantity()+"&");
        sb.append("seller_email="+getSeller_email()+"&");
        sb.append("seller_id="+getSeller_id()+"&");
        sb.append("subject="+getSubject()+"&");
        sb.append("total_fee="+getTotal_fee()+"&");
        sb.append("trade_no="+getTrade_no()+"&");
        sb.append("trade_status="+getTrade_status()+"&");
        sb.append("use_coupon="+getUse_coupon()+"&");
        sb.append("key="+"MIICXAIBAAKBgQCqNQvizx6Cgji9eRFTeRrbsPwwrSsm3QwpgzLrkIlKheU6sEuVncP" +
                "18bi6RW6283cysS6qxyQscp3yi/q3FLII/2+JPWVCRkSEP9M0zrxpcyXGDSPXTGX5aoSkMJ5r04L" +
                "qkrCtzOpdVeezaLpGLImTbTqsPaMPIHWygd4HUxGUWwIDAQABAoGAfqVR3sW2Me2rQnBfD/lAns8mFyI" +
                "CxbwcXJcewGCfhy/xzKV42C63rLzbct5O7xphSvrDepcKt3fhRqQZPTjM7uZJ+it/yY0scQ8WvmtRBG" +
                "MXtbDebHPN+cRc8Kv17IKb6bavIJO/8ehkJG0pyyQDcZ8swb5gLRlnNulDqds6jqECQQDVbefbmz0c7Sqp" +
                "tKrELD6RAZHVImj8ShbSUTHISZqbTiPQ9ie1RFpJ6mGXBvUXP/+gQtXelTmseulDa+VW9hP1AkEAzCgj7PX8nByf75" +
                "OUjQ30I102YO6mmY0P4Jd1zf7Nb9n+36s9hn/+rWC5nF9BeKmeXtYbpuHCa2kM/Ah9rDQlDwJBALhU1f1LVDrZcxFjImGP" +
                "vPHMI0GrGHVdH9zBl3deapNbp39gOedKg5h3P5YKwj80XPjzAHfe9hLLjQ9X7pluqskCQC/0Vcv5U+bPinnZ7pDbcDdGU9mc" +
                "9SEoU0xsB03lxhe52vZq4L7RHd9X0wF6FoTjQsIABOrEKMjWGiixTeO/M90CQGB7XUq5QZUkR7JxnqxLfiFwBshxp95WPhCxp" +
                "dJFsDEwRmpHrPwndWcP586VAAtY8JsbZojuTvcNpBDj+Myhsm4=");
        return MD5Encryption.getMD5(sb.toString());
    }
}

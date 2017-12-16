package com.jishi.reservation.otherService.pay.protocol;

import com.doraemon.base.util.RandomUtil;
import com.jishi.reservation.otherService.pay.WXSignature;
import com.jishi.reservation.util.Constant;
import lombok.Data;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 微信统一订单支付请求数据
 * Created by zbs on 2016/11/11.
 */
@XmlRootElement
@Data
public class WXUnifiedOrderPayReqData {
    //应用API
    private String appid = Constant.WECHAT_PAY_APPID;
    //商户号
    private String mch_id = Constant.WECHAT_PAY_MCHID;
    //设备号
    private String device_info = "WEB";
    //随机字符串
    private String nonce_str = RandomUtil.getRandomStringByLength(32).toUpperCase();
    //签名
    private String sign = "";
    //签名类型
    private String sign_type="MD5";
    //商品描述
    private String body= Constant.WECHAT_PAY_APP_NAME;
    //商品详情
    private String detail="";
    //附加数据
    private String attach = "";
    //商户订单号
    private String out_trade_no = "";
    //货币类型
    private String fee_type="CNY";
    //总金额
    private int total_fee = 0;
    //终端IP
    private String spbill_create_ip = "";
    //交易起始时间
    private String time_start = "";
    //交易结束时间
    private String time_expire = "";
    //商品标记
    private String goods_tag = "";
    //通知地址
    private String notify_url="";
    //交易类型
    private String trade_type="APP";
    //指定支付方式
    private String limit_pay = "";
    //openId
    private String scene_info = "";

    private WXUnifiedOrderPayReqData(){}
    /**
     * 构造统一下单对象
     * @param detail 商品详情,可以为空
     * @param totalFee 支付金额
     * @param spbillCreateIp 用户端时间IP
     * @param notifyUrl 回调地址
     * @throws NoSuchAlgorithmException
     */
    public WXUnifiedOrderPayReqData(String outTradeNo, String detail, int totalFee, String spbillCreateIp, String notifyUrl) throws NoSuchAlgorithmException {


        setDetail(detail);
        setOut_trade_no(outTradeNo);

        setTotal_fee(totalFee);
        setSpbill_create_ip(spbillCreateIp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date newDate = new Date();
        String startTime = sdf.format(newDate);
        String endTime = sdf.format(new Date(newDate.getTime() + 30 * 60 * 1000));
        setTime_start(startTime);
        //半小时后过期
        setTime_expire(endTime);
        setNotify_url(notifyUrl);
        setTrade_type("APP");
        //根据API给的签名规则进行签名
        Map<String, Object> map = toMap();
        String sign = WXSignature.getSign(map);
        setSign(sign);  //TODO 密匙设置
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

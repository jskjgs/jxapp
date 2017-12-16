package com.jishi.reservation.otherService.pay;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.doraemon.base.protocol.http.HttpAgent;
import com.doraemon.base.util.RandomUtil;
import com.doraemon.base.util.xml.XMLParser;
import com.jishi.reservation.dao.mapper.OrderInfoMapper;
import com.jishi.reservation.dao.models.OrderInfo;
import com.jishi.reservation.otherService.pay.protocol.WXUnifiedOrderPayReqData;
import com.jishi.reservation.service.enumPackage.OrderStatusEnum;
import com.jishi.reservation.service.enumPackage.PayEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.exception.BussinessException;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.Helpers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by zbs on 2017/9/19.
 */
@Service
@Slf4j
public class WeChatPay {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public boolean notify(String notifyStr) throws ParserConfigurationException, NoSuchAlgorithmException, SAXException, IOException, BussinessException, ParseException {
        log.info("微信支付回调数据：" + notifyStr);
        if (notifyStr == null || notifyStr.isEmpty()) {
            return false;
        }
        Map notifyMap = XMLParser.getMapFromXML(notifyStr);
        //校验签名
        if (!WXSignature.checkIsSignValidFromMap(notifyMap)) {
            throw new BussinessException(ReturnCodeEnum.WEICHART_PAY_ERR_SIGN_CHECK_FAILED);
        }
        if(notifyMap.get("return_code") == null || notifyMap.get("result_code") == null
                      || !"SUCCESS".equals(notifyMap.get("return_code"))
                      || !"SUCCESS".equals(notifyMap.get("result_code"))) {
            log.info("微信支付通知给出失败信息：" + notifyMap.get("return_msg"));
            throw new BussinessException(ReturnCodeEnum.WEICHART_PAY_ERR_NOTIFY_RETURN_ERR);
        }

        String outTradeNo = (String)notifyMap.get("out_trade_no"); //商户订单号
        String totalFee = (String)notifyMap.get("total_fee");//总金额
        String transactionId = (String)notifyMap.get("transaction_id"); //微信支付订单号
        String time_end = (String)notifyMap.get("time_end");
        String openid = (String)notifyMap.get("openid");  //用户标识

        //判断支付金额和商户订单号和自己系统中的信息是否吻合，做判断
        log.info("订单号：" + outTradeNo);
        OrderInfo orderInfo =  orderInfoMapper.queryByOutTradeNo(outTradeNo);
        Helpers.assertNotNull(orderInfo, ReturnCodeEnum.ORDER_ERR_NOT_FOUND);
        log.info("订单信息：\n"+ JSONObject.toJSONString(orderInfo));
        Helpers.assertFalse(orderInfo.getStatus().equals(OrderStatusEnum.PAYED.getCode()), ReturnCodeEnum.ORDER_ERR_PAYED);
        Helpers.assertFalse(orderInfo.getStatus().equals(OrderStatusEnum.CANCELED.getCode()), ReturnCodeEnum.ORDER_ERR_CANCLED);

        BigDecimal amount = new BigDecimal(totalFee);
        amount = amount.divide(new BigDecimal("100"),2, BigDecimal.ROUND_HALF_UP); // 微信支付返回的是分，这里转换成元
        BigDecimal price = orderInfo.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
        Helpers.assertTrue(price.equals(amount), ReturnCodeEnum.ORDER_ERR_AMOUNT_NOT_MACH);

        //改变订单状态和支付时间
        orderInfo.setStatus(OrderStatusEnum.PAYED.getCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        orderInfo.setPayTime(sdf.parse(time_end));
        //orderInfo.setBuyerEmail(buyerEmail);
        //orderInfo.setSellerEmail(sellerEmail);
        orderInfo.setBuyerId(openid);
        orderInfo.setPayType(PayEnum.WEIXIN.getCode());
        orderInfo.setThirdOrderNumber(transactionId);
        orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
        log.info("订单状态修改为已支付。订单id:"+orderInfo.getId());
        return true;
    }

    public Map generateOrder(String notifyUrl, String orderNumber, String subject, BigDecimal price, String spbillCreateIp) throws Exception {
        //拼接回调接口
        //String notifyUrl = notifyUrl;
        WXUnifiedOrderPayReqData wxUnifiedOrderPayReqData = generateProductWithOpenId(orderNumber, subject, notifyUrl, price, spbillCreateIp);
        //把对象转换为xml格式的字符串
        String postDataXML = XMLParser.toXMLString(wxUnifiedOrderPayReqData);

        //调用微信接口
        log.info("微信支付统一下单请求数据：" + postDataXML);
        String response = HttpAgent.create().sendPost(Constant.UNIFIED_ORDER_API, postDataXML);
        log.info("微信支付统一下单返回数据：" + response);
        //对返回结果进行解析
        Map<String,Object> resp = XMLParser.getMapFromXML(response);

        //判断微信标识是否为成功
        if(resp.get("return_code") == null || resp.get("result_code") == null
                    || resp.get("prepay_id") == null
                    || !"SUCCESS".equals(resp.get("return_code"))
                    || !"SUCCESS".equals(resp.get("result_code"))) {
            log.info(String.valueOf("微信错误提示return_msg：" + resp.get("return_msg")));
            throw new BussinessException(ReturnCodeEnum.WEICHART_PAY_ERR_UNIFIEDORDER_FAILED);
        }
        //校验签名
        if (!WXSignature.checkIsSignValidFromResponseString(response)) {
          throw new BussinessException(ReturnCodeEnum.WEICHART_PAY_ERR_SIGN_CHECK_FAILED);
        }
        String nonce_str = (String) resp.get("nonce_str");
        String appid = (String) resp.get("appid");
        String sign = (String) resp.get("sign");
        String trade_type = (String) resp.get("trade_type");
        String mch_id = (String) resp.get("mch_id");
        String prepay_id = (String) resp.get("prepay_id");
        String return_msg = (String) resp.get("return_msg");
        String result_code = (String) resp.get("result_code");
        String return_code = (String) resp.get("return_code");

        Map data =  generateWxSign(prepay_id);
        log.info("微信调起支付参数：" + data);
        return data;

    }

    public WXUnifiedOrderPayReqData generateProductWithOpenId(String orderNumber, String detail, String notifyUrl, BigDecimal price, String spbillCreateIp) throws NoSuchAlgorithmException {
        //#########################################
        Integer totalFee = price.multiply(new BigDecimal(100)).intValue();
        WXUnifiedOrderPayReqData wxUnifiedOrderPayReqData = new WXUnifiedOrderPayReqData(orderNumber, detail, totalFee, spbillCreateIp, notifyUrl);
        return wxUnifiedOrderPayReqData;
    }

    /**
     * 微信支付APP所需参数
     * @param prepay_id
     * @return
     * @throws NoSuchAlgorithmException
     */
    public Map generateWxSign(String prepay_id) throws NoSuchAlgorithmException {
        Map<String,Object> map = new HashMap<>();
        map.put("appid", Constant.WECHAT_PAY_APPID);
        map.put("partnerid", Constant.WECHAT_PAY_MCHID);
        map.put("prepayid", prepay_id);
        map.put("noncestr", RandomUtil.getRandomStringByLength(32).toUpperCase());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()).substring(0,10));
        map.put("package", "Sign=WXPay");

        String sign = WXSignature.getSign(map);
        map.put("sign",sign);

        return map;
    }

    public static String getRequestXml(Map<String,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            if (entry.getValue() instanceof String && "".equals(entry.getValue())) {
                continue;
            }
            String v = (String)entry.getValue();
            //if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)) {
            if (false) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<");
                sb.append(k);
                sb.append(">");
                sb.append(v);
                sb.append("</");
                sb.append(k);
                sb.append(">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

}

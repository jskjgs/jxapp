package com.jishi.reservation.otherService.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.doraemon.base.util.RandomUtil;
import com.google.common.base.Preconditions;
import com.jishi.reservation.conf.PayConfiguration;
import com.jishi.reservation.controller.protocol.OrderGenerateVO;
import com.jishi.reservation.dao.mapper.OrderInfoMapper;
import com.jishi.reservation.dao.models.OrderInfo;
import com.jishi.reservation.otherService.pay.protocol.AliPayCallbackModel;
import com.jishi.reservation.service.enumPackage.OrderStatusEnum;
import com.jishi.reservation.service.enumPackage.PayEnum;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.OrderInfoUtil2_0;
import com.jishi.reservation.util.PayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zbs on 2017/9/19.
 */

@Service
@Slf4j
public class AlibabaPay {


    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    private PayConfiguration payConfiguration;


    /**
     * 退款  返回0成功   1失败
     * @param orderNumber 支付宝的订单号
     * @return
     * @throws Exception
     */
    public Integer refund(String orderNumber) throws Exception {


         OrderInfo orderInfo =  orderInfoMapper.queryByNumber(orderNumber);
         log.info("订单号为"+orderNumber+"的订单申请退款，退款金额："+orderInfo.getPrice());
         Preconditions.checkState(orderInfo.getStatus()==OrderStatusEnum.PAYED.getCode(),"该笔订单未支付，不能退款");
        AlipayClient alipayClient = new DefaultAlipayClient(PayConstant.SERVER_URL_REFUND,PayConstant.APP_ID,PayConstant.APP_PRIVATE_KEY,PayConstant.DATA_FORMAT,PayConstant.CHARSET_GBK,PayConstant.ALI_PAY_PUBLIC_KEY,PayConstant.ENCRYPT);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "\"trade_no\":"+orderInfo.getThirdOrderNumber()+","+
                "\"refund_amount\":"+orderInfo.getPrice()+"," +
                "\"refund_reason\":\"正常退款\"" +
                "  }");
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        log.info("退款详情：\n"+JSONObject.toJSONString(response));
        if(response.isSuccess()){
           log.info("退款成功");
            return 0;
        } else {
            log.info("退款失败");

            return 1;
        }

    }



    public OrderGenerateVO generateOrder(String orderNumber,String subject, BigDecimal price) throws Exception {
        AlipayClient client = new DefaultAlipayClient(
                PayConstant.SERVER_URL,
                PayConstant.APP_ID,
                PayConstant.APP_PRIVATE_KEY
                ,
                PayConstant.DATA_FORMAT,
                PayConstant.CHARSET,

                PayConstant.ALI_PAY_PUBLIC_KEY,
                PayConstant.ENCRYPT
        );
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(subject);
        model.setSubject(subject);

        model.setOutTradeNo(orderNumber);
        model.setTimeoutExpress(PayConstant.TIME_OUT_EXPRESS);
        model.setTotalAmount(String.valueOf(price));
        model.setProductCode(PayConstant.QUICK_MSECURITY_PAY);
        log.info("生成的订单请求对象："+JSONObject.toJSONString(model));
        request.setBizModel(model);
        String notifyUrl = payConfiguration.getPayCallbackBaseUrl() + "/pay/aliPayCallBack";
        log.info("支付成功通知地址：" + notifyUrl);
        request.setNotifyUrl(notifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = client.sdkExecute(request);
            log.info("支付宝返回的处理结果：\n"+JSONObject.toJSONString(response));

            OrderGenerateVO vo = new OrderGenerateVO();
            vo.setOrderNumber(orderNumber);
            vo.setOrderString(response.getBody());
            return vo;

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;

    }


    public String aliPay_notify(AliPayCallbackModel model) throws ParseException {
        System.out.println("支付宝支付结果通知:\n"+JSONObject.toJSONString(model));
        Map<String, String> params = model.toMap();
        log.info("转换后的map\n"+JSONObject.toJSONString(params));
        //获取支付宝POST过来反馈信息
       // Map<String,String> params = new HashMap<String,String>();


        try {
            //加密采用RSA...
            boolean flag = AlipaySignature.rsaCheckV1(params, PayConstant.ALI_PAY_PUBLIC_KEY, PayConstant.CHARSET,PayConstant.ENCRYPT);
            log.info(""+flag);

                if("TRADE_SUCCESS".equals(params.get("trade_status"))){
                    //付款金额
                    String amount =  params.get("total_amount");
                    if(amount!=null&&!"".equals(amount)){
                        System.out.println("金额："+amount);
                    }else {
                        System.out.println("~~~~~~~~~~~~~~");
                    }
                    //商户订单号
                    String outTradeNo =  params.get("out_trade_no");
                    //支付宝交易号
                    String trade_no =  params.get("trade_no");
                    //支付时间
                    String payTime = params.get("notify_time");
                    //附加数据
                    //购买者邮箱
                    String buyerEmail = params.get("buyer_email");
                    //购买者id
                    String buyerId = params.get("buyer_id");
                    //商家邮箱
                    String sellerEmail = params.get("seller_email");
                    //String passback_params = URLDecoder.decode(params.get("passback_params"));


                    //判断支付金额和商户订单号和自己系统中的信息是否吻合，做判断
                    log.info("订单号："+outTradeNo);
                    OrderInfo orderInfo =  orderInfoMapper.queryByOutTradeNo(outTradeNo);
                    Preconditions.checkNotNull(orderInfo,"找不到该订单信息");

                    log.info("订单信息：\n"+JSONObject.toJSONString(orderInfo));

                    Preconditions.checkState(String.valueOf(orderInfo.getPrice()).equals(amount),"支付宝传递的订单金额与系统的订单金额不符合，回调失败");
                    //todo  调取his的门诊号缴费单

                    //改变订单状态和支付时间
                    //Preconditions.checkState(orderInfo.getStatus() == OrderStatusEnum.WAIT_PAYED.getCode(),"该订单不是待支付状态.");
                    orderInfo.setStatus(OrderStatusEnum.PAYED.getCode());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    orderInfo.setPayTime(sdf.parse(payTime));
                    orderInfo.setBuyerEmail(buyerEmail);
                    orderInfo.setSellerEmail(sellerEmail);
                    orderInfo.setBuyerId(buyerId);
                    orderInfo.setPayType(PayEnum.ALI.getCode());
                    orderInfo.setThirdOrderNumber(trade_no);
                    orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                    log.info("订单状态修改为已支付。订单id:"+orderInfo.getId());

                }

        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "success";
    }

    public static String generateUniqueOrderNumber() throws Exception {

       String prefix = "jxt_";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return prefix+format+RandomUtil.getRandomLetterAndNum(6);
    }


}

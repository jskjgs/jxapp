package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;

import com.google.common.base.Preconditions;
import com.jishi.reservation.conf.PayConfiguration;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.OrderGenerateVO;
import com.jishi.reservation.otherService.pay.AlibabaPay;
import com.jishi.reservation.otherService.pay.WeChatPay;
import com.jishi.reservation.otherService.pay.protocol.AliPayCallbackModel;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;

import com.jishi.reservation.service.exception.BussinessException;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/pay")
@Slf4j
@Api(description = "支付接口")
public class PayController extends MyBaseController {


    @Autowired
    private AlibabaPay alibabaPay;

    @Autowired
    private WeChatPay weChatPay;

    @Autowired
    private PayConfiguration payConfiguration;

    /**
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "阿里支付回调接口", notes = "")
    @RequestMapping(value = "aliPayCallBack", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject aliPayCallBack(
            AliPayCallbackModel model
    ) throws Exception {

        //todo  看到时候给我哪些参数...
        alibabaPay.aliPay_notify(model);

        return ResponseWrapper().addMessage("回调成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    /**
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信支付回调接口", notes = "")
    @RequestMapping(value = "wxPayCallBack", method = RequestMethod.POST)
    @ResponseBody
    public String wxPayCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //读取参数
        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();

        String returnCode = "SUCCESS";
        String returnMsg = "ok";
        try {
            boolean rslt = weChatPay.notify(sb.toString());
            if (!rslt) {
                returnCode = "FAIL";
                returnMsg = "FAIL";
            }
        } catch (BussinessException e) {
            log.info(e.toString());
            returnCode = "FAIL";
            returnMsg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            returnCode = "FAIL";
            returnMsg = e.getClass().getSimpleName();
        }
        log.info("微信支付回调接口返回数据：returnCode=" + returnCode + " returnMsg=" + returnMsg);
        return wxPayNotifyResponse(returnCode, returnMsg);
    }


    /**
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "阿里支付", notes = "")
    @RequestMapping(value = "aliPay", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject aliPay(
            @ApiParam(value = "商户生成的订单号") @RequestParam(value = "orderNumber") String orderNumber,
            @ApiParam(value = "支付的商品名称") @RequestParam(value = "subject") String subject,
            @ApiParam(value = "支付的商品价格 元为单位") @RequestParam(value = "price") BigDecimal price
    ) throws Exception {

        Preconditions.checkNotNull(subject,"缺少参数：subject");
        Preconditions.checkNotNull(orderNumber,"缺少参数：orderNumber");
        Preconditions.checkNotNull(price,"缺少参数：price");

        OrderGenerateVO vo = alibabaPay.generateOrder(orderNumber,subject, price);

        return ResponseWrapper().addData(vo).addMessage("请求成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }



    /**
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "阿里退款", notes = "")
    @RequestMapping(value = "aliRefund", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject aliPay(
            @ApiParam(value = "订单号") @RequestParam(value = "orderNumber") String orderNumber
    ) throws Exception {

        alibabaPay.refund(orderNumber);

        return ResponseWrapper().addMessage("请求成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    /**
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信支付", notes = "")
    @RequestMapping(value = "wxPay", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject wxPay(
        @ApiParam(value = "用户端实际ip") @RequestParam(value = "spbillCreateIp") String spbillCreateIp,
        @ApiParam(value = "商户生成的订单号") @RequestParam(value = "orderNumber") String orderNumber,
        @ApiParam(value = "支付的商品名称") @RequestParam(value = "subject") String subject,
        @ApiParam(value = "支付的商品价格 元为单位") @RequestParam(value = "price") BigDecimal price) throws Exception {

        Preconditions.checkNotNull(subject,"缺少参数：subject");
        Preconditions.checkNotNull(orderNumber,"缺少参数：orderNumber");
        Preconditions.checkNotNull(price,"缺少参数：price");

        String notifyUrl = payConfiguration.getPayCallbackBaseUrl() + "/pay/wxPayCallBack";
        log.info("支付成功通知地址：" + notifyUrl);
        Map map = weChatPay.generateOrder(notifyUrl, orderNumber,subject, price, spbillCreateIp);

        return ResponseWrapper().addData(map).addMessage("请求成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    private String wxPayNotifyResponse(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode
                  + "]]></return_code><return_msg><![CDATA[" + returnMsg
                  + "]]></return_msg></xml>";
    }
}

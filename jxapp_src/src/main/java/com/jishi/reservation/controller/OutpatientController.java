package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.*;
import com.jishi.reservation.dao.models.OrderInfo;
import com.jishi.reservation.service.OutpatientQueueService;
import com.jishi.reservation.service.OutpatientService;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liangxiong on 2017/10/25.
 */
@RestController
@RequestMapping("/outpatient")
@Slf4j
@Api(description = "门诊缴费相关接口")
public class OutpatientController extends MyBaseController {

    @Autowired
    private OutpatientService outpatientService;

    @Autowired
    private OutpatientQueueService outpatientQueueService;


    @ApiOperation(value = "门诊缴费列表，默认处理收费单，挂号单不处理", response = OutpatientPaymentInfoVO.class)
    @RequestMapping(value="/paymentInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryOutpatientPamentInfo(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                      @ApiParam(value = "支付状态，0-待支付，1-全部(包含待支付已支付已退费)，(默认为1)", required = false) @RequestParam(value = "status", defaultValue = "1", required = false) Integer status,
                      @ApiParam(value = "页数(支付状态为1时提供，默认第一页，页数从1开始)", required = false) @RequestParam(value = "startPage", defaultValue = "1", required = false) Integer startPage,
                      @ApiParam(value = "每页多少条(支付状态为1时提供)，默认10", required = false) @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) throws Exception {
        List<OutpatientPaymentInfoVO> outpatientPamentInfo = outpatientService.queryOutpatientPamentInfo(accountId, status, startPage, pageSize);
        return ResponseWrapperSuccess(outpatientPamentInfo);
    }

    @ApiOperation(value = "生成订单", response = OrderInfo.class)
    @RequestMapping(value = "generateOrder", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject generateOrder(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "预交的名称 eg:...门诊缴费", required = true) @RequestParam(value = "subject", required = true) String subject,
            @ApiParam(value = "交易的金额", required = true) @RequestParam(value = "price", required = true) BigDecimal price,
            @ApiParam(value = "brId(his病人ID)", required = true) @RequestParam(value = "brId", required = true) String brId,
            @ApiParam(value = "挂号单号", required = true) @RequestParam(value = "registerNumber", required = true) String registerNumber,
            @ApiParam(value = "单据ID，可以多个单据，以','分隔", required = true) @RequestParam(value = "docIds", required = true) String docIds,
            @ApiParam(value = "单据类型，1-收费单，4-挂号单", required = true) @RequestParam(value = "documentType", required = true) Integer documentType) throws Exception {

        OrderInfo orderInfo = outpatientService.generatePaymentOrder(accountId, brId, registerNumber, subject, price, docIds, documentType);

        return ResponseWrapperSuccess(orderInfo);
    }

    @ApiOperation(value = "门诊缴费确认(单个)", response = OrderVO.class)
    @RequestMapping(value="/payConfirm", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public JSONObject payModify(
                  @ApiParam(value = "订单号", required = true) @RequestParam(value = "orderNumber", required = true) String orderNumber) throws Exception {
        OrderVO orderVO = outpatientService.payConfirm(orderNumber);
        return ResponseWrapperSuccess(orderVO);
    }

    @ApiOperation(value = "门诊缴费确认(可多个单据)", response = OrderVO.class)
    @RequestMapping(value="/batchpayConfirm", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject outpatientPaymentConfirm(
                @ApiParam(value = "订单号", required = true) @RequestParam(value = "orderNumber", required = true) String orderNumber) throws Exception {
        OrderVO orderVO = outpatientService.batchpayConfirm(orderNumber);
        return ResponseWrapperSuccess(orderVO);
    }

    @ApiOperation(value = "门诊记录", response = OutpatientVisitRecordVO.class)
    @RequestMapping(value="/visitRecord", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryVisitRecord(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) throws Exception {
        List<OutpatientVisitRecordVO> recordVOList = outpatientService.queryVisitRecord(accountId, pageNum, pageSize);
        return ResponseWrapperSuccess(recordVOList);
    }

    @ApiOperation(value = "门诊记录的单据费用信息", response = OutpatientVisitReceiptVO.class)
    @RequestMapping(value="/visitReceipt", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject visitReceipt(
                                       @ApiParam(value = "挂号单号", required = true) @RequestParam(value = "registerNum") String registerNum) throws Exception {
        List<OutpatientVisitReceiptVO> receiptVOList = outpatientService.queryVisitReceipt(registerNum);
        return ResponseWrapperSuccess(receiptVOList);
    }

    @ApiOperation(value = "门诊记录的单据处方信息", response = OutpatientVisitPrescriptionVO.class)
    @RequestMapping(value="/visitPrescription", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryVisitPrescription(
                                           @ApiParam(value = "挂号单号", required = true) @RequestParam(value = "registerNum") String registerNum) throws Exception {
        List<OutpatientVisitPrescriptionVO> prescriptionVOList = outpatientService.queryVisitPrescription(registerNum);
        return ResponseWrapperSuccess(prescriptionVOList);
    }

    @ApiOperation(value = "就诊排队信息(根据token或brid获取)", response = OutpatientQueueDetailVO.class)
    @RequestMapping(value="/visitQueueInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject visitQueueInfo(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                                 @ApiParam(value = "brId(his病人ID)", required = false) @RequestParam(value = "brId", required = false) String brId) throws Exception {

        List<OutpatientQueueDetailVO> visitQueueInfoList = outpatientQueueService.generateTestData(4);
        //List<OutpatientQueueDetailVO> visitQueueInfoList = outpatientQueueService.queryVisitQueueInfo(accountId, brId);
        return ResponseWrapperSuccess(visitQueueInfoList);
    }

}

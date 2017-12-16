package com.jishi.reservation.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.dao.models.QueueLength;
import com.jishi.reservation.service.OutpatientQueueService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liangxiong on 2017/11/22.
 */
@RestController
@RequestMapping("/ad/queue")
@Slf4j
@Api(description = "门诊排队叫号推送队列长度相关接口")
public class AdminQueueController extends MyBaseController {

    @Autowired
    private OutpatientQueueService outpatientQueueService;

    @ApiOperation(value = "添加医生就诊排队通知队列长度", response = String.class)
    @RequestMapping(value="/addDoctorNotifyLength", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addDoctorNotifyLength(
              @ApiParam(value = "his医生id", required = true) @RequestParam(value = "doctorHisId") String doctorHisId,
              @ApiParam(value = "通知队列长度", required = true) @RequestParam(value = "length") Integer length) throws Exception {
        boolean rslt = outpatientQueueService.addQueueLength(doctorHisId, length, false);
        return rslt ? ResponseWrapper().addMessage("success").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode()) :
          ResponseWrapper().addMessage("failed").ExeSuccess(ReturnCodeEnum.FAILED.getCode());
    }

    @ApiOperation(value = "添加部门就诊排队通知队列长度", response = String.class)
    @RequestMapping(value="/addDepartNotifyLength", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addDepartNotifyLength(
              @ApiParam(value = "his部门id", required = true) @RequestParam(value = "departHisId") String departHisId,
              @ApiParam(value = "通知队列长度", required = true) @RequestParam(value = "length") Integer length) throws Exception {
        boolean rslt = outpatientQueueService.addQueueLength(departHisId, length, true);
        return rslt ? ResponseWrapper().addMessage("success").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode()) :
          ResponseWrapper().addMessage("failed").ExeSuccess(ReturnCodeEnum.FAILED.getCode());
    }

    @ApiOperation(value = "修改医生就诊排队通知队列长度", response = String.class)
    @RequestMapping(value="/updateDoctorNotifyLength", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateDoctorNotifyLength(
              @ApiParam(value = "his医生id", required = true) @RequestParam(value = "doctorHisId") String doctorHisId,
              @ApiParam(value = "通知队列长度", required = true) @RequestParam(value = "length") Integer length) throws Exception {
        boolean rslt = outpatientQueueService.updateQueueLength(doctorHisId, length, false);
        return rslt ? ResponseWrapper().addMessage("success").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode()) :
          ResponseWrapper().addMessage("failed").ExeSuccess(ReturnCodeEnum.FAILED.getCode());
    }

    @ApiOperation(value = "修改部门就诊排队通知队列长度", response = String.class)
    @RequestMapping(value="/updateDepartNotifyLength", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateDepartNotifyLength(
              @ApiParam(value = "his部门id", required = true) @RequestParam(value = "departHisId") String departHisId,
              @ApiParam(value = "通知队列长度", required = true) @RequestParam(value = "length") Integer length) throws Exception {
        boolean rslt = outpatientQueueService.updateQueueLength(departHisId, length, true);
        return rslt ? ResponseWrapper().addMessage("success").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode()) :
          ResponseWrapper().addMessage("failed").ExeSuccess(ReturnCodeEnum.FAILED.getCode());
    }

    @ApiOperation(value = "查询所有就诊排队通知队列长度，数组", response = QueueLength.class)
    @RequestMapping(value="/queryNotifyLength", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryQueueNotifyLength() throws Exception {
        List<QueueLength> visitQueueInfoList = outpatientQueueService.queryQueueLengthAll();
        return ResponseWrapper().addData(visitQueueInfoList).addMessage("查询成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询所有就诊排队通知队列长度", response = QueueLength.class)
    @RequestMapping(value="/queryDoctorNotifyLength", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryDoctorQueueNotifyLength(
                @ApiParam(value = "his医生id", required = true) @RequestParam(value = "doctorHisId") String doctorHisId) throws Exception {
        QueueLength queueLength = outpatientQueueService.queryDoctorQueueLength(doctorHisId);
        return ResponseWrapper().addData(queueLength).addMessage("查询成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询所有就诊排队通知队列长度", response = QueueLength.class)
    @RequestMapping(value="/queryDepartNotifyLength", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryDepartQueueNotifyLength(
                @ApiParam(value = "his部门id", required = true) @RequestParam(value = "departHisId") String departHisId) throws Exception {
        QueueLength queueLength = outpatientQueueService.queryDepartQueueLength(departHisId);
        return ResponseWrapper().addData(queueLength).addMessage("查询成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

}

package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.dao.models.PatientInfo;
import com.jishi.reservation.service.AccountService;
import com.jishi.reservation.service.PatientInfoService;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.Helpers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */

@RestController
@RequestMapping("/patientInfo")
@Slf4j
@Api(description = "就诊人相关接口")
public class PatientInfoController extends MyBaseController
{

    @Autowired
    PatientInfoService patientInfoService;

    @Autowired
    AccountService accountService;

    @ApiOperation(value = "增加就诊人信息  8月30号提出 一个账号最多有5个")
    @RequestMapping(value = "addPatientInfo", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject addPatientInfo(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                                    @ApiParam(value = "就诊人名称", required = true) @RequestParam(value = "name", required = true) String name,
                                      @ApiParam(value = "病人电话", required = true) @RequestParam(value = "phone", required = true) String phone,
                                    @ApiParam(value = "病人身份证", required = true) @RequestParam(value = "idCard", required = true) String idCard,
                                     @ApiParam(value = "证件类型") @RequestParam(value = "idCardType") String idCardTpye
                                     ) throws Exception {

        Preconditions.checkNotNull(name,"请传入必须的参数：name");
        Preconditions.checkNotNull(phone,"请传入必须的参数：phone");
        Preconditions.checkNotNull(idCard,"请传入必须的参数：idCard");

        if("".equals(name) || "".equals(phone)){
           return ResponseWrapper().addMessage("姓名或手机不能为空").ExeFaild(ReturnCodeEnum.FAILED.getCode());
        }
       Long id =  patientInfoService.addPatientInfo(accountId, name, phone, idCard,idCardTpye);
        return ResponseWrapper().addData(id).addMessage("添加成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询就诊人信息", response = PatientInfo.class)
    @RequestMapping(value = "queryPatientInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryPatientInfo(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                                       @ApiParam(value = "就诊人ID", required = false) @RequestParam(value = "patientInfoId", required = false) Long patientInfoId) throws Exception {

        if (Helpers.isNullOrEmpty(patientInfoId) && Helpers.isNullOrEmpty(accountId))
            throw new Exception("查询条件不能都为空");
        List<PatientInfo> patientInfos = patientInfoService.queryPatientInfo( patientInfoId,accountId, EnableEnum.EFFECTIVE.getCode());
        patientInfoService.wrapPregnant(patientInfos);
        return ResponseWrapper().addData(patientInfos).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询全部就诊人信息", response = PatientInfo.class)
    @RequestMapping(value = "queryAllPatientInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryAllPatientInfo(
            @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "排序", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception {
        PageInfo<PatientInfo> patientInfo = patientInfoService.queryPatientInfoPagaInfo(null, null, 0, Paging.create(pageNum,pageSize,orderBy,desc));
        return ResponseWrapper().addData(patientInfo).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "app 通过token查询该用户所有就诊人信息", response = PatientInfo.class)
    @RequestMapping(value = "queryPatientInfoByToken", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryPatientInfoByToken(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                                              @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "排序", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception {

        PageInfo<PatientInfo> patientInfo = patientInfoService.queryPatientInfoPagaInfo(null, accountId, EnableEnum.EFFECTIVE.getCode(), Paging.create(pageNum,pageSize,orderBy,desc));
        patientInfoService.wrapPregnant(patientInfo.getList());
        return ResponseWrapper().addData(patientInfo).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "修改就诊人信息")
    @RequestMapping(value = "modifyPatientInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyPatientInfo(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "就诊人ID") @RequestParam(value = "patientInfoId", required = true) Long patientInfoId,
            @ApiParam(value = "就诊人名称",required = false) @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "病人电话",required = false) @RequestParam(value = "phone", required = false) String phone,
            @ApiParam(value = "病人身份证",required = false) @RequestParam(value = "idCard", required = false) String idCard) throws Exception {

        patientInfoService.modifyPatientInfo(accountId,patientInfoId, name, phone, idCard, null);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "删除就诊人")
    @RequestMapping(value = "failurePatientInfo", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deletePatientInfo(
            @ApiParam(value = "就诊人ID", required = true) @RequestParam(value = "patientInfoId", required = true) Long patientInfoId
    ) throws Exception {
        Preconditions.checkNotNull(patientInfoId,"请传入必须的参数：patientInfoId");

        patientInfoService.deletePatientInfo(patientInfoId);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


}

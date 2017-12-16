package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.LoginData;
import com.jishi.reservation.controller.protocol.PatientHisVO;
import com.jishi.reservation.service.AccountService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.his.HisUserManager;
import com.jishi.reservation.service.his.bean.Credentials;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/his_account")
@Slf4j
@Api(description = "对接了his系统的账号相关接口")
public class HisAccountController extends MyBaseController {

    @Autowired
    AccountService accountService;


    @Autowired
    HisUserManager hisUserManager;


    @ApiOperation(value = "通过手机号来查询用户就诊信息")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject query(
            @ApiParam(value = "账号") @RequestParam(value = "account") String account
    ) throws Exception {

        Preconditions.checkNotNull(account,"请传入所需要的参数：account");
        LoginData loginData = accountService.queryInfo(account);
        return ResponseWrapper().addData(loginData).addMessage("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "通過身份證去his系统查询就诊人信息")
    @RequestMapping(value = "queryPatient", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryPatient(
            @ApiParam(value = "身份证") @RequestParam(value = "idCard",required = true) String idCard,
            @ApiParam(value = "卡号类型 固定传 二代身份证") @RequestParam(value = "idCardType",required = true) String idCardType,
            @ApiParam(value = "姓名") @RequestParam(value = "name",required = true) String name


            ) throws Exception {

        Preconditions.checkNotNull(idCard,"请传入所需要的参数：idCard");
        Preconditions.checkNotNull(idCardType,"请传入所需要的参数：idCardType");

        Credentials cre = hisUserManager.getUserInfoByRegNO(idCard, idCardType, name, idCard, idCardType);

        PatientHisVO vo = new PatientHisVO();
        vo.setBrId(cre.getBRID());
        vo.setIdCard(idCard);
        vo.setMzh(cre.getMZH());

        return ResponseWrapper().addData(vo).addMessage("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

}

package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.IMAccountVO;
import com.jishi.reservation.dao.models.IMAccount;
import com.jishi.reservation.otherService.im.neteasy.model.IMUser;
import com.jishi.reservation.service.IMAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liangxiong on 2017/12/8.
 */
@RestController
@RequestMapping("/doctor_im")
@Slf4j
@Api(description = "Doctor IM账号相关接口")
public class DoctorImController extends MyBaseController {

    @Autowired
    private IMAccountService imAccountService;

    @ApiOperation(value = "登录医生", response = IMAccountVO.class)
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject login(
              @ApiParam(value = "username", required = false) @RequestParam(value = "username", required = false) Long username,
              @ApiParam(value = "password", required = false) @RequestParam(value = "password", required = false) Long password
              ) throws Exception {
        // 先用测试账号
        IMAccount imAccount = imAccountService.getDoctorIMAccount(37L);
        return ResponseWrapperSuccess(imAccount);
    }

    @ApiOperation(value = "获取医生im账号，token可用于im客户端登录，没有则创建", response = IMAccountVO.class)
    @RequestMapping(value = "/getDoctorAccount", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDoctorAccount(
              @ApiParam(value = "doctorId", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId) throws Exception {
        IMAccount imAccount = imAccountService.getDoctorIMAccount(doctorId);
        IMAccountVO imAccountVO =new IMAccountVO();
        imAccountVO.setImAccId(imAccount.getImAccId());
        imAccountVO.setImToken(imAccount.getImToken());
        return ResponseWrapperSuccess(imAccountVO);
    }

    @ApiOperation(value = "更新医生im token，token失效时调用", response = String.class)
    @RequestMapping(value = "/refreshDoctorToken", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject refreshDoctorToken(
              @ApiParam(value = "doctorId", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId) throws Exception {
        String imToken = imAccountService.refreshDoctorToken(doctorId);
        return ResponseWrapperSuccess(imToken);
    }

    @ApiOperation(value = "获取医生IM账号信息", response = IMUser.class)
    @RequestMapping(value = "/getDoctorIMDetail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDoctorDetail(
              @ApiParam(value = "doctorId", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId) throws Exception {

        // TODO 医生账号怎么验证账号已登录
        IMUser imUserVO = imAccountService.queryUser(doctorId, true);
        return ResponseWrapperSuccess(imUserVO);
    }
}

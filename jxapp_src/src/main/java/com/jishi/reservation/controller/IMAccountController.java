package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.IMAccountVO;
import com.jishi.reservation.controller.protocol.IMChatInfo;
import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.dao.models.IMAccount;
import com.jishi.reservation.otherService.im.neteasy.model.IMUser;
import com.jishi.reservation.service.AccountService;
import com.jishi.reservation.service.IMAccountService;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created by liangxiong on 2017/10/27.
 */
@RestController
@RequestMapping("/im")
@Slf4j
@Api(description = "IM用户账号相关接口")
public class IMAccountController extends MyBaseController {

    @Autowired
    AccountService accountService;

    @Autowired
    private IMAccountService imAccountService;

    @ApiOperation(value = "获取普通用户im账号，token可用于im客户端登录，没有则创建", response = IMAccountVO.class)
    @RequestMapping(value = "/getUserAccount", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserAccount(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId) throws Exception {

        IMAccount imAccount = imAccountService.getUserIMAccount(accountId);
        IMAccountVO imAccountVO =new IMAccountVO();
        imAccountVO.setImAccId(imAccount.getImAccId());
        imAccountVO.setImToken(imAccount.getImToken());
        return ResponseWrapperSuccess(imAccountVO);
    }

    @ApiOperation(value = "不建议使用的接口，获取普通用户im token", response = String.class)
    @RequestMapping(value = "/getUserToken", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public JSONObject getUserToken(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId) throws Exception {
        String imToken = imAccountService.getUserIMAccount(accountId).getImToken();
        return ResponseWrapperSuccess(imToken);
    }

    @ApiOperation(value = "更新普通用户im token，token失效时调用", response = String.class)
    @RequestMapping(value = "/refreshUserToken", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject refreshUserToken(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId) throws Exception {
        String imToken = imAccountService.refreshUserToken(accountId);
        return ResponseWrapperSuccess(imToken);
    }

    @ApiOperation(value = "获取聊天信息，医生im账户，用户im账号和token", response = IMChatInfo.class)
    @RequestMapping(value = "/chatToDocter", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject chatToDocter(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                                         @ApiParam(value = "doctorId", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId) throws Exception {
        IMChatInfo info = imAccountService.chatToDocter(accountId, doctorId);
        return ResponseWrapperSuccess(info);
    }

    @ApiOperation(value = "获取咨询医生历史列表", response = Doctor.class)
    @RequestMapping(value = "/visitHistory", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject visitHistory(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId) throws Exception {
        List<Doctor> imAccessRecordList = imAccountService.queryUserIMAccessRecord(accountId);
        return ResponseWrapperSuccess(imAccessRecordList);
    }

    @ApiOperation(value = "更新咨询医生时间", response = Doctor.class)
    @RequestMapping(value = "/updateVisitTime", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateVisitTime(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                      @ApiParam(value = "doctorId", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId) throws Exception {
        imAccountService.updateVisitRecord(accountId, doctorId);
        return ResponseWrapperSuccess();
    }

    @ApiOperation(value = "获取用户IM账号信息", response = IMUser.class)
    @RequestMapping(value = "/getUserIMDetail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserDetail(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId) throws Exception {
        IMUser imUserVO = imAccountService.queryUser(accountId, false);
        return ResponseWrapperSuccess(imUserVO);
    }
}

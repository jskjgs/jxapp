package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;

import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.LoginData;
import com.jishi.reservation.service.AccountService;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.enumPackage.SmsEnum;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/account")
@Slf4j
@Api(description = "账号相关接口")
public class AccountController extends MyBaseController {

    @Autowired
    AccountService accountService;


    @Value("constant.dynamic_code_key.login_or_register")
    public String loginOrRegister;
    @Value("constant.dynamic_code_key.change_phone_origin")
    public String changePhoneOrigin;
    @Value("constant.dynamic_code_key.change_phone_new")
    public String changePhoneNew;




    @ApiOperation(value = "采用手机进行动态码登陆和注册(如果已经注册就走登陆,如果没有注册,先注册再登陆)")
    @RequestMapping(value = "loginOrRegisterThroughPhone", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject loginOrRegisterThroughPhone(
            @ApiParam(value = "电话", required = true) @RequestParam(value = "phone", required = true) String phone,
            @ApiParam(value = "动态码", required = true) @RequestParam(value = "dynamicCode", required = true) String dynamicCode) throws Exception {
        Preconditions.checkNotNull(phone,"请传入所需要的参数：phone");
        Preconditions.checkNotNull(dynamicCode,"请传入所需要的参数：dynamicCode");

        //todo 检测是否是合格手机号
        if(!accountService.isValidTelephone(phone)){
            return ResponseWrapper().addMessage("请输入正确的手机号").ExeFaild(ReturnCodeEnum.FAILED.getCode());
        }

        LoginData loginData = accountService.loginOrRegisterThroughPhone(phone,loginOrRegister ,dynamicCode);
        if(loginData==null)
            return ResponseWrapper().addMessage("验证码错误,登录失败!").ExeFaild(ReturnCodeEnum.VALID_CODE_WRONG.getCode());
        return ResponseWrapper().addData(loginData).addMessage("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "退出登陆  token放在param里面进行传递")
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject logout(@RequestParam(value = "token") String token) throws Exception {
        Preconditions.checkNotNull(token,"请传入所需要的参数：token");

        accountService.logout(token);
         return ResponseWrapper().addMessage("退出成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "发送登陆/注册动态验证码")
    @RequestMapping(value = "sendDynamicCode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendDynamicCode(
            @ApiParam(value = "电话", required = true) @RequestParam(value = "phone", required = true) String phone) throws Exception {
        Preconditions.checkNotNull(phone,"请传入所需要的参数：phone");

        String code = accountService.sendDynamicCode(phone, loginOrRegister,SmsEnum.LOGIN_REGISTER.getTemplateCode());
        return ResponseWrapper().addData(code).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "发送换绑手机的动态验证码   发给原手机")
    @RequestMapping(value = "originalPhoneCode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendChangePhoneDynamicCode(
            @ApiParam(value = "电话", required = true) @RequestParam(value = "phone", required = true) String phone) throws Exception {
        Preconditions.checkNotNull(phone,"请传入所需要的参数：电话号码");
        Preconditions.checkNotNull(accountService.queryAccount(null,phone,EnableEnum.EFFECTIVE.getCode()),"该手机用户不存在");
        String code = accountService.sendDynamicCode(phone,changePhoneOrigin,SmsEnum.CHANGE_BUNDLE_TELEPHONE_OLD.getTemplateCode());
        return ResponseWrapper().addData(code).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "验证原手机和原手机收到的验证码是否一致")
    @RequestMapping(value = "checkOriginalPhoneCode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkOriginalPhoneCode(
            @ApiParam(value = "电话", required = true) @RequestParam(value = "phone", required = true) String phone,
            @ApiParam(value = "动态码", required = true) @RequestParam(value = "dynamicCode", required = true) String dynamicCode) throws Exception {
            Preconditions.checkNotNull(phone,"请传入所需要的参数：电话号码");
            Preconditions.checkNotNull(dynamicCode,"请传入所需要的参数：动态码");


            return accountService.checkOriginalPhoneCode(phone,changePhoneOrigin,dynamicCode)?ResponseWrapper().addMessage("验证成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode()):
            ResponseWrapper().addMessage("验证失败!").ExeFaild(ReturnCodeEnum.FAILED.getCode());
    }

    @ApiOperation(value = "发送换绑手机的动态验证码   发给第二个手机")
    @RequestMapping(value = "newPhoneCode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendSurePhoneDynamicCode(
            @ApiParam(value = "电话", required = true) @RequestParam(value = "phone", required = true) String phone) throws Exception {
        Preconditions.checkNotNull(phone,"请传入所需要的参数：电话号码");

        String code = accountService.sendDynamicCode(phone,changePhoneNew,SmsEnum.CHANGE_BUNDLE_TELEPHONE_NEW.getTemplateCode());
        return ResponseWrapper().addData(code).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "换绑最后一步")
    @RequestMapping(value = "changePhone", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject changePhone(
            @ApiParam(value = "原电话") @RequestParam(value = "originalPhone") String originalPhone,
            @ApiParam(value = "新电话") @RequestParam(value = "newPhone") String newPhone,
            @ApiParam(value = "新电话收到的验证码") @RequestParam(value = "dynamicCode") String dynamicCode
    ) throws Exception {
        Preconditions.checkNotNull(originalPhone,"请传入所需要的参数：originalPhone");
        Preconditions.checkNotNull(newPhone,"请传入所需要的参数：newPhone");
        Preconditions.checkNotNull(dynamicCode,"请传入所需要的参数：dynamicCode");
        if(originalPhone.equals(newPhone)){
            return  ResponseWrapper().addMessage("换绑的新旧手机号不能相等").ExeFaild(ReturnCodeEnum.FAILED.getCode());
        }
        if(accountService.queryAccountByTelephone(newPhone)!=null){
            return  ResponseWrapper().addMessage("新号码已存在，不能换绑").ExeFaild(ReturnCodeEnum.FAILED.getCode());

        }

        return accountService.changePhone(originalPhone,changePhoneNew,newPhone,dynamicCode)?ResponseWrapper().addMessage("换绑成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode()):
                ResponseWrapper().addMessage("手机号和验证码不对应,换绑失败").ExeFaild(ReturnCodeEnum.FAILED.getCode()) ;
    }


    @ApiOperation(value = "传统的增加账号")
    @RequestMapping(value = "addAccount", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject addAccount(
            @ApiParam(value = "账号", required = true) @RequestParam(value = "account", required = true) String account,
            @ApiParam(value = "账号密码", required = true) @RequestParam(value = "passwd", required = true) String passwd,
            @ApiParam(value = "头像", required = false) @RequestParam(value = "headPortrait", required = false) String headPortrait,
            @ApiParam(value = "昵称", required = false) @RequestParam(value = "nick", required = false) String nick,
            @ApiParam(value = "电话", required = true) @RequestParam(value = "phone", required = true) String phone,
            @ApiParam(value = "邮箱", required = false) @RequestParam(value = "email", required = false) String email) throws Exception {
        accountService.addAccount(account,passwd,headPortrait,nick,phone,email);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "修改账号信息")
    @RequestMapping(value = "modifyAccountInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyAccountInfo(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "昵称", required = false) @RequestParam(value = "nick", required = false) String nick,
            @ApiParam(value = "头像", required = false) @RequestParam(value = "headPortrait", required = false) String headPortrait,
            @ApiParam(value = "邮箱", required = false) @RequestParam(value = "email", required = false) String email) throws Exception {

        accountService.modifyAccountInfo(accountId,null,nick,headPortrait,email,null,null);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "修改账号的密码")
    @RequestMapping(value = "modifyAccountPasswd", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyAccountPasswd(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "电话", required = false) @RequestParam(value = "phone", required = false) String phone,
            @ApiParam(value = "老密码", required = true) @RequestParam(value = "oldPasswd", required = true) String oldPasswd,
            @ApiParam(value = "新密码", required = true) @RequestParam(value = "newPasswd", required = true) String newPasswd) throws Exception {
        Preconditions.checkNotNull(oldPasswd,"请传入所需要的参数：oldPasswd");
        Preconditions.checkNotNull(newPasswd,"请传入所需要的参数：newPasswd");

        accountService.modifyAccountPasswd(accountId,phone,oldPasswd,newPasswd);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "修改账号绑定手机")
    @RequestMapping(value = "modifyAccountPhone", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyAccountPhone(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "电话", required = true) @RequestParam(value = "phone", required = true) String phone) throws Exception {

        Preconditions.checkNotNull(accountId,"请传入所需要的参数：accountId");
        Preconditions.checkNotNull(phone,"请传入所需要的参数：phone");

        accountService.modifyAccountPhone(accountId,phone);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


}

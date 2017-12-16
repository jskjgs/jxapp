package com.jishi.reservation.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doraemon.base.controller.BaseController;
import com.doraemon.base.controller.Result;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.exception.BussinessException;
import com.jishi.reservation.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by sloan on 2017/10/25.
 */
@Slf4j
public class MyBaseController extends BaseController {




    @Override
    @ExceptionHandler
    @ResponseBody
    public JSONObject exceptionHandle(Exception e) {
        doExceptionLog(e);
        int code = ReturnCodeEnum.ERR.getCode();
        String message = (e != null && e.getMessage() != null && e.getMessage().length() <= 30) ? e.getMessage() : ReturnCodeEnum.ERR.getDesc();
        return this.ResponseWrapper().addMessage(message).ExeFaild(code);
    }

    @ExceptionHandler
    @ResponseBody
    public JSONObject doBussinessException(BussinessException e) {
        doExceptionLog(e);
        return this.ResponseWrapper().addMessage(e.getMessage()).ExeFaild(e.getCodeNum());
    }

    @ExceptionHandler
    @ResponseBody
    public JSONObject doBaseException(BaseException e) {
        doExceptionLog(e);
        return this.ResponseWrapper().addMessage(e.getMessage()).ExeFaild(e.getCodeNum());
    }

    private void doExceptionLog(Exception e) {
        Long accountId = getCurrentUserId();
        JSONObject json = (JSONObject) JSON.toJSON(getCurrentRequest().getParameterMap());
        log.info("登录用户: " + accountId + "  uri：" + getCurrentRequest().getRequestURI() + "  params: " + json.toString());
        log.info("Exception: " + e);
        e.printStackTrace();
    }

    // 通过状态码枚举类返回
    public JSONObject ResponseWrapperFailed(ReturnCodeEnum returnCodeEnum) {
        Result result = this.ResponseWrapper().addMessage(returnCodeEnum.getDesc());
        if (returnCodeEnum.getCode() == 200) {
            return result.ExeSuccess(returnCodeEnum.getCode());
        }
        return result.ExeFaild(returnCodeEnum.getCode());
    }

    // 通过数据返回，状态码默认 ReturnCodeEnum.SUCCESS
    public JSONObject ResponseWrapperSuccess(Object data) {
        return this.ResponseWrapper().addData(data).addMessage(ReturnCodeEnum.SUCCESS.getDesc()).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    // 返回成功状态 ReturnCodeEnum.SUCCESS
    public JSONObject ResponseWrapperSuccess() {
        return ResponseWrapperSuccess(null);
    }


    public Long getCurrentUserId() {
        Object accountObj = this.getCurrentRequest().getAttribute(Constant.ATTR_LOGIN_ACCOUNT_ID);
        if (accountObj != null && accountObj instanceof Long) {
            return (Long) accountObj;
        }
        return Constant.NOT_LOGIN_ACCOUNT_ID;
    }

    @Override
    public String getToken() {
        return null;
    }
}

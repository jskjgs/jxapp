package com.jishi.reservation.service.exception;

  import com.jishi.reservation.controller.base.BaseException;
  import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;

/**
 * Created by liangxiong on 2017/11/28.
 */
public class BussinessException extends BaseException {

    public BussinessException(ReturnCodeEnum returnCode) {
        // enableSuppression是否启用禁止多个异常 writableStackTrace是否写入异常栈信息
        super(returnCode.getCode(), returnCode.name(), returnCode.getDesc(), null, true, false);
    }

    public BussinessException(ReturnCodeEnum returnCode, Throwable throwable) {
        super(returnCode.getCode(), returnCode.name(), returnCode.getDesc(), throwable, true, false);
    }
}

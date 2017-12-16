package com.jishi.reservation.util;

/**
 * Created by zbs on 2017/8/9.
 */
public class MyException extends RuntimeException {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = -2969389431023708255L;

    /**
     * 错误码
     */
    private final MyEnum errorCode;

    /**
     * 错误详细描述
     */
    private String detailMessage;

    public MyException(String errorMsg) {
        super(errorMsg);
        errorCode = MyEnum.SYSTEM_ERROR;
    }

    /**
     * 含有异常栈的构造函数
     *
     * @param errorCode
     * @param t
     */
    public MyException(MyEnum errorCode, Throwable t) {
        super(errorCode.getDesc(), t);
        this.errorCode = errorCode;
    }

    public MyException(MyEnum errorCode, String detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    /***
     * 含有异常栈和错误信息构造器,使用时需要特别注意此方法构造出来的异常结果码为默认的系统异常结果码
     *
     * @param errorMsg
     * @param t
     */
    public MyException(String errorMsg, Throwable t) {
        super(errorMsg, t);
        errorCode = MyEnum.SYSTEM_ERROR;
    }

    public MyException(MyEnum errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    public MyEnum getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
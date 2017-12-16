package com.jishi.reservation.controller.base;

/**
 * Created by liangxiong on 2017/11/28.
 */
public class BaseException extends Exception {

    private int codeNum = -1;
    private String codeName;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(int codeNum, String codeName) {
        this.codeNum = codeNum;
        this.codeName = codeName;
    }

    public BaseException(int codeNum, String codeName, String message) {
        super(message);
        this.codeNum = codeNum;
        this.codeName = codeName;
    }

    public BaseException(int codeNum, String codeName, String message, Throwable cause) {
        super(message, cause);
        this.codeNum = codeNum;
        this.codeName = codeName;
    }

    public BaseException(int codeNum, String codeName, String message, Throwable cause,
                         boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.codeNum = codeNum;
        this.codeName = codeName;
    }

  public int getCodeNum() {
        return codeNum;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
            "{codeNum=" + codeNum +
            ", codeName='" + codeName +
            "\', message='" + getMessage() +
            "\'}";
    }
}

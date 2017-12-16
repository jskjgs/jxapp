package com.jishi.reservation.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by wang on 16/1/31.
 */
public class Result<T> implements DataWrapper {
    protected boolean state = false;
    protected String message;
    protected T tempData;
    protected Integer code;

    /**
     * 取得包装器中的真实内容
     */
    @Override
    public T getData() {
        return tempData;
    }


    public static Result create() {
        Result result = new Result();
        return result;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 填充返回消息
     */
    public Result addMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 填充返回数据
     */
    public Result addData(T data) {
        tempData = data;
        return this;
    }

    /**
     * 任务执行成功并返回
     */
    public JSONObject ExeSuccess() {
        state = true;
        return this.toJSON();
    }

    /**
     * 任务执行失败并返回
     */
    public JSONObject ExeFaild() {
        state = false;
        return this.toJSON();
    }

    /**
     * 任务执行成功并返回
     */
    public JSONObject ExeSuccess(int statusCode) {
        state = true;
        code = statusCode;
        return this.toJSON();
    }

    /**
     * 带状态码返回
     */
    public JSONObject ExeFaild(int statusCode) {
        state = false;
        code = statusCode;
        return this.toJSON();
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return state;
    }

    /**
     * 返回json字符串信息
     */
    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    /**
     * 返回json对象信息(阿里的JsonObject)
     */
    public JSONObject toJSON() {
        return (JSONObject) JSONObject.toJSON(this);
    }

    public void getName() {
        System.out.println(this.getClass().getName());
    }

}

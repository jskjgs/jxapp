package com.jishi.reservation.worker.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

/**
 * Created by liangxiong on 2017/11/13.
 */
@Getter
public class PushData {
    private int msgType = -1;
    private Date date = new Date();
    private Object content;

    private PushData() {
        super();
    }

    public static PushData create() {
        return new PushData();
    }

    public PushData msgType(PushDataMsgTypeDef msgTypeDef) {
        this.msgType = msgTypeDef.getCode();
        return this;
    }

    public PushData content(Object content) {
        this.content = content;
        return this;
    }

    public String toJSON() {
        if (msgType == -1 || this.content == null) {
            return null;
        }
        return JSONObject.toJSONString(this);
    }

    public static enum PushDataMsgTypeDef {
        PUSH_DATA_REGISTER_INFO(1),     //挂号就诊时间提醒
        PUSH_DATA_OUTPATIENT_QUEUE(2);  //排队叫号提醒

        private int code;

        PushDataMsgTypeDef(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }
}

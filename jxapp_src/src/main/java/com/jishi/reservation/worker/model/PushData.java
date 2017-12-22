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
    private String msgName = "";
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
        this.msgName = msgTypeDef.name();
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

    public enum PushDataMsgTypeDef {

        PUSH_DATA_TYPE_REGISTER_TIME_INFO(0),   // 挂号就诊时间提醒
        PUSH_DATA_TYPE_REGISTER_SUCCESS(1),     // 挂号预约成功并缴费成功
        PUSH_DATA_TYPE_OUT_QUEUE_INFO(2),       // 排队叫号提醒
        PUSH_DATA_TYPE_OUT_UNPAID_DOC(3),       // 门诊有待支付缴费单
        PUSH_DATA_TYPE_OUT_PAY_COMPLETE(4),     // 门诊缴费单完成支付
        PUSH_DATA_TYPE_HOS_UNPAID_DOC(5),       // 住院有待结算缴费单
        PUSH_DATA_TYPE_HOS_PAY_COMPLETE(6),     // 住院缴费单完成结算
        PUSH_DATA_TYPE_HOS_PRE_PAY(7),          // 住院预交金额不足
        PUSH_DATA_TYPE_HOS_PRE_PAY_COMPLETE(8), // 住院预交金额不足
        PUSH_DATA_TYPE_APPOINTMENT_NOTICE(9),   // 复诊提醒
        PUSH_DATA_TYPE_UNKNOW(-1);

        private int code;

        PushDataMsgTypeDef(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }
}

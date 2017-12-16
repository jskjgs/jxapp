package com.jishi.reservation.service.pay.bean;

import com.jishi.reservation.service.pay.IHandle;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zbs on 2016/11/15.
 */
@Data
public class PayCallBackDataBean {
    //订单sn
    String orderSn;
    //支付金额
    BigDecimal amount;
    //第三方支付号
    String tradeNo;
    //支付方式标识
    String payType;
    //支付时间
    Date payTime;
    //执行列表
    List<IHandle> handleList;
}

package com.jishi.reservation.service.pay;

import com.jishi.reservation.service.pay.bean.PayDataBean;

/**
 * Created by zbs on 2017/10/10.
 */
public interface IHandle {

    boolean execution(PayDataBean payDataBean) throws Exception;
}

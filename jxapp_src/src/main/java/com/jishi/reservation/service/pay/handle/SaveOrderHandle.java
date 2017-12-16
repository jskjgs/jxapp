package com.jishi.reservation.service.pay.handle;

import com.jishi.reservation.service.pay.IHandle;
import com.jishi.reservation.service.pay.bean.PayDataBean;
import org.springframework.stereotype.Service;

/**
 * Created by zbs on 2017/10/10.
 */
@Service
public class SaveOrderHandle implements IHandle {

    @Override
    public boolean execution(PayDataBean payDataBean) throws Exception {
        return false;
    }
}

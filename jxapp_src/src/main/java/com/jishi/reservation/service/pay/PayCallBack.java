package com.jishi.reservation.service.pay;

import com.alibaba.fastjson.JSON;
import com.jishi.reservation.service.pay.bean.PayCallBackDataBean;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zbs on 2016/11/15.
 */
@Service
@Log4j
public class PayCallBack {

    public interface ResultListener<T> {

        T getDate();

        //构建对象
        PayCallBackDataBean getPayCallBackDataBean(T t) throws Exception;

        //API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
        boolean onFailByReturnCodeError(T t) throws Exception ;

        //API返回ReturnCode为失败状态，支付API系统返回失败，请检测Post给API的数据是否规范合法
        boolean onFailByReturnCodeFail(T t) throws Exception;

        //支付请求API返回的数据签名验证失败，有可能数据被篡改了
        boolean onFailBySignInvalid(T t)  throws Exception;

        //业务支付失败
        String onFail(T t) throws Exception;


        boolean business(T t , List<IHandle> handleList) throws Exception;

        //支付成功
        String onSuccess(T t)  throws Exception;

    }

    public String startPayCall(ResultListener resultListener, List<IHandle> handleList) throws Exception {
        log.info("支付API返回的数据如下:" + JSON.toJSONString(resultListener.getDate()));
        if (!resultListener.onFailByReturnCodeError(resultListener.getDate())) {
            log.error("【支付失败】支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
            return resultListener.onFail(resultListener.getDate());
        }
        if (!resultListener.onFailByReturnCodeFail(resultListener.getDate())) {
            log.error("【支付失败】支付API系统返回失败，请检测Post给API的数据是否规范合法");
            return resultListener.onFail(resultListener.getDate());
        }
        if (!resultListener.onFailBySignInvalid(resultListener.getDate())) {
            log.error("【支付失败】支付请求API返回的数据签名验证失败，有可能数据被篡改了");
            return resultListener.onFail(resultListener.getDate());
        }
        if(resultListener.business(resultListener.getDate(),handleList)) {
            log.info("【支付执行业务成功】返回成功信息");
            return resultListener.onSuccess(resultListener.getDate());
        }else {
            log.info("【支付执行业务失败】返回失败信息");
            return resultListener.onFail(resultListener.getDate());
        }
    }

}

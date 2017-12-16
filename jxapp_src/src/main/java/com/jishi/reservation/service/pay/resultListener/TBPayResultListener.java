package com.jishi.reservation.service.pay.resultListener;


import com.jishi.reservation.service.pay.PayCallBack;
import com.jishi.reservation.service.pay.bean.PayCallBackDataBean;
import com.jishi.reservation.service.pay.bean.PayDataBean;
import com.jishi.reservation.service.pay.bean.TBPaymentModel;
import com.jishi.reservation.service.pay.IHandle;

import java.util.List;

/**
 * Created by zbs on 2016/11/16.
 */
public class TBPayResultListener implements PayCallBack.ResultListener<TBPaymentModel> {

    private TBPaymentModel tbPaymentModel = null;

    public TBPayResultListener(TBPaymentModel tbPaymentModel) {
        this.tbPaymentModel = tbPaymentModel;
    }


    @Override
    public TBPaymentModel getDate() {
        return tbPaymentModel;
    }

    /**
     * 构建对象
     *
     * @param tbPaymentModel
     * @return
     * @throws Exception
     */
    @Override
    public PayCallBackDataBean getPayCallBackDataBean(TBPaymentModel tbPaymentModel) {
        return null;
    }

    /**
     * API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
     *
     * @param tbPaymentModel
     * @return
     */
    @Override
    public boolean onFailByReturnCodeError(TBPaymentModel tbPaymentModel) {
        return true;
    }

    /**
     * API返回ReturnCode为失败状态，支付API系统返回失败，请检测Post给API的数据是否规范合法
     *
     * @param tbPaymentModel
     * @return
     */
    @Override
    public boolean onFailByReturnCodeFail(TBPaymentModel tbPaymentModel) {
        if ("TRADE_SUCCESS".equals(tbPaymentModel.getTrade_status()))
            return true;
        if ("TRADE_FINISHED".equals(tbPaymentModel.getTrade_status()))
            return true;
        return false;
    }


    /**
     * 支付请求API返回的数据签名验证失败，有可能数据被篡改了
     *
     * @param tbPaymentModel
     * @return
     * @throws Exception
     */
    @Override
    public boolean onFailBySignInvalid(TBPaymentModel tbPaymentModel) {
        return true;
    }


    /**
     * 返回给支付宝系统失败标识
     *
     * @param tbPaymentModel
     * @return
     */
    @Override
    public String onFail(TBPaymentModel tbPaymentModel) {
        return "fail";
    }

    /**
     * 业务系统
     *
     * @param tbPaymentModel
     * @return
     * @throws Exception
     */
    @Override
    public boolean business(TBPaymentModel tbPaymentModel, List<IHandle> handleList) throws Exception {
        //如果返回的数据为空,或者需要执行的业务为空,都返回失败
        if (tbPaymentModel == null || handleList == null)
            return false;
        //把TBPaymentModel转换为PayDataBean todo:没做
        PayDataBean payDataBean = new PayDataBean();
        //执行业务
        for (IHandle handle : handleList) {
            if (!handle.execution(payDataBean))
                return false;
        }
        return true;
    }

    /**
     * 返回给支付宝系统成功标识
     *
     * @param tbPaymentModel
     * @return
     */
    @Override
    public String onSuccess(TBPaymentModel tbPaymentModel) {
        return "success";
    }
}

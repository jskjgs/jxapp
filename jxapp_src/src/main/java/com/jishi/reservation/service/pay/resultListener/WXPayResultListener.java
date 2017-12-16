package com.jishi.reservation.service.pay.resultListener;

import com.jishi.reservation.service.pay.PayCallBack;
import com.jishi.reservation.service.pay.bean.PayCallBackDataBean;
import com.jishi.reservation.service.pay.WXSignature;
import com.jishi.reservation.service.pay.bean.PayDataBean;
import com.jishi.reservation.service.pay.bean.WXPaymentModel;
import com.jishi.reservation.service.pay.IHandle;
import com.jishi.reservation.util.Constant;
import lombok.extern.log4j.Log4j;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zbs on 2016/11/16.
 */
@Log4j
public class WXPayResultListener implements PayCallBack.ResultListener<WXPaymentModel> {

    private WXPaymentModel wxPaymentModel = null;

    public WXPayResultListener(WXPaymentModel wxPaymentModel) {
        this.wxPaymentModel = wxPaymentModel;
    }

    @Override
    public WXPaymentModel getDate() {
        return wxPaymentModel;
    }

    /**
     * 构建对象
     * @param wxPaymentModel
     * @return
     * @throws Exception
     */
    @Override
    public PayCallBackDataBean getPayCallBackDataBean(WXPaymentModel wxPaymentModel) throws Exception {
        BigDecimal total = new BigDecimal(wxPaymentModel.getTotal_fee());
        BigDecimal amount = total.divide(new BigDecimal(100), 2, BigDecimal.ROUND_UP);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date payTime = sdf.parse(wxPaymentModel.getTime_end());
        PayCallBackDataBean payCallBackDataBean = new PayCallBackDataBean();
        payCallBackDataBean.setAmount(amount);
        payCallBackDataBean.setOrderSn(wxPaymentModel.getOut_trade_no());
        payCallBackDataBean.setPayTime(payTime);
        payCallBackDataBean.setTradeNo(wxPaymentModel.getTransaction_id());
        payCallBackDataBean.setPayType(Constant.ORDER_PAY_TYPE_WX);
        return payCallBackDataBean;
    }

    /**
     * API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
     * @param wxPaymentModel
     * @return
     */
    @Override
    public boolean onFailByReturnCodeError(WXPaymentModel wxPaymentModel) {
        if(wxPaymentModel != null){
            return true;
        }
        return false;
    }

    /**
     * API返回ReturnCode为失败状态，支付API系统返回失败，请检测Post给API的数据是否规范合法
     * @param wxPaymentModel
     * @return
     */
    @Override
    public boolean onFailByReturnCodeFail(WXPaymentModel wxPaymentModel) {
        if("SUCCESS".equals(wxPaymentModel.getResult_code()) && "SUCCESS".equals(wxPaymentModel.getResult_code())
                && wxPaymentModel.getTotal_fee() != null && wxPaymentModel.getTransaction_id() != null
                && wxPaymentModel.getOut_trade_no() != null && wxPaymentModel.getTime_end() != null
                && wxPaymentModel.getSign() != null){
             return true;
        }
        return false;
    }

    /**
     * 支付请求API返回的数据签名验证失败，有可能数据被篡改了
     * @param wxPaymentModel
     * @return
     * @throws Exception
     */
    @Override
    public boolean onFailBySignInvalid(WXPaymentModel wxPaymentModel) throws Exception {
        String sign = wxPaymentModel.getSign();
        Map map = wxPaymentModel.toMap();
        map.remove("sign");
        if(sign.equals(WXSignature.getSign(map))){
            return true;
        }
        return false;
    }

    /**
     * 返回给微信系统失败标识
     * @param wxPaymentModel
     * @return
     */
    @Override
    public String onFail(WXPaymentModel wxPaymentModel) {
        String req = ("<xml>" +
                "<return_code><![CDATA[FAIL]]></return_code>" +
                "<return_msg><![CDATA[fail]]></return_msg>" +
                "</xml>");
        return req;
    }

    /**
     * 业务系统
     * @param wxPaymentModel
     * @return
     * @throws Exception
     */
    @Override
    public boolean business(WXPaymentModel wxPaymentModel, List<IHandle> handleList) throws Exception {
        //如果返回的数据为空,或者需要执行的业务为空,都返回失败
        if(wxPaymentModel == null || handleList == null)
            return false;
        //把WXPaymentModel转换为PayDataBean todo:没做
        PayDataBean payDataBean = new PayDataBean();
        //执行业务
        for(IHandle handle : handleList){
            if(!handle.execution(payDataBean))
                return false;
        }
        return true;
    }

    /**
     * 返回给微信系统成功标识
     * @param wxPaymentModel
     * @return
     */
    @Override
    public String onSuccess(WXPaymentModel wxPaymentModel) {
        String req = ("<xml>" +
                "<return_code><![CDATA[SUCCESS]]></return_code>" +
                "<return_msg><![CDATA[ok]]></return_msg>" +
                "</xml>");
        return req;
    }
}

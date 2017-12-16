package com.jishi.reservation.controller.base.filter.user;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 支付接口登录验证，排除支付回调
 * Created by liangxiong on 2017/12/15.
 */
@WebFilter(filterName = "PayLoginFilter", urlPatterns = {"/reservation/pay/*"},
      //添加不进行登录验证的url，可以为实际路径或正则表达式，以','分隔
      initParams = {@WebInitParam(name = VerifyLoginFilter.EXCLUDED_PAGES, value=".*/pay/aliPayCallBack,.*/pay/wxPayCallBack")})
public class PayLoginFilter extends VerifyLoginFilter {
}

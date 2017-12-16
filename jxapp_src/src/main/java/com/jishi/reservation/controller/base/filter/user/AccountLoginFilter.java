package com.jishi.reservation.controller.base.filter.user;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * Created by liangxiong on 2017/11/21.
 */
@WebFilter(filterName = "AccountLoginFilter", urlPatterns = {"/reservation/account/*"},
  //添加不进行登录验证的url，可以为实际路径或正则表达式，以','分隔
  initParams = {@WebInitParam(name = VerifyLoginFilter.EXCLUDED_PAGES, value=".*/account/sendDynamicCode,.*/account/loginOrRegisterThroughPhone")})
public class AccountLoginFilter extends VerifyLoginFilter {
}

package com.jishi.reservation.controller.base.filter.user;

import com.doraemon.base.controller.Result;
import com.jishi.reservation.controller.base.filter.BaseFilter;
import com.jishi.reservation.service.AccountService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户登录验证父类
 * Created by liangxiong on 2017/11/20.
 */
public class VerifyLoginFilter extends BaseFilter {

    private Logger logger = LoggerFactory.getLogger(VerifyLoginFilter.class);

    @Autowired
    private AccountService accountService;


    @Override
    public void executeFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Long accountId = null;
        try {
            accountId = accountService.returnIdByToken((HttpServletRequest) request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("*******验证失败，服务器异常*******");
            returnFailed(response, ReturnCodeEnum.ERR);
        }

        if (accountId == null || accountId.equals(Constant.NOT_LOGIN_ACCOUNT_ID)) {
            Long testAccount = getTestAccountId(request);
            if (testAccount == null || testAccount.equals(Constant.NOT_LOGIN_ACCOUNT_ID)) {
                logger.info("*******验证失败，未登录*******");
                returnFailed(response, ReturnCodeEnum.NOT_LOGIN);
                return;
            } else {
                accountId = testAccount;
                logger.info("[ 走测试用户: testAccount:" + testAccount + " ]");
            }
        }

        logger.info("*******登录验证成功，登录用户: " + accountId + "  uri：" + ((HttpServletRequest) request).getRequestURI());
        request.setAttribute(Constant.ATTR_LOGIN_ACCOUNT_ID, accountId);
        filterChain.doFilter(request, response);
    }

    private void returnFailed(ServletResponse response, ReturnCodeEnum rslt) throws IOException {
        Result result = new Result();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(result.addMessage(rslt.getDesc()).ExeFaild(rslt.getCode()));
    }

    //获取测试账号
    private Long getTestAccountId(ServletRequest request) {
        Object testAccountObj = ((HttpServletRequest)request).getHeader(Constant.HEADER_TEST_ACCOUNT_ID);
        Long testAccount = -1L;
        if (testAccountObj != null) {
            if (testAccountObj instanceof Long) {
                testAccount = (Long) testAccountObj;
            } else if (testAccountObj instanceof String) {
                try {
                    testAccount = Long.parseLong((String) testAccountObj);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        if (Constant.TEST_ACCOUNT_ID_LIST.indexOf(testAccount) > -1) {
            return testAccount;
        }
        return Constant.NOT_LOGIN_ACCOUNT_ID;
    }

}

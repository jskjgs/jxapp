package com.jishi.reservation.controller.base.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用拦截器
 * Created by liangxiong on 2017/11/22.
 */
public abstract class BaseFilter implements Filter {

    public static final String EXCLUDED_PAGES = "excludedPages";
    private String excludedPages;
    private String[] excludedPageArray = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludedPages = filterConfig.getInitParameter(EXCLUDED_PAGES);
        if (null != excludedPages && excludedPages.length() != 0) { // 例外页面不为空
            excludedPageArray = excludedPages.split(String.valueOf(','));
        }
    }

    //使用executeFilter
    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (isExclude(((HttpServletRequest) request).getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            executeFilter(request, response, filterChain);
        }
    }

    // 子类实现该方法进行过滤
    public abstract void executeFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException;

    @Override
    public void destroy() {
        excludedPages = null;
        excludedPageArray = null;
    }


    //是否排除验证，在该类urlPatterns所匹配的url中进行排除，可以为实际路径或正则表达式，以','分隔
    public boolean isExclude(String path) {
        if (excludedPages == null || excludedPages.isEmpty()) {
            return false;
        }
        if (excludedPages.contains(path)) {
            return true;
        }
        Pattern pattern;
        for (String page : excludedPageArray) {// 遍历例外url数组
            pattern = Pattern.compile(page);
            Matcher matcher = pattern.matcher(path);
            if(matcher.matches()){
                return true;
            }
        }
        return false;
    }

}

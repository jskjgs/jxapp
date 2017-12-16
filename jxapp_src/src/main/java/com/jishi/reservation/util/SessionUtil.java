package com.jishi.reservation.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sloan on 2017/6/1.
 */
public class SessionUtil {


    /**
     * 添加属性到session
     * @param request
     * @param name  session名字
     * @param value session值
     */
    public static void addSession(HttpServletRequest request, String name, String value){

        request.getSession().setAttribute(name,value);
    }



    /**
     * 根据属性名从session中获取值
     * @param request
     * @param name  session名字
     */
    public static String getSession(HttpServletRequest request, String name){

        return  (String) request.getSession().getAttribute(name);
    }



    /**
     * 根据属性名删除session中的值
     * @param request
     * @param name
     */
    public static void deleteSession(HttpServletRequest request,String name){
        request.getSession().removeAttribute(name);
    }


}

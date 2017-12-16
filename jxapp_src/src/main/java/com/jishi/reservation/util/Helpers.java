package com.jishi.reservation.util;

import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.exception.BussinessException;

import java.util.Collection;
import java.util.Map;

/**
 * Created by zbs on 2017/8/9.
 */
public class Helpers {


    /**
     * 断言指定的对象不为空，抛出业务异常，在baseController中进行处理，给客户端返回统一业务码
     *
     * @param o   待检测对象
     * @param <T> 待检测对象类型
     * @return 如果对象不为空则返回
     */
    public static <T> void assertNotNull(T o, ReturnCodeEnum r) throws BussinessException {
        if (o == null) {
            throw new BussinessException(r);
        }
    }

    public static void assertTrue(boolean o, ReturnCodeEnum r) throws BussinessException {
        if (!o) {
            throw new BussinessException(r);
        }
    }

    public static void assertFalse(boolean o, ReturnCodeEnum r) throws BussinessException {
        if (o) {
            throw new BussinessException(r);
        }
    }

    public static <T> void assertNotNullOrEmpty(ReturnCodeEnum r, T ... args) throws BussinessException {
        for(T o : args) {
            if (o == null) throw new BussinessException(r);
            if (o instanceof String && ((String) o).isEmpty()) throw new BussinessException(r);
            if (o instanceof Collection && ((Collection) o).isEmpty()) throw new BussinessException(r);
            if (o instanceof Map && ((Map) o).isEmpty()) throw new BussinessException(r);
        }
    }

    /**
     * 断言集合不为空
     *
     * @param o
     * @param <T>
     * @return
     */
    public static <T extends Collection<?>> void assertNotNullOrEmpty(T o, ReturnCodeEnum r) throws BussinessException {
        if (o == null || o.isEmpty()) {
            throw new BussinessException(r);
        }
    }

    /**
     * 断言Map不为空
     *
     * @param o
     * @param <T>
     * @return
     */
    public static <T extends Map<?, ?>> void assertNotNullOrEmpty(T o, ReturnCodeEnum r) throws BussinessException {
        if (o == null || o.isEmpty()) {
            throw new BussinessException(r);
        }
    }

    /**
     * 断言字符串不为空
     *
     * @param o
     * @return
     */
    public static void assertNotNullOrEmpty(String o, ReturnCodeEnum r) throws BussinessException {
        if (o == null || o.length() == 0) {
            throw new BussinessException(r);
        }
    }

    /**
     * 如果目标值为空， 则使用默认值
     *
     * @param value
     * @param defaultValue 默认值
     * @param <T>          类型
     * @return 目标值 或者 默认值
     */
    public static <T> T getDefaultValueIfNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNullOrEmpty(String o) {
        return o == null || o.length() == 0;
    }

    public static boolean isNullOrEmpty(Object... o) {
        return o == null || o.length == 0;
    }

    public static boolean isNullOrEmpty(Collection<?> o) {
        return o == null || o.isEmpty();
    }

    public static boolean isNullOrEmpty(Map<?, ?> o) {
        return o == null || o.isEmpty();
    }

    /**
     * 比较两对象
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T extends Comparable> int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }

    /**
     * 取两对象中较小的
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T extends Comparable> T minOf(T o1, T o2) {
        return compare(o1, o2) < 0 ? o1 : o2;
    }

    /**
     * 取两对象中较大的
     *
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    public static <T extends Comparable> T maxOf(T o1, T o2) {
        return compare(o1, o2) > 0 ? o1 : o2;
    }
}

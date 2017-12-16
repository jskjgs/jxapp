package com.jishi.reservation.otherService.im;


/**
 * Created by liangxiong on 2017/10/25.
 */
public class HttpParam {
    private StringBuffer stringBuffer = new StringBuffer();

    public HttpParam add(String name, Object value) throws IMException {
        if (name == null || name.length() == 0) {
            throw new IMException("参数不能为 null 或者 empty");
        }
        stringBuffer.append("&");
        stringBuffer.append(name);
        stringBuffer.append("=");
        if (value != null) {
            stringBuffer.append(value);
        }
        return this;
    }

    public HttpParam add(String name, String value, int length) throws IMException {
        if (length > 0) {
            if (value != null && value.length() > length) {
                throw new IMException("参数\"" + name + "\"的值\"" + value + "\"超过规定长度：" + length);
            }
        }
        return add(name, value);
    }

    public boolean hasParam() {
        return !stringBuffer.toString().isEmpty();
    }

    @Override
    public String toString() {
        String params = stringBuffer.toString();
        if (params.indexOf("&") == 0) {
            params = params.substring(1, params.length());
        }
        return params;
    }
}

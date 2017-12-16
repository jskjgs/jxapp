package com.jishi.reservation.otherService.im.neteasy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.otherService.im.IMException;

import java.util.List;

/**
 * Created by liangxiong on 2017/11/7.
 */
public class JsonUtil {

    public static final String RES_DATA_CODE = "code";
    public static final String RES_DATA_KEY_DESC = "desc";

    public static <T> T parseObject(String url, String jsonStr, String dataKey, Class<T> clazz) throws IMException {
        JSONObject object = JSONObject.parseObject(jsonStr);
        int code = Integer.parseInt(object.getString(RES_DATA_CODE));
        if (code != 200) {
            throw new IMException(code, url, object.getString(RES_DATA_KEY_DESC));
        }
        JSONObject data = object.getJSONObject(dataKey);
        return data.toJavaObject(clazz);
    }

    public static <T> List<T> parseObjectList(String url, String jsonStr, String dataKey, Class<T> clazz) throws IMException {
        JSONObject object = JSONObject.parseObject(jsonStr);
        int code = Integer.parseInt(object.getString(RES_DATA_CODE));
        if (code != 200) {
            throw new IMException(code, url, object.getString(RES_DATA_KEY_DESC));
        }
        String array = object.getString(dataKey);
        return JSON.parseArray(array, clazz);
    }

}

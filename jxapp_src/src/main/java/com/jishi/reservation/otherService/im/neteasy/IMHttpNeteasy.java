package com.jishi.reservation.otherService.im.neteasy;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.otherService.im.HttpParam;
import com.jishi.reservation.otherService.im.IMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;


/**
 * Created by liangxiong on 2017/10/24.
 */
public class IMHttpNeteasy {
    private static final String IM_NETEASY_URL = "https://api.netease.im/nimserver";
    private static final int IM_NETEASY_CONNET_TIMEOUT = 30000;
    private static final int IM_NETEASY_READ_TIMEOUT = 30000;
    private static final String IM_NETEASY_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String IM_NETEASY_HTTP_METHOD = "POST";


    private String appKey;//App-Key
    private String appSecret;//App-Secret

    private Logger logger = LoggerFactory.getLogger(IMHttpNeteasy.class);

    public IMHttpNeteasy(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public String doPost(String serviceName, HttpParam httpParam) throws Exception {
        URL url = new URL(IM_NETEASY_URL + serviceName);
        HttpURLConnection conn = initConnection((HttpURLConnection) url.openConnection());
        logger.debug(conn.toString() + "  参数：" + httpParam);

        if (httpParam != null && httpParam.hasParam()) {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            try {
                out.write(httpParam.toString().getBytes("utf-8"));
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new IMException(url.toString() + ": 写入body参数失败，param：" + httpParam.toString());
            } finally {
                out.close();
            }
        }

        return returnResult(conn, url);
    }

    private HttpURLConnection initConnection(HttpURLConnection conn) throws Exception {
        String nonce = String.valueOf(Math.random() * 1000000);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        conn.setRequestProperty("AppKey", appKey);
        conn.setRequestProperty("Nonce", nonce);
        conn.setRequestProperty("CurTime", curTime);
        conn.setRequestProperty("CheckSum", getCheckSum(appSecret, nonce, curTime));

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod(IM_NETEASY_HTTP_METHOD);
        conn.setConnectTimeout(IM_NETEASY_CONNET_TIMEOUT);
        conn.setReadTimeout(IM_NETEASY_READ_TIMEOUT);
        conn.setRequestProperty("Content-Type", IM_NETEASY_CONTENT_TYPE);
        return conn;
    }

    private byte[] readInputStream(InputStream inStream, URL url) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data;
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IMException(url.toString() + ": 读取数据失败!");
        } finally {
            outStream.close();
            inStream.close();
        }
        return data;
    }

    private String returnResult(HttpURLConnection conn, URL url) throws Exception {
        InputStream input = null;
        int code = conn.getResponseCode();
        if (code == 200) {
            input = conn.getInputStream();
        } else {
            logger.error(url.toString() + " code: " + code);
            throw new IMException(code, url.toString(), "网络错误");
        }
        String result = new String(readInputStream(input, url), "UTF-8");
        logger.debug(result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer rsltCode = jsonObject.getInteger("code");
        if (rsltCode == null || rsltCode != 200) {
            String desc = jsonObject.getString("desc");
            throw new IMException(rsltCode, url.toString(), desc == null ? result : desc);
        }
        return result;
    }

    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}

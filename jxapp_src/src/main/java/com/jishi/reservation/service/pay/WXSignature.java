package com.jishi.reservation.service.pay;

import com.doraemon.base.util.MD5Encryption;
import com.doraemon.base.util.xml.XMLParser;
import lombok.extern.log4j.Log4j;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zbs on 2017/9/19.
 */
@Log4j
public class WXSignature {

    final static String key = "WXHOTR1383916102WXHOTR1383916102";

    public WXSignature() {
    }

    public static String getSign(Object o) throws IllegalAccessException, NoSuchAlgorithmException {
        ArrayList list = new ArrayList();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        Field[] size = fields;
        int arrayToSort = fields.length;

        for (int sb = 0; sb < arrayToSort; ++sb) {
            Field result = size[sb];
            result.setAccessible(true);
            if (result.get(o) != null && result.get(o) != "") {
                list.add(result.getName() + "=" + result.get(o) + "&");
            }
        }

        int var8 = list.size();
        String[] var9 = (String[]) list.toArray(new String[var8]);
        Arrays.sort(var9, String.CASE_INSENSITIVE_ORDER);
        StringBuilder var10 = new StringBuilder();

        for (int var11 = 0; var11 < var8; ++var11) {
            var10.append(var9[var11]);
        }

        String var12 = var10.toString();
        var12 = var12 + "key=" + key;
        var12 = MD5Encryption.getMD5(var12).toUpperCase();
        return var12;
    }

    public static String getSign(Map<String, Object> map) throws NoSuchAlgorithmException {
        ArrayList list = new ArrayList();
        Iterator size = map.entrySet().iterator();

        while (size.hasNext()) {
            Map.Entry arrayToSort = (Map.Entry) size.next();
            if (arrayToSort.getValue() != "") {
                list.add((String) arrayToSort.getKey() + "=" + arrayToSort.getValue() + "&");
            }
        }

        int var6 = list.size();
        String[] var7 = (String[]) list.toArray(new String[var6]);
        Arrays.sort(var7, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();

        for (int result = 0; result < var6; ++result) {
            sb.append(var7[result]);
        }

        String var8 = sb.toString();
        var8 = var8 + "key=WXHOTR1383916102WXHOTR1383916102";
        log.info("Sign Before MD5:" + var8);
        var8 = MD5Encryption.getMD5(var8).toUpperCase();
        log.info("Sign Result:" + var8);
        return var8;
    }

    public static String getSignFromResponseString(String responseString) throws IOException, SAXException, ParserConfigurationException, NoSuchAlgorithmException {
        Map map = XMLParser.getMapFromXML(responseString);
        map.put("sign", "");
        return getSign(map);
    }

    public static boolean checkIsSignValidFromResponseString(String responseString) throws ParserConfigurationException, IOException, SAXException, NoSuchAlgorithmException {
        Map map = XMLParser.getMapFromXML(responseString);
        log.info(map.toString());
        String signFromAPIResponse = map.get("sign").toString();
        if (signFromAPIResponse != "" && signFromAPIResponse != null) {
            log.info("服务器回包里面的签名是:" + signFromAPIResponse);
            map.put("sign", "");
            String signForAPIResponse = getSign(map);
            if (!signForAPIResponse.equals(signFromAPIResponse)) {
                log.error("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
                return false;
            } else {
                log.info("恭喜，API返回的数据签名验证通过!!!");
                return true;
            }
        } else {
            log.error("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
            return false;
        }
    }
}

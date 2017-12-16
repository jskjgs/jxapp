package com.jishi.reservation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liangxiong on 2017/11/10.
 */
public class NetUtil {

    private static final String URL_CHINAZ = "http://ip.chinaz.com";
    private static final String URL_IPCN = "http://www.ip.cn";
    private static final String REXP_CHINAZ = "\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>";
    private static final String REXP_IPCN = "(((1[0-9][0-9])|(2[0-4][0-9])|(25[0-5])|([1-9][0-9])|([0-9]))\\.){3}((1[0-9][0-9])|(2[0-4][0-9])|(25[0-5])|([1-9][0-9]|[0-9]))";

    public static String getLocalIP() throws Exception {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements())
        {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address)
                {
                    if (!"127.0.0.1".equals(ip.getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        }
        return null;
    }

    public static String getV4IP() throws Exception {
        String ipstr = null;

        String page = getWebPageContent(URL_IPCN);
        Pattern p = Pattern.compile(REXP_IPCN);
        Matcher m = p.matcher(page.toString());
        while (m.find()) {
            //ipstr = m.group(1);
            ipstr = m.group(0);
        }
        return ipstr;
    }

    public static String getWebPageContent(String urlStr) {
        BufferedReader in = null;
        StringBuffer sb = null;
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "-1";
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException ex1) {
              }
            }
        }
        return sb.toString();
    }
}

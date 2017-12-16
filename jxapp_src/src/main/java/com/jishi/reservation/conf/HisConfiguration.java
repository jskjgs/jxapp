package com.jishi.reservation.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liangxiong on 2017/12/15.
 */
@Configuration
public class HisConfiguration {

    @Value("${his.host}")
    private String host;

    @Value("${his.port}")
    private String port;

    @Value("${his.token}")
    private String token;

    @Value("${his.key}")
    private String key;


    private static final String ROOT_PATH = "/ExternalServices/ZL_InformationService.asmx";

    public String getHisBaseUrl() {
        if (port == null || port.isEmpty() || "80".equals(port)) {
            return host + ROOT_PATH;
        }
        return host + ":" + port + ROOT_PATH;
    }

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }
}

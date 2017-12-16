package com.jishi.reservation.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liangxiong on 2017/12/15.
 */
@Configuration
public class PayConfiguration {
    @Value("${pay-callback.host}")
    private String host;

    @Value("${pay-callback.port}")
    private String port;

    @Value("${server.servlet-path}")
    private String servletPath;

    public String getPayCallbackBaseUrl() {
        if (port == null || port.isEmpty() || "80".equals(port)) {
            return host + servletPath;
        }
        return host + ":" + port + servletPath;
    }
}

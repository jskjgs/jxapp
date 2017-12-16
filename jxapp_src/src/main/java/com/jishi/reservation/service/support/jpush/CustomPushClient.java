package com.jishi.reservation.service.support.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.HttpProxy;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.utils.Preconditions;
import cn.jpush.api.push.model.PushPayload;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by liangxiong on 2017/11/23.
 */
@Slf4j
public class CustomPushClient {

    private NettyHttpClient _httpClient;
    private String _baseUrl;
    private String _pushPath;
    private int _apnsProduction;
    private long _timeToLive;


    private CustomPushClient(String appKey, String masterSecret, HttpProxy proxy, ClientConfig conf) {
        super();
        ServiceHelper.checkBasic(appKey, masterSecret);
        this._baseUrl = (String)conf.get("push.host.name");
        this._pushPath = (String)conf.get("push.path");
        this._apnsProduction = ((Integer)conf.get("apns.production")).intValue();
        this._timeToLive = ((Long)conf.get("time.to.live")).longValue();
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);

        URI uri = null;
        try {
          uri = new URI(this._baseUrl + this._pushPath);
        } catch (java.lang.Exception e) {
          e.printStackTrace();
        }
        this._httpClient = new com.jishi.reservation.service.support.jpush.NettyHttpClient(authCode, proxy, conf, uri, null);
    }

    public static CustomPushClient newInstance(String appKey, String masterSecret) {
        return new CustomPushClient(appKey, masterSecret, (HttpProxy)null, ClientConfig.getInstance());
    }

    public void sendRequest(PushPayload pushPayload) throws APIConnectionException, APIRequestException, URISyntaxException {
      Preconditions.checkArgument(null != pushPayload, "pushPayload should not be null");
      if (this._apnsProduction > 0) {
          pushPayload.resetOptionsApnsProduction(true);
      } else if (this._apnsProduction == 0) {
          pushPayload.resetOptionsApnsProduction(false);
      }

      if (this._timeToLive >= 0L) {
          pushPayload.resetOptionsTimeToLive(this._timeToLive);
      }
      this._httpClient.sendRequest(HttpMethod.POST, pushPayload.toString());
    }

    public void close() {
        if (this._httpClient != null) {
            this._httpClient.close();
        }
    }
}

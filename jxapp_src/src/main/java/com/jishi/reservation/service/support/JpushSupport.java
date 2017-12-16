package com.jishi.reservation.service.support;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.IHttpClient;
import cn.jiguang.common.connection.NettyHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.jishi.reservation.service.support.jpush.CustomPushClient;
import com.jishi.reservation.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sloan on 2017/9/23.
 */

@Slf4j
@Service
public class JpushSupport {

    private CustomPushClient pushClient = null;

    public static PushPayload buildPushObjMessage(String pushId,String message) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(pushId))
                .setMessage(Message.newBuilder()
                        .setMsgContent(message)
                        .addExtra("from", "JPush")
                        .build())
                .setOptions(Options.newBuilder()
                    .setApnsProduction(true)
                    .build())
                .build();

    }

    public static PushPayload buildPushObjNotifycation(String pushId, String alert, String extra) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("data", extra);
        return PushPayload.newBuilder()
                  .setPlatform(Platform.all())
                  .setAudience(Audience.alias(pushId))
                  .setNotification(Notification.newBuilder()
                      .setAlert(alert)
                      .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtras(map).build())
                      .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtras(map).build())
                      .build())
                  .setOptions(Options.newBuilder()
                      .setApnsProduction(true)
                      .build())
                  .build();
    }

    /**
     * @description 异步推送通知
     * @param pushId 推送id
     * @param message 消息内容
     * @param extra 消息内容JSON
     **/
    public void sendNotification(String pushId, String message, String extra){
        PushPayload payload = buildPushObjNotifycation(pushId, message, extra);
        sendPushAsyn(payload);
    }

    /**
     * @description 异步推送消息
     * @param pushId 推送id
     * @param message 消息内容
     **/
    public void sendPush(String pushId,String message){
        PushPayload payload = buildPushObjMessage(pushId,message);
        sendPushAsyn(payload);
    }

    /**
     * @description 异步批量推送
     * @param pushPayloadList 推送数据列表
     **/
    public void sendPush(List<PushPayload> pushPayloadList) {
        if (pushPayloadList == null || pushPayloadList.isEmpty()) {
            return;
        }
        for (PushPayload payload : pushPayloadList) {
            sendPushAsyn(payload);
        }
    }

    /**
     * @description 异步单个推送
     * @param payload 推送数据
    **/
    public void sendPushAsyn(PushPayload payload){
        //JPushClient jpushClient = new JPushClient(Constant.JPush_MASTER_SECRET, Constant.JPush_Appkey, null, ClientConfig.getInstance());
        try {
            checkPushClient();
            pushClient.sendRequest(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error, should retry later. ");
        } catch (APIRequestException e) {
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Send Message: " + payload.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Send Message: " + payload.toString());
        } catch (Throwable e) {
            e.printStackTrace();
            log.info("Unknow Error: " + payload.toString());
        }
    }

    private void checkPushClient() {
        if (pushClient == null) {
            synchronized (this) {
                if (pushClient == null) {
                    pushClient = CustomPushClient.newInstance(Constant.JPush_Appkey, Constant.JPush_MASTER_SECRET);
                }
            }
        }
    }

}

package com.jishi.reservation.otherService.im.neteasy;

import com.jishi.reservation.otherService.im.neteasy.operation.*;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liangxiong on 2017/10/25.
 */
@Data
public class IMClientNeteasy {

    private UserOperation userOperation;
    private FriendOperation friendOperation;
    private TeamOperation teamOperation;
    private MessageOperation messageOperation;
    private PushOperation pushOperation;
    private SystemOperation systemOperation;

    private IMClientNeteasy(String appKey, String appSecret) {
        IMHttpNeteasy imHttpNeteasy = new IMHttpNeteasy(appKey, appSecret);
        userOperation = new UserOperation(imHttpNeteasy);
        friendOperation = new FriendOperation(imHttpNeteasy);
        teamOperation = new TeamOperation(imHttpNeteasy);
        messageOperation = new MessageOperation(imHttpNeteasy);
        pushOperation = new PushOperation(imHttpNeteasy);
        systemOperation = new SystemOperation(imHttpNeteasy);
    }

    private static ConcurrentHashMap<String, IMClientNeteasy> imClientNeteasyMap = new ConcurrentHashMap<String,IMClientNeteasy>();

    public static IMClientNeteasy getInstance(String appKey, String appSecret) {
        if (imClientNeteasyMap.get(appKey) == null) {
            synchronized (IMClientNeteasy.class) {
                if (imClientNeteasyMap.get(appKey) == null) {
                    imClientNeteasyMap.putIfAbsent(appKey, new IMClientNeteasy(appKey, appSecret));
                }
            }
        }
        return imClientNeteasyMap.get(appKey);
    }
}

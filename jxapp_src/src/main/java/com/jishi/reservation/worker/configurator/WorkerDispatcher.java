package com.jishi.reservation.worker.configurator;

import com.doraemon.base.redis.RedisOperation;
import com.jishi.reservation.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangxiong on 2017/12/26.
 */
@Slf4j
@Component
public class WorkerDispatcher {

    @Autowired
    private RedisOperation redisOperation;

    public boolean hasPermission(WorkerTypeEnum type) throws Exception {
        final String name = type.name();
        final String ip = NetUtil.getLocalIP();
        final String keyIp = name + "_ip";

        log.info(type.name() + " 申请获取执行权限，本机ip：" + ip + " 超时时间：" + type.getExpireTime());

        boolean canRun = false;
        String valueIp = redisOperation.usePool().get(keyIp);
        if (valueIp == null || valueIp.isEmpty()) {
            log.info(type.name() + "任务已超时，" + ip + " 准备获取权限");
            canRun = doGetPermission(keyIp, ip, type);
        } else {
            canRun = valueIp.equals(ip);
        }
        log.info(type.name() + " 执行权限：" + ip + "    " + canRun);
        return canRun;
    }


    private boolean doGetPermission(String keyIp, String ip, WorkerTypeEnum type) throws Exception {
        boolean permission = false;
        List<String> keys = new ArrayList<String>();
        keys.add(keyIp);
        keys.add(ip);
        try {
            String ADD_KEY_IP_NX = " return redis.call('set',KEYS[1],KEYS[2],'ex'," + type.getExpireTime() + " ,'nx'); ";
            Object obj = redisOperation.usePool().eval(ADD_KEY_IP_NX, keys, new ArrayList<String>());
            if (obj != null && obj instanceof String) {
                permission = String.valueOf(obj).toUpperCase().equals("OK");
            } else if  (obj != null && obj instanceof Integer) {
                Integer rslt = Integer.valueOf(String.valueOf(obj));
                permission = rslt > 0;
            }
        } catch (Exception e) {
            log.info("获取worker权限失败");
            e.printStackTrace();
        }
        return permission;
    }

}

package com.jishi.reservation.service.his;

import com.doraemon.base.redis.RedisOperation;
import com.jishi.reservation.TestApplication;
import com.jishi.reservation.service.his.util.HisMedicalCardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbs on 2017/10/10.
 */
public class HisUserManagerTest extends TestApplication {

    @Autowired
    private HisUserManager hisUserManager;

    @Autowired
    private RedisOperation redisOperation;


    @Test
    public void addUserInfoTest() throws Exception {
        System.out.println(hisUserManager.addUserInfo("513823198408130042","身份证","周彬杉","13678113250"));
    }

    @Test
    public void getUserInfoByCodeTest() throws Exception {
        System.out.println(hisUserManager.getUserInfoByCode("513823198408130042","身份证"));
    }

    @Test
    public void getUserInfoByRegNO() throws Exception {
        System.out.println(hisUserManager.getUserInfoByRegNO("420324199407035384",
                HisMedicalCardType.ID_CARD.getCardType(), "天亮", "12345678901234", HisMedicalCardType.MEDICAL_CARD.getCardType()));
    }

    @Test
    public void test() throws Exception {
        List<String> keys = new ArrayList<String>();
        keys.add("test_nx");
        keys.add("test_nx");
        String ADD_KEY_IP_NX = " return redis.call('set',KEYS[1],KEYS[2],'ex'," + 30 * 5 + " ,'nx'); ";
        Object rslt = redisOperation.usePool().eval(ADD_KEY_IP_NX, keys, new ArrayList<String>());
        System.out.println("redis 返回结果" + rslt);
        System.out.println("redis 返回值" + redisOperation.usePool().get("test_nx"));
        rslt = redisOperation.usePool().eval(ADD_KEY_IP_NX, keys, new ArrayList<String>());
        System.out.println("redis 返回结果" + rslt);
    }
}

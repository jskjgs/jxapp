package com.jishi.reservation.util;

import com.doraemon.base.redis.RedisOperation;
import com.jishi.reservation.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zbs on 2017/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class RedisTest {

    @Autowired
    RedisOperation redisOperation;

    @Test
    public void redis() throws Exception {
        redisOperation.usePool().get("aaa");
        redisOperation.usePool().set("constant.dynamic_code_key.login_or_register_13678113250","bbbb");
        System.out.println("插入的值为:" + redisOperation.get("constant.dynamic_code_key.login_or_register_13678113250"));
    }

}

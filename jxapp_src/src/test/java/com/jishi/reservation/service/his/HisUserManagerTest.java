package com.jishi.reservation.service.his;

import org.testng.annotations.Test;

/**
 * Created by zbs on 2017/10/10.
 */
public class HisUserManagerTest {

    /**
     *
     * @throws Exception
     */
    @Test
    public void getUserInfoByCodeTest() throws Exception {
        HisUserManager hisUserManager = new HisUserManager();
        System.out.println(hisUserManager.getUserInfoByCode("513823198408130042","身份证"));
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void addUserInfoTest() throws Exception {
        HisUserManager hisUserManager = new HisUserManager();
        System.out.println(hisUserManager.addUserInfo("513823198408130042","身份证","周彬杉","13678113250"));
    }
}

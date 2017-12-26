package com.jishi.reservation.service.his;

import com.jishi.reservation.Main;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.annotations.Test;

/**
 * Created by liangxiong on 2017/12/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class HisHospitalizationTest {

    @Autowired
    private HisHospitalization hisHospitalization;

    private String brid = "15467";
    private String zycs = "1";

    @Test
    public void selectDepositBalance() throws Exception {
        System.out.println(hisHospitalization.selectDepositBalance(brid, zycs));
    }
    @Test
    public void selectDepositBalanceLog() throws Exception {
        System.out.println(hisHospitalization.selectDepositBalanceLog(brid));
    }
    @Test
    public void selectTotalPayDetail() throws Exception {
        System.out.println(hisHospitalization.selectTotalPayDetail(brid, zycs, "1"));
    }
    @Test
    public void selectDailyPayDetail() throws Exception {
        // TODO RQ
        System.out.println(hisHospitalization.selectDailyPayDetail(brid,"7", zycs));
    }
    @Test
    public void selectDetail() throws Exception {
        System.out.println(hisHospitalization.selectDetail(brid, zycs));
    }
    @Test
    public void selectHistoryDetail() throws Exception {
        System.out.println(hisHospitalization.selectHistoryDetail(brid, "1","10", ""));
    }
    @Test
    public void pay() throws Exception {
        // TODO  住院预交费
        //System.out.println(hisHospitalization.pay());
    }
    @Test
    public void testInformationDetailQuery() throws Exception {
        System.out.println(hisHospitalization.testInformationDetailQuery(brid, zycs));
    }
}

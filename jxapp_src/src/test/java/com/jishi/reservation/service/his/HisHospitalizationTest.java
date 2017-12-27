package com.jishi.reservation.service.his;

import com.jishi.reservation.TestApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liangxiong on 2017/12/26.
 */
public class HisHospitalizationTest extends TestApplication {

    @Autowired
     HisHospitalization hisHospitalization;

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

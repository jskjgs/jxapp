package com.jishi.reservation.service.his;
import com.jishi.reservation.service.his.bean.ConfirmRegister;
import org.testng.annotations.Test;

/**
 * Created by zbs on 2017/10/6.
 */

public class HisOutpatientTest {


    /**
     * 获取缴费单据信息
     * @throws Exception
     */
    @Test
    public void queryPayReceiptTest() throws Exception {
        HisOutpatient hisOutpatient = new HisOutpatient();
        System.out.println(hisOutpatient.queryPayReceipt("215","",String.valueOf(500),"1"));
    }


    /**
     * 获取挂号号源
     * @throws Exception
     */
    @Test
    public void queryRegisteredNumberTest() throws Exception {
        HisOutpatient hisOutpatient = new HisOutpatient();
        System.out.println(hisOutpatient.queryRegisteredNumber("","","","32","","","",""));
    }

    /**
     *	挂号
     * @throws Exception
     */
    @Test
    public void confirmRegisterTest() throws Exception {
        HisOutpatient hisOutpatient = new HisOutpatient();
        ConfirmRegister confirmRegister = new ConfirmRegister();
        confirmRegister.setBrid("226");
        confirmRegister.setBrlx("医保病人");
        confirmRegister.setCzjlid("");
        confirmRegister.setFb("");
        confirmRegister.setHm("21");
        confirmRegister.setHx("");
        confirmRegister.setHzdw("");
        confirmRegister.setJe("9.5");
        confirmRegister.setJqm("");
        confirmRegister.setJsfs("");
        confirmRegister.setJsje("9.5");
        confirmRegister.setJskh("");
        confirmRegister.setJsklb("");
        confirmRegister.setJylr("");
        confirmRegister.setJylsh("111111");
        confirmRegister.setJymc("");
        confirmRegister.setSm("");
        confirmRegister.setYyfs("交易信息");
        confirmRegister.setYysj("2017-10-06 8:00:00");
        System.out.println(hisOutpatient.confirmRegister(confirmRegister));
    }

    @Test
    public void subscribeRegisterTest() throws Exception {
        HisOutpatient hisOutpatient = new HisOutpatient();
        ConfirmRegister confirmRegister = new ConfirmRegister();
        confirmRegister.setBrid("227");
        confirmRegister.setBrlx("医保病人");
        confirmRegister.setCzjlid("");
        confirmRegister.setFb("");
        confirmRegister.setHm("21");
        confirmRegister.setHx("26");
        confirmRegister.setHzdw("");
        confirmRegister.setJe("9.5");
        confirmRegister.setJqm("");
        confirmRegister.setJsfs("");
        confirmRegister.setJsje("9.5");
        confirmRegister.setJskh("");
        confirmRegister.setJsklb("");
        confirmRegister.setJylr("");
        confirmRegister.setJylsh("111111");
        confirmRegister.setJymc("");
        confirmRegister.setSm("");
        confirmRegister.setYyfs("交易信息");
        confirmRegister.setYysj("2017-10-06 8:00:00");
        System.out.println(hisOutpatient.subscribeRegister(confirmRegister));
    }

    @Test
    public void cancelSubscribeRegisterTest() throws Exception {
        HisOutpatient hisOutpatient = new HisOutpatient();
        hisOutpatient.cancelSubscribeRegister("","","");
    }


}

package com.jishi.reservation.service.his;
import com.jishi.reservation.TestApplication;
import com.jishi.reservation.service.his.bean.ConfirmRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by zbs on 2017/10/6.
 */
public class HisOutpatientTest extends TestApplication {

    @Autowired
    private HisOutpatient hisOutpatient;

    String brid = "";
    String jsklb = "";
    String cxts = "";
    String zd = "";

    /**
     * 获取缴费单据信息
     * @throws Exception
     */
    @Test
    public void queryPayReceiptTest() throws Exception {
        System.out.println(hisOutpatient.queryPayReceipt("215","",String.valueOf(500),"1"));
    }

    /**
     * 获取挂号号源
     * @throws Exception
     */
    @Test
    public void queryRegisteredNumberTest() throws Exception {
        System.out.println(hisOutpatient.queryRegisteredNumber("","","","32","","","",""));
    }

    @Test
    public void queryLastPrice() throws Exception {
        String xmid = "";
        String brid= "";
        System.out.println(hisOutpatient.queryLastPrice(xmid, brid));
    }

    @Test
    public void checkIsPatientMatchRegister() throws Exception {
        String brid = "";
        String ghhm = "";
        System.out.println(hisOutpatient.checkIsPatientMatchRegister(brid, ghhm));
    }
    @Test
    public void checkCancelRegister() throws Exception {
        String ghhm = "";
        System.out.println(hisOutpatient.checkCancelRegister(ghhm));
    }
    @Test
    public void cancelRegister() throws Exception {
        String ghhm = "";
        System.out.println(hisOutpatient.cancelRegister(ghhm));
    }
    @Test
    public void checkIsRegisterLimit() throws Exception {
        String brid = "";
        String hm = "";
        String registerTime = "";
        String departmentId = "";
        String czjlid = "";
        System.out.println(hisOutpatient.checkIsRegisterLimit(brid, hm, registerTime, departmentId, czjlid));
    }
    @Test
    public void lockRegister() throws Exception {
        String hm = "";
        String yysj = "";
        String hzdw = "";
        String jqm = "";
        String czjlid = "";
        System.out.println(hisOutpatient.lockRegister(hm, yysj, hzdw, jqm, czjlid));
    }

    @Test
    public void cancelSubscribeRegisterTest() throws Exception {
        String ghdh = "";
        String jsklb = "";
        String yyfs = "";
        hisOutpatient.cancelSubscribeRegister(ghdh, jsklb, yyfs);
    }
    @Test
    public void subscribeRegisterTest() throws Exception {
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
    public void confirmRegisterTest() throws Exception {
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
    public void selectDepartments() throws Exception {
        String hzdw = "";
        String cxts = "";
        String zd = "";
        System.out.println(hisOutpatient.selectDepartments(hzdw, cxts, zd));
    }
    @Test
    public void unlockRegister() throws Exception {
        String hm = "";
        String rq = "";
        String hx = "";
        //System.out.println(hisOutpatient.unlockRegister(hm, rq, hx));
    }
    @Test
    public void queryRegisterRegReceipt() throws Exception {
        System.out.println(hisOutpatient.queryRegisterRegReceipt(brid, cxts));
    }
    @Test
    public void queryPayReceipt() throws Exception {
        System.out.println(hisOutpatient.queryPayReceipt(brid, jsklb, cxts, zd));
    }
    @Test
    public void payModify() throws Exception {
        String brId = "";
        String docmentId = "";
        BigDecimal price = new BigDecimal("1");
        BigDecimal payPrice = new BigDecimal("1");
        int isRegisterDoc = 1;
        String thirdOrderNumber = "";
        String paymentContent = "";
        System.out.println(hisOutpatient.payModify(
                brId , docmentId, price, payPrice, isRegisterDoc, thirdOrderNumber, paymentContent, jsklb));
    }
    @Test
    public void batchPayModify() throws Exception {
        String brId = "";
        String docmentId = "";
        BigDecimal price = new BigDecimal("1");
        BigDecimal payPrice = new BigDecimal("1");
        int isRegisterDoc = 1;
        String thirdOrderNumber = "";
        String paymentContent = "";
        System.out.println(hisOutpatient.batchPayModify(
                brId , docmentId, price, payPrice, isRegisterDoc, thirdOrderNumber, paymentContent, jsklb));
    }
    @Test
    public void queryOutpatientVisitRecord() throws Exception {
        System.out.println(hisOutpatient.queryOutpatientVisitRecord(brid, 1, 10, zd));
    }
    @Test
    public void queryOutpatientVisitPrescription() throws Exception {
        String ghdh = "";
        System.out.println(hisOutpatient.queryOutpatientVisitPrescription(ghdh));
    }
    @Test
    public void queryOutpatientVisitReceipt() throws Exception {
        String ghdh = "";
        System.out.println(hisOutpatient.queryOutpatientVisitReceipt(ghdh));
    }
    @Test
    public void testGuideAdviceReceiptQuery() throws Exception {
        String ghdh = "";
        System.out.println(hisOutpatient.testGuideAdviceReceiptQuery(jsklb, ghdh));
    }
    @Test
    public void testGuideDrugAdviceQuery() throws Exception {
        String yzid = "";
        System.out.println(hisOutpatient.testGuideDrugAdviceQuery(yzid));
    }
    public void testReportRecordQuery() throws Exception {
        String brid = "";
        System.out.println(hisOutpatient.testReportRecordQuery(brid));
    }
    public void testReportXMLDetailQuery() throws Exception {
        String blid = "";
        String yzid = "";
        System.out.println(hisOutpatient.testReportXMLDetailQuery(blid, yzid));
    }
    public void basicUsableServiceQuery() throws Exception {
        System.out.println(hisOutpatient.basicUsableServiceQuery());
    }

}

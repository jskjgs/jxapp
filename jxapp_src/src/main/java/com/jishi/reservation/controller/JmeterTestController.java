package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.dao.mapper.AccountMapper;
import com.jishi.reservation.dao.mapper.OutpatientPaymentMapper;
import com.jishi.reservation.dao.models.Account;
import com.jishi.reservation.dao.models.Department;
import com.jishi.reservation.dao.models.OutpatientPayment;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.bean.DepartmentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangxiong on 2017/12/19.
 */
@RestController
@RequestMapping("/jmeter")
@Slf4j
public class JmeterTestController extends MyBaseController {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    HisOutpatient hisOutpatient;
    @Autowired
    private OutpatientPaymentMapper outpatientPaymentMapper;

    @RequestMapping(value = "doGetDate", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject fastestTest() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateSrc = simpleDateFormat.format(date);
        log.info("----------jmeter-test-doGetDate: " + dateSrc);
        return ResponseWrapper().addMessage("success").addData(dateSrc).ExeSuccess();
    }

     // sec: 睡眠时间（秒）
    @RequestMapping(value = "doGetDateDelay", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject fastestTestDelay(
            @RequestParam(value = "sec", defaultValue = "10") Integer sec) throws InterruptedException {
        Date dateBegin = new Date();
        log.info("----------jmeter-test-doGetDate_delay: " + dateBegin);
        Thread.sleep(sec * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateEnd = new Date();
        String dateSrc = simpleDateFormat.format(dateEnd);
        log.info("----------jmeter-test-doGetDate_delay: " + (dateEnd.getTime() - dateBegin.getTime())/1000 + "s");
        return ResponseWrapper().addMessage("success").addData(dateSrc).ExeSuccess();
    }

    @RequestMapping(value = "doDbSelectTest", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject doDbSelectTest() {
        log.info("----------jmeter-test-doDbSelectTest");
        Account account = new Account();
        account.setId(30L);
        Object obj = accountMapper.selectOne(account);
        return ResponseWrapper().addMessage("success").addData(obj).ExeSuccess();
    }

    @RequestMapping(value = "doDbInsertTest", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject doDbInsertTest() {
        log.info("----------jmeter-test-doDbInsertTest");
        String registerNumber = "jmeter-test-" + System.currentTimeMillis();
        OutpatientPayment payment = new OutpatientPayment();
        payment.setAccountId(30L);
        payment.setBrId("jmeter-test-30");
        payment.setRegisterNumber(registerNumber);
        payment.setDocmentId("jmeter-test");
        payment.setDocmentType(1);
        payment.setStatus(0);
        payment.setOrderId(0L);
        payment.setOrderNumber("jmeter-test");
        payment.setCreateTime(new Date());
        outpatientPaymentMapper.insert(payment);
        OutpatientPayment paymentDelete = new OutpatientPayment();
        paymentDelete.setRegisterNumber(registerNumber);
        outpatientPaymentMapper.delete(paymentDelete);
        return ResponseWrapper().addMessage("success").addData(payment).ExeSuccess();
    }

    @RequestMapping(value = "doHisTest", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject doHisTest() throws Exception {
        log.info("----------jmeter-test-doHisTest");
        DepartmentList departmentListHis = hisOutpatient.selectDepartments("", "", "");
        DepartmentList.KSLIST kslist = departmentListHis.getKslist();
        List<DepartmentList.DepartmentHis> list = kslist.getList();
        List<Department> departmentListLocal = new ArrayList<>();
        for (DepartmentList.DepartmentHis dptHis : list) {
            Department department = new Department();
            department.setName(dptHis.getMc());
            department.setHId(dptHis.getId());
            department.setEnable(0);
            department.setPosition("锦欣医院");
            departmentListLocal.add(department);
        }
        return ResponseWrapper().addData(departmentListLocal).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

}

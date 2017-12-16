package com.jishi.reservation.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.RegisterAdminVO;
import com.jishi.reservation.controller.protocol.RegisterCompleteVO;
import com.jishi.reservation.dao.mapper.*;
import com.jishi.reservation.dao.models.*;
import com.jishi.reservation.otherService.pay.AlibabaPay;
import com.jishi.reservation.service.enumPackage.*;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.HisUserManager;
import com.jishi.reservation.service.his.bean.LastPrice;
import com.jishi.reservation.service.his.bean.LockRegister;
import com.jishi.reservation.util.Helpers;
import com.jishi.reservation.util.NewRandomUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class RegisterService {

    @Autowired
    RegisterMapper registerMapper;

    @Autowired
    DoctorMapper doctorMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    PatientInfoMapper patientInfoMapper;
    @Autowired
    AccountService accountService;
    @Autowired
    PatientInfoService patientInfoService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    ScheduledService scheduledService;
    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    HisUserManager hisUserManager;

    @Autowired
    HisOutpatient hisOutpatient;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    AlibabaPay alibabaPay;


    /**
     * 增加一条预约
     * @param accountId
     * @param brid
     * @param departmentId
     * @param doctorId
     * @param agreedTime
     * @throws Exception
     */
    @Transactional
    public RegisterCompleteVO addRegister(String orderNumber,Long accountId,String brid,String departmentId,String doctorId,String xmid,
                                          Long agreedTime,String timeInterval,String doctorName,
                                          String price,String subject,String brName,String department,String hm) throws Exception {

        log.info("开始预约，预约病人id："+brid);


        RegisterCompleteVO completeVO = new RegisterCompleteVO();
        if(orderNumber == null || "".equals(orderNumber)){


            //检查病人信息和挂号信息是否匹配
            log.info("检查病人信息和挂号信息是否匹配");
            if(!hisOutpatient.checkIsPatientMatchRegister(brid, hm)){
                completeVO.setState(RegisterErrCodeEnum.PATIENT_NOT_MATCH.getCode());
                return completeVO;
            }


            Date agreeDate = new Date(agreedTime);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            log.info("检测本地是否存在挂号记录");
            if(!this.canRegister(brid,agreeDate,doctorId)){
                log.info("挂号检查失败,库里面已存在该记录,不能挂号.");

                completeVO.setState(RegisterErrCodeEnum.LIMIT_FOR_PATIENT.getCode());
                return completeVO;
            }

            //挂号检查
            log.info("去his检查是否能挂号");
            if(!hisOutpatient.checkIsRegisterLimit(brid,hm,sdf.format(agreeDate),departmentId)){

                log.info("挂号检查失败，不能挂号.");

                completeVO.setState(RegisterErrCodeEnum.LIMIT_FOR_PATIENT.getCode());
                return completeVO;
            }




            //his 锁定号源,返回hx 号序
        String hx = this.lockRegister(hm, agreeDate);
        if(hx.equals("invalid hx")){
            completeVO.setState(RegisterErrCodeEnum.DOCTOR_FULL.getCode());
            return completeVO;
        }


        LastPrice lastPrice = hisOutpatient.queryLastPrice(xmid, brid);


        BigDecimal truePriceFormat = BigDecimal.valueOf(Double.valueOf(price));
        BigDecimal yhjeFormat = BigDecimal.valueOf(0);
        OrderInfo order = new OrderInfo();
        if(lastPrice!=null){
            if(lastPrice.getYhje() != null && !"".equals(lastPrice.getYhje())){
                BigDecimal yhje = new BigDecimal(lastPrice.getYhje());
                log.info("获取到的优惠金额（未处理格式）："+yhje);
                yhjeFormat = yhje.setScale(2,RoundingMode.HALF_UP);
                log.info("获取到的优惠金额（处理格式）："+yhjeFormat);
                order.setDiscount(yhjeFormat);

            }
            if(lastPrice.getJe()!=null && !"".equals(lastPrice.getJe())){
                BigDecimal truePrice=new BigDecimal(lastPrice.getJe());
                log.info("获取到真实价格（未处理格式）："+truePrice);
                truePriceFormat = truePrice.setScale(2, RoundingMode.HALF_UP);
                log.info("获取到真实价格（处理格式）："+truePriceFormat);
            }

        }


            log.info("没有传入订单号，生成新的订单");

            order.setAccountId(accountId);
            order.setBrId(brid);
            order.setCreateTime(new Date());
            order.setSubject(subject);
            order.setDes(subject);
            order.setPrice(truePriceFormat);
            order.setEnable(EnableEnum.EFFECTIVE.getCode());
            String orderNumberGenerate = AlibabaPay.generateUniqueOrderNumber();
            order.setOrderNumber(orderNumberGenerate);
            //order.setRegisterId(register.getId());
            order.setStatus(OrderStatusEnum.WAIT_PAYED.getCode());
            order.setPayType(PayEnum.ALI.getCode());
            order.setType(OrderTypeEnum.REGISTER.getCode());

            Register register = new Register();
            register.setAccountId(accountId);
            register.setDepartmentId(departmentId);
            register.setDoctorId(doctorId);
            register.setBrId(brid);
            register.setDepartment(department);
            register.setPatientName(brName);
            register.setDoctorName(doctorName);
            register.setOrderId(order.getId());
            register.setAgreedTime(agreeDate);
            register.setStatus(StatusEnum.REGISTER_STATUS_NO_PAYMENT.getCode());
            register.setEnable(EnableEnum.EFFECTIVE.getCode());
            register.setCreateTime(new Date());
            register.setHm(hm);
            String serialCode = NewRandomUtil.getRandomNum(4);
            register.setSerialNumber(serialCode);

            registerMapper.insertReturnId(register);
            order.setRegisterId(register.getId());
            orderInfoMapper.insertReturnId(order);
            register.setOrderId(order.getId());

            completeVO.setRegisterId(register.getId());
            completeVO.setDoctor(doctorName);
            //Department department = departmentMapper.queryById(departmentId);
            completeVO.setDepartment(department);
            completeVO.setAgreeTime(agreeDate);
            //todo 真实的地址
            completeVO.setPosition("四川泸州锦欣医院");
            completeVO.setTimeInterval(timeInterval);
            completeVO.setPatient(brName);



            completeVO.setPayType(PayEnum.ALI.getCode());
            completeVO.setPayTime(new Date());
            completeVO.setCompleteTime(new Date());
            completeVO.setPrice(truePriceFormat);
            //completeVO.setPrice(BigDecimal.valueOf(0.01));
            completeVO.setCountDownTime(new Date().getTime()+30*60*1000L-new Date().getTime()>0?register.getCreateTime().getTime()+30*60*1000L-new Date().getTime():0);
            completeVO.setOrderCode(orderNumberGenerate);
            completeVO.setSerialNumber(serialCode);
            completeVO.setSubject(subject);
            completeVO.setDes(subject);
            completeVO.setOrderId(order.getId());
            completeVO.setYhje(yhjeFormat);
            completeVO.setState(RegisterErrCodeEnum.RIGHT.getCode());
            register.setHx(hx);
            registerMapper.updateByPrimaryKeySelective(register);

            return completeVO;
        }else {

            log.info("传入了订单号，更新新的订单号，带到支付宝");
            log.info("检查订单状态,,");
            //生成新的订单号，带去支付宝，不然支付宝会找到重复订单，支付失败
            String newOrderNumber = AlibabaPay.generateUniqueOrderNumber();
            OrderInfo orderInfo = orderInfoMapper.queryByIdOrOrderNumber(null, orderNumber);
            if(orderInfo == null){
                completeVO.setState(RegisterErrCodeEnum.ORDER_NOT_EXIST.getCode());
                return completeVO;
            }


            if(!orderInfo.getStatus().equals(OrderStatusEnum.WAIT_PAYED.getCode()) || !orderInfo.getType().equals(OrderTypeEnum.REGISTER.getCode())){
                completeVO.setState(RegisterErrCodeEnum.ORDER_STATE_NOT_MATCH.getCode());
                return completeVO;
            }
            //订单的用户id要和当前操作者的用户id相匹配
            if(!orderInfo.getAccountId().equals(accountId)){
                completeVO.setState(RegisterErrCodeEnum.ORDER_NUMBER_NOT_MATCH_ACCOUNT.getCode());
                return completeVO;
            }

            orderInfo.setOrderNumber(newOrderNumber);


            orderInfoMapper.updateByPrimaryKeySelective(orderInfo);

            Register register = registerMapper.queryByOrderId(orderInfo.getId());


            completeVO.setRegisterId(register.getId());
            completeVO.setDoctor(doctorName);
            //Department department = departmentMapper.queryById(departmentId);
            completeVO.setDepartment(department);
            completeVO.setAgreeTime(register.getAgreedTime());
            //todo 真实的地址
            completeVO.setPosition("四川泸州锦欣医院");
            completeVO.setTimeInterval(timeInterval);
            completeVO.setPatient(brName);


            completeVO.setPayType(PayEnum.ALI.getCode());
            completeVO.setPayTime(new Date());
            completeVO.setCompleteTime(new Date());
            completeVO.setPrice(orderInfo.getPrice());
            //completeVO.setPrice(BigDecimal.valueOf(0.01));
            completeVO.setCountDownTime(new Date().getTime()+30*60*1000L-new Date().getTime()>0?register.getCreateTime().getTime()+30*60*1000L-new Date().getTime():0);
            completeVO.setOrderCode(newOrderNumber);
            completeVO.setSerialNumber(register.getSerialNumber());
            completeVO.setSubject(subject);
            completeVO.setDes(subject);
            completeVO.setOrderId(orderInfo.getId());
            completeVO.setYhje(orderInfo.getDiscount());
            completeVO.setSubject(orderInfo.getSubject());
            completeVO.setDes(orderInfo.getSubject());
            completeVO.setPatient(register.getPatientName());
            completeVO.setDoctor(register.getDoctorName());
            completeVO.setState(RegisterErrCodeEnum.RIGHT.getCode());
            return completeVO;

        }


    }

    private boolean canRegister(String brid, Date agreeDate, String doctorId) {

        log.info("开始检测本地的库.....");
        log.info("病人ID："+brid+",预约时间："+agreeDate.getTime()+",医生id:"+doctorId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh");
        String timeStr = sdf.format(agreeDate);
        log.info("转换后的预约时间："+timeStr);
        List<Register> registerList = registerMapper.queryByBrIdTimeDoctorId(brid, timeStr, doctorId);
        log.info("本地的查询预约列表："+JSONObject.toJSONString(registerList));
        if(registerList == null || registerList.size() == 0){
            log.info("为空了。。。");
        }else{
            log.info("不为空");
        }
        return registerList == null || registerList.size() == 0;
    }


    private String lockRegister(String hm, Date agreedTime) throws Exception {
        log.info("开始锁定号源");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String timeStr = sdf.format(agreedTime);
        LockRegister lockRegister = hisOutpatient.lockRegister(hm, timeStr, "", "jxyy+zczh");
        if(lockRegister !=null)
            return lockRegister.getHx();
        return "invalid hx";
    }

    /**
     * 查询预约
     * @param accountId
     * @param registerId
     * @param status
     * @return
     * @throws Exception
     */
    public List<Register> queryRegister(Long registerId,Long accountId ,Integer status,Integer enable) throws Exception {
        if(Helpers.isNullOrEmpty(accountId) && Helpers.isNullOrEmpty(registerId))
            throw new Exception("查询条件不能都为空");

        //找到所有有效的病人
        List<String> patientIdList =  patientInfoMapper.queryValidPatientHisId(accountId);
        if(patientIdList != null && patientIdList.size() != 0){
            return registerMapper.selectConditionByBridList(patientIdList, registerId, status, enable);


        }else{
            return null;
        }


       // List<Register> list = registerMapper.selectCondition(accountId, registerId, status, enable);
    }

    /**
     * 带有分页排序的查询
     * @param registerId
     * @param accountId
     * @param status
     * @param enable
     * @param paging
     * @return
     * @throws Exception
     */
    public PageInfo queryRegisterPageInfo(Long registerId,Long accountId ,Integer status,Integer enable,Paging paging) throws Exception {
        if(!Helpers.isNullOrEmpty(paging)){
            if(paging.getPageSize() == 0){
                paging.setPageSize(queryRegister(registerId,accountId,status,enable).size());
            }
            PageHelper.startPage(paging.getPageNum(),paging.getPageSize()).setOrderBy("id desc");
            List<Register> list = queryRegister(registerId, accountId, status, enable);
            log.info("~~"+JSONObject.toJSONString(list));
            return new PageInfo<>(list);



        }
        return null;
    }

    /**
     * 查询全部预约
     * @return
     * @throws Exception
     */
    public List<Register> queryAllRegister(Integer enable, Paging paging) throws Exception {
        Register queryRegister = new Register();
        queryRegister.setEnable(enable);
        if(!Helpers.isNullOrEmpty(paging))
           PageHelper.startPage(paging.getPageSize(),paging.getPageNum(),paging.getOrderBy());
        return registerMapper.select(queryRegister);
    }


    /**
     * 把就诊信息置为无效
     * @param registerId
     * @throws Exception
     */

    @Transactional
    public Integer failureRegister(Long registerId) throws Exception {
 /*       if(Helpers.isNullOrEmpty(registerId) || queryRegister(registerId,null,null,null) == null)
            throw new Exception("预约信息为空.");*/


        Register register = registerMapper.queryById(registerId);
        register.setStatus(StatusEnum.REGISTER_STATUS_CANCEL.getCode());
        OrderInfo orderInfo = orderInfoMapper.queryById(register.getOrderId());
        orderInfo.setStatus(StatusEnum.REGISTER_STATUS_CANCEL.getCode());


        // 检查是否有资格退号
        if(!hisOutpatient.checkCancelRegister(orderInfo.getGhdh())){
            log.info("订单id:"+orderInfo.getId()+",该订单没有退号资格。");
            return 1;
        }
        //开始退号
        if(hisOutpatient.cancelRegister(orderInfo.getGhdh())){

            log.info("预约取消成功..");
            log.info("向支付宝发起退款请求");
            //todo 现在只有支付宝 11.30

            if(alibabaPay.refund(orderInfo.getOrderNumber()) == 0){
                registerMapper.updateByPrimaryKeySelective(register);
                orderInfoMapper.updateByPrimaryKeySelective(orderInfo);

                return 0;
            }else {
                log.info("退款失败..订单号："+orderInfo.getOrderNumber());
                return 1;
            }


        }else {
            log.info("订单id:"+orderInfo.getId()+",该订单退号同步到his的时候失败。");

            return 1;
        }


    }

    public Register queryByOrderId(Long orderId) {

        return registerMapper.queryByOrderId(orderId);
    }

    public PageInfo<RegisterAdminVO> queryRegisterAdmin(String key, Long startTime, Long endTime, Long doctorId, Long departmentId, Integer status, Integer startPage, Integer pageSize) {


        PageHelper.startPage(startPage,pageSize).setOrderBy("id desc");
        List<Register> registerList = registerMapper.queryAllRegister();
        PageInfo<Register> registerPageInfo = new PageInfo<>(registerList);
        List<RegisterAdminVO> voList = new ArrayList<>();
        for (Register register : registerList) {
            log.info("预约id:"+register.getId());
            OrderInfo orderInfo = orderInfoMapper.queryById(register.getOrderId());
            RegisterAdminVO vo = new RegisterAdminVO();
            vo.setDepartment(register.getDepartment());
            vo.setDepartmentId(register.getDepartmentId());
            vo.setDoctorId(register.getDoctorId());
            vo.setDoctorName(register.getDoctorName());
            vo.setPatientName(register.getPatientName());
            vo.setRegisterTime(register.getAgreedTime());
            vo.setId(register.getId());
            Account account = accountMapper.queryById(register.getAccountId());
            PatientInfo patientInfo = patientInfoMapper.queryByById(register.getBrId(),orderInfo.getAccountId());
            vo.setPhone(account.getPhone());
            //todo  状态..
            vo.setStatus(String.valueOf(orderInfo.getStatus()));
            if(patientInfo !=null){
                vo.setIdCard(patientInfo.getIdCard());
                vo.setJjkh(patientInfo.getMzh());
            }

            voList.add(vo);

        }
        PageInfo<RegisterAdminVO> page = new PageInfo<>();
        page.setList(voList);
        page.setTotal(registerPageInfo.getTotal());
        page.setPageNum(registerPageInfo.getPageNum());
        page.setPageSize(registerPageInfo.getPageSize());
        page.setPages(registerPageInfo.getPages());

        return page;
    }

    /**
     * 检查订单是否是已付款的预约订单，这样的订单才是可以申请预约退号的订单
     * @param registerId
     * @return
     */
    public boolean checkIsPayedRegister(Long registerId) {

        OrderInfo orderInfo = orderInfoMapper.queryById(registerMapper.queryById(registerId).getOrderId());

        return orderInfo.getType()==OrderTypeEnum.REGISTER.getCode() && orderInfo.getStatus() == OrderStatusEnum.PAYED.getCode();
    }
}

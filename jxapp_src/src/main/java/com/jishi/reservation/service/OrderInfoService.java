package com.jishi.reservation.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.OrderVO;
import com.jishi.reservation.controller.protocol.PrePaymentRecordVO;
import com.jishi.reservation.dao.mapper.OrderInfoMapper;
import com.jishi.reservation.dao.mapper.PrePaymentMapper;
import com.jishi.reservation.dao.mapper.RegisterMapper;
import com.jishi.reservation.dao.models.OrderInfo;
import com.jishi.reservation.dao.models.PrePayment;
import com.jishi.reservation.dao.models.Register;
import com.jishi.reservation.otherService.pay.AlibabaPay;
import com.jishi.reservation.service.enumPackage.*;
import com.jishi.reservation.service.his.bean.ConfirmOrder;
import com.jishi.reservation.service.his.bean.ConfirmRegister;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    RegisterMapper registerMapper;

    @Autowired
    PatientInfoService patientService;

    @Autowired
    PrePaymentMapper prePaymentMapper;

    public OrderVO queryOrderVoById(Long orderId,String orderNumber) throws ParseException {

        OrderInfo orderInfo = orderInfoMapper.queryByIdOrOrderNumber(orderId,orderNumber);
        Register register =  registerMapper.queryByOrderId(orderInfo.getId());

        OrderVO orderVO = new OrderVO();
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        orderVO.setOrderNumber(orderInfo.getOrderNumber());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(register != null){
            orderVO.setSerialNumber(register.getSerialNumber());
            orderVO.setDoctorName(register.getDoctorName());
            orderVO.setDepartment(register.getDepartment());
            orderVO.setPatientName(register.getPatientName());
            orderVO.setTimeInterval(sdf.format(register.getAgreedTime()).contains("14:00")?"下午":"上午");
            orderVO.setRegisterTime(register.getAgreedTime());
        }else {
            log.info("非预约订单，没有预约信息。");
        }

        orderVO.setPayType(orderInfo.getPayType());
        orderVO.setPrice(orderInfo.getPrice());
        orderVO.setPosition("泸州锦欣医院");
        if(orderInfo.getPayTime()!=null){
            orderVO.setCompletedTime(orderInfo.getPayTime());
        }


        return orderVO;
    }



    public ConfirmRegister returnConfirmRegister(Long orderId,String orderNumber) {

        ConfirmRegister confirmRegister = new ConfirmRegister();
        OrderInfo orderInfo = orderInfoMapper.queryByIdOrOrderNumber(orderId,orderNumber);
        Register register = registerMapper.queryByOrderId(orderInfo.getId());
        confirmRegister.setBrid(orderInfo.getBrId());
        confirmRegister.setJe(String.valueOf(orderInfo.getPrice().stripTrailingZeros()));

        log.info("处理前："+String.valueOf(orderInfo.getPrice()));
        log.info("处理后："+String.valueOf(orderInfo.getPrice().stripTrailingZeros()));
        confirmRegister.setCzjlid("");
        confirmRegister.setHm(register.getHm());
        confirmRegister.setHx(register.getHx());
        confirmRegister.setHzdw("");   //合作单位 固定传入第三方名称
        confirmRegister.setYyfs("");  //预约方式 固定传入第三方名称
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        confirmRegister.setYysj(sdf.format(register.getAgreedTime()));
        confirmRegister.setSm(""); //说明，固定传入第三方名称
        confirmRegister.setJqm("jxyy+zczh");
        confirmRegister.setJsklb("");//结算卡类别，固定传入第三方名称
        confirmRegister.setJsfs("");//结算方式，传空
        //confirmRegister.setJsje(String.valueOf(orderInfo.getPrice()));
        confirmRegister.setJsje(String.valueOf(orderInfo.getPrice().stripTrailingZeros()));
        confirmRegister.setJylsh(orderInfo.getOrderNumber());
        confirmRegister.setJymc("交易信息");
        confirmRegister.setJylr("支付帐号|姓名");
        log.info("调取his的订单确认Bean:\n"+JSONObject.toJSONString(confirmRegister));
        return confirmRegister;
    }

    public PageInfo queryOrderList(Long accountId,Integer status, Integer enable, Paging paging) {
        if(paging.getPageSize() == 0){
            paging.setPageSize( queryOrderList(accountId,status,enable).size());
        }
        if(!Helpers.isNullOrEmpty(paging))
            PageHelper.startPage(paging.getPageNum(),paging.getPageSize(),paging.getOrderBy());
        return new PageInfo(queryOrderList(accountId,status,enable));


    }

    private List queryOrderList(Long accountId,Integer status, Integer enable) {

        return orderInfoMapper.queryOrderList( accountId,status,enable);

    }

    public Integer confirmOrderHis(Long orderId, String orderNumber,ConfirmOrder confirmOrder) {

        OrderInfo orderInfo = orderInfoMapper.queryByIdOrOrderNumber(orderId,orderNumber);
        //Preconditions.checkState(orderInfo.getStatus()==OrderStatusEnum.PAYED.getCode(),"该订单未支付，不能确认");
        if(orderInfo.getStatus() != OrderStatusEnum.PAYED.getCode())
            return ReturnCodeEnum.FAILED.getCode();
        orderInfo.setGhdh(confirmOrder.getGhdh());
        orderInfo.setCzsj(confirmOrder.getCzsj());
        orderInfo.setJsid(confirmOrder.getJzid());
        orderInfo.setStatus(OrderStatusEnum.PAYED.getCode());

        orderInfoMapper.updateByPrimaryKeySelective(orderInfo);

        Register register = registerMapper.queryByOrderId(orderInfo.getId());
        register.setStatus(StatusEnum.REGISTER_STATUS_PAYMENT.getCode());
        registerMapper.updateByPrimaryKeySelective(register);

        log.info("his订单信息已同步到系统中..预约信息已更新");
        return ReturnCodeEnum.SUCCESS.getCode();
    }

    public OrderInfo findOrderById(Long orderId) {
        return orderInfoMapper.queryById(orderId);
    }


    /**
     * 生成住院预交款
     * @param subject
     * @param price
     * @param accountId
     * @param brId
     * @param rycs
     */
    public OrderInfo generatePrepayment(String subject, BigDecimal price, Long accountId, String brId, Integer rycs) throws Exception {

        //todo 判断accountId和brId是否匹配
        log.info("accountId:"+accountId+",brId:"+brId);
        Preconditions.checkState(patientService.isAccountIdMatchBrid(accountId,brId),"账号和brId不匹配，不能执行操作");

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setSubject(subject);
        orderInfo.setDes(subject);
        orderInfo.setOrderNumber(AlibabaPay.generateUniqueOrderNumber());
        orderInfo.setPrice(price);
        orderInfo.setAccountId(accountId);
        orderInfo.setBrId(brId);
        orderInfo.setType(OrderTypeEnum.HOSPITALIZED.getCode());
        orderInfo.setEnable(EnableEnum.EFFECTIVE.getCode());
        orderInfo.setStatus(OrderStatusEnum.WAIT_PAYED.getCode());
        orderInfo.setCreateTime(new Date());
        orderInfoMapper.insertSelectiveReturnId(orderInfo);

        PrePayment prePayment = new PrePayment();
        prePayment.setOrderId(orderInfo.getId());
        prePayment.setBrId(brId);
        prePayment.setAccountId(accountId);
        prePayment.setRycs(rycs);

        prePaymentMapper.insertSelectiveReturnId(prePayment);
        return orderInfo;

    }

    /**
     * 生成门诊订单
     * @param subject
     * @param price
     * @param accountId
     * @param brId
     */
    public OrderInfo generateOutpatient(Long accountId, String brId, String subject, BigDecimal price) throws Exception {

      //todo 判断accountId和brId是否匹配
      Preconditions.checkState(patientService.isAccountIdMatchBrid(accountId,brId),"账号和brId不匹配，不能执行操作");

      OrderInfo orderInfo = new OrderInfo();
      orderInfo.setSubject(subject);
      orderInfo.setDes(subject);
      orderInfo.setOrderNumber(AlibabaPay.generateUniqueOrderNumber());
      orderInfo.setPrice(price);
      orderInfo.setAccountId(accountId);
      orderInfo.setBrId(brId);
      orderInfo.setType(OrderTypeEnum.Outpatient.getCode());
      orderInfo.setEnable(EnableEnum.EFFECTIVE.getCode());
      orderInfo.setStatus(OrderStatusEnum.WAIT_PAYED.getCode());
      orderInfo.setCreateTime(new Date());
      orderInfoMapper.insertSelectiveReturnId(orderInfo);

      return orderInfo;

    }

    public OrderInfo queryOrderByOrderNumber(String orderNumber) {
        return orderInfoMapper.queryByNumber(orderNumber);
    }

    public PageInfo<PrePaymentRecordVO> queryPrePayment(String brId,Integer startPage,Integer pageSize) throws ParseException {

        if(pageSize == 0){
            pageSize = orderInfoMapper.queryPrePayment(brId).size();
        }
        PageHelper.startPage(startPage,pageSize).setOrderBy(" create_time desc ");
        List<OrderInfo> list =  orderInfoMapper.queryPrePayment(brId);
        PageInfo<OrderInfo> page = new PageInfo<>(list);


        List<PrePaymentRecordVO> voList = new ArrayList<>();
        PageInfo<PrePaymentRecordVO> resultPage = new PageInfo<>();
        for (OrderInfo orderInfo : list) {
            PrePayment prePayment = prePaymentMapper.queryByOrderId(orderInfo.getId());
            PrePaymentRecordVO vo = new PrePaymentRecordVO();
            vo.setZffs(orderInfo.getPayType());
            vo.setOrderNumber(orderInfo.getOrderNumber());
            vo.setJe(String.valueOf(orderInfo.getPrice()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vo.setJksh(orderInfo.getPayTime());
            vo.setLx("预交");

            voList.add(vo);
        }

        resultPage.setList(voList);
        resultPage.setHasNextPage(page.isHasNextPage());
        resultPage.setSize(page.getSize());
        resultPage.setPageNum(page.getPageNum());
        resultPage.setTotal(page.getTotal());
        resultPage.setPages(page.getPages());

        return resultPage;
    }
}

package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.RegisterAdminVO;
import com.jishi.reservation.controller.protocol.RegisterCompleteVO;
import com.jishi.reservation.controller.protocol.RegisterVO;
import com.jishi.reservation.dao.models.*;
import com.jishi.reservation.service.*;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.RegisterErrCodeEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.support.JpushSupport;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/register")
@Slf4j
@Api(description = "预约相关接口")
public class RegisterController extends MyBaseController {

    @Autowired
    RegisterService registerService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PatientInfoService patientInfoService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    AccountService accountService;

    @Autowired
    JpushSupport jpushSupport;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    HisOutpatient hisOutpatient;


    @ApiOperation(value = "根据项目id和病人id brid查询挂号的真实价格")
    @RequestMapping(value = "queryTruePrice", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject addRegister(
                                  @ApiParam(value = "病人id", required = true) @RequestParam(value = "brid", required = true) String brid,
                                  @ApiParam(value = "项目id", required = true) @RequestParam(value = "xmid", required = true) String xmid
    ) throws Exception {


        //String price = hisOutpatient.queryLastPrice(xmid, brid);

        return ResponseWrapper().addMessage("查询成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
        }




    @ApiOperation(value = "增加预约信息",response = RegisterCompleteVO.class)
    @RequestMapping(value = "addRegister", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject addRegister(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                                  @ApiParam(value = "订单号", required = false) @RequestParam(value = "orderNumber", required = false) String orderNmuer,
                                  @ApiParam(value = "价格", required = false) @RequestParam(value = "price", required = false) String price,
                                  @ApiParam(value = "支付名称", required = false) @RequestParam(value = "subject", required = false) String subject,
                                  @ApiParam(value = "病人名称", required = false) @RequestParam(value = "brName", required = false) String brName,
                                  @ApiParam(value = "医生名称", required = false) @RequestParam(value = "doctorName", required = false) String doctorName,
                                  @ApiParam(value = "病人ID", required = false) @RequestParam(value = "brid", required = false) String brid,
                                  @ApiParam(value = "科室ID", required = false) @RequestParam(value = "departmentId", required = false) String departmentId,
                                  @ApiParam(value = "科室名称", required = false) @RequestParam(value = "department", required = false) String department,
                                  @ApiParam(value = "his的号码 HM", required = false) @RequestParam(value = "hm", required = false) String hm,
                                  @ApiParam(value = "项目id", required = false) @RequestParam(value = "xmid", required = false) String xmid,


            @ApiParam(value = "预约的医生ID 是his系统存的医生id,即h_id", required = false) @RequestParam(value = "doctorId", required = false) String doctorId,
            @ApiParam(value = "预约的时间段", required = false) @RequestParam(value = "timeInterval", required = false) String timeInterval,
            @ApiParam(value = "预约时间", required = false) @RequestParam(value = "agreedTime", required = false) Long agreedTime
            ) throws Exception {



        //验证br_id 是否存在..
        if(brid!=null && !"".equals(brid)){
            if(patientInfoService.queryByBrIdAndAccountId(brid,accountId) == null)
                return ResponseWrapper().addMessage("该病人id不存在，请检查").ExeFaild(ReturnCodeEnum.FAILED.getCode());
        }



        // 10.17  在此处加入订单。。
        RegisterCompleteVO completeVO = registerService.addRegister(orderNmuer,accountId, brid, departmentId, doctorId,xmid, agreedTime,timeInterval,doctorName,price,subject,brName,department,hm);




        //医生挂号源已满
        if(completeVO.getState() == RegisterErrCodeEnum.DOCTOR_FULL.getCode()){
            return ResponseWrapper().addMessage(RegisterErrCodeEnum.DOCTOR_FULL.getDesc()).ExeSuccess(ReturnCodeEnum.FAILED.getCode());
        }
        //订单状态不正确，不是待支付，或者不是挂号订单  （只传订单号时）
        if(completeVO.getState() == RegisterErrCodeEnum.ORDER_STATE_NOT_MATCH.getCode()){
            return ResponseWrapper().addMessage(RegisterErrCodeEnum.ORDER_STATE_NOT_MATCH.getDesc()).ExeSuccess(ReturnCodeEnum.FAILED.getCode());

        }

        //病人信息和挂号类别不符
        if(completeVO.getState() == RegisterErrCodeEnum.PATIENT_NOT_MATCH.getCode()){
            return ResponseWrapper().addMessage(RegisterErrCodeEnum.PATIENT_NOT_MATCH.getDesc()).ExeSuccess(ReturnCodeEnum.FAILED.getCode());

        }

        //病人信息和挂号号源相冲突 例如同一病人同一天同一科室同一医生不能挂多次
        if(completeVO.getState() == RegisterErrCodeEnum.LIMIT_FOR_PATIENT.getCode()){
            return ResponseWrapper().addMessage(RegisterErrCodeEnum.LIMIT_FOR_PATIENT.getDesc()).ExeSuccess(ReturnCodeEnum.FAILED.getCode());

        }

        //该笔订单与操作用户不符，不能继续
        if(completeVO.getState() == RegisterErrCodeEnum.ORDER_NUMBER_NOT_MATCH_ACCOUNT.getCode()){
            return ResponseWrapper().addMessage(RegisterErrCodeEnum.ORDER_NUMBER_NOT_MATCH_ACCOUNT.getDesc()).ExeSuccess(ReturnCodeEnum.FAILED.getCode());

        }
        //订单不存在
        if(completeVO.getState() == RegisterErrCodeEnum.ORDER_NOT_EXIST.getCode()){
            return ResponseWrapper().addMessage(RegisterErrCodeEnum.ORDER_NOT_EXIST.getDesc()).ExeSuccess(ReturnCodeEnum.FAILED.getCode());

        }

        jpushSupport.sendPush(accountService.queryAccountById(accountId).getPushId(), Constant.REGISTER_SUCCESS_MGS);
        return ResponseWrapper().addData(completeVO).addMessage("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询预约信息 ", response = RegisterVO.class)
    @RequestMapping(value = "queryRegister", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryRegister(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
                                    @ApiParam(value = "预约ID", required = false) @RequestParam(value = "registerId", required = false) Long registerId,
                                    @ApiParam(value = "状态 0：已完成(已付款)； 1：待付款 ；2：已取消", required = false) @RequestParam(value = "status", required = false) Integer status,
                                    @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                    @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                    @ApiParam(value = "排序", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
                                    @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception {

        List<RegisterVO> registerVOList = new ArrayList<>();
        PageInfo pageInfo = null;
        if( status != null && status  == 2){
            pageInfo =  registerService.queryRegisterPageInfo(registerId, accountId, status, EnableEnum.INVALID.getCode(), Paging.create(pageNum, pageSize, "id", true));

        }else {
            pageInfo =  registerService.queryRegisterPageInfo(registerId, accountId, status, EnableEnum.EFFECTIVE.getCode(), Paging.create(pageNum, pageSize, "id", true));

        }
        List<Register> registerList = pageInfo.getList();

        List<Account> accountList = accountService.queryAccount(accountId, null, null);
        for (Register register : registerList) {
            RegisterVO registerVO = new RegisterVO();
            //List<Doctor> doctors = doctorService.queryDoctor(null, String.valueOf(register.getDoctorId()),null, null,null, null);

            //OrderVO orderVO = orderInfoService.queryOrderInfoById(register.getOrderId());
            OrderInfo orderInfo = orderInfoService.findOrderById(register.getOrderId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            register.setPayType(orderInfo.getPayType());

            if(orderInfo.getPayTime()!=null) {
                register.setCompleteTime(orderInfo.getPayTime());
                register.setPayTime(orderInfo.getPayTime());
                }
            register.setPrice(orderInfo.getPrice());
            //register.setCountDownTime(register.getCreateTime().getTime()+30*60*1000L-new Date().getTime()>0?register.getCreateTime().getTime()+30*60*1000L-new Date().getTime():0);
            register.setOrderCode(orderInfo.getOrderNumber());
            register.setDiscount(orderInfo.getDiscount());
            register.setLocation(Constant.HOSPITAL_LOCATION);
            Doctor doctor = doctorService.queryDoctorByHid(register.getDoctorId());
            registerVO.setRegister(register);
            registerVO.setDoctor(doctor);
            accountList.get(0).setPasswd(null);
            registerVO.setAccount(accountList.size() > 0 ? accountList.get(0) : null);
            Department department = new Department();
            department.setName(register.getDepartment());
            department.setId(Long.valueOf(register.getDepartmentId()));
            registerVO.setDepartment(department);
            PatientInfo patientInfo = patientInfoService.queryByBrIdAndAccountId(register.getBrId(),accountId);


            registerVO.setPatientInfo(patientInfo);
            registerVOList.add(registerVO);
        }
        pageInfo.setList(registerVOList);
        return ResponseWrapper().addData(pageInfo).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }



    @ApiOperation(value = "取消预约  针对已付款的预约挂号订单，退款并同步到his退号")
    @RequestMapping(value = "failureRegister", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject failureRegister(
            @ApiParam(value = "预约ID", required = true) @RequestParam(value = "registerId", required = true) Long registerId
    ) throws Exception {
        Preconditions.checkNotNull(registerId,"请传入必须的参数：registerId");

        Preconditions.checkState(registerService.checkIsPayedRegister(registerId),"该订单不是已付款的预约订单，请检查");

        Integer status = registerService.failureRegister(registerId);
        switch (status){
            case 0:
                return ResponseWrapper().addData("取消预约成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
            case 1:
                return ResponseWrapper().addData("取消预约失败").ExeFaild(ReturnCodeEnum.FAILED.getCode());
        }
        return ResponseWrapper().addData("取消预约失败.").ExeFaild(ReturnCodeEnum.FAILED.getCode());

    }


    @ApiOperation(value = "锁定号源")
    @RequestMapping(value = "lock", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject lock(
            @ApiParam(value = "预约ID", required = true) @RequestParam(value = "registerId", required = true) Long registerId
    ) throws Exception {
        Preconditions.checkNotNull(registerId,"请传入必须的参数：registerId");

        registerService.failureRegister(registerId);
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }



    @ApiOperation(value = "admin 查询预约信息 ", response = RegisterVO.class)
    @RequestMapping(value = "queryRegisterAdmin", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryRegisterAdmin(
                                         @ApiParam(value = "开始时间", required = false) @RequestParam(value = "startTime", required = false) Long startTime,
                                         @ApiParam(value = "结束时间", required = false) @RequestParam(value = "endTime", required = false) Long endTime,
                                         @ApiParam(value = "查询关键字", required = false) @RequestParam(value = "key", required = false) String key,
                                         @ApiParam(value = "医生id", required = false) @RequestParam(value = "doctorId", required = false) Long doctorId,
                                         @ApiParam(value = "科室id", required = false) @RequestParam(value = "departmentId", required = false) Long departmentId,
                                         @ApiParam(value = "预约状态 过期未到诊 1，正常就诊 2 ，预约就诊 3", required = false) @RequestParam(value = "status", required = false) Integer status,
                                        @ApiParam(value = "页数", required = false) @RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
                                    @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws Exception {

        PageInfo<RegisterAdminVO> page  = registerService.queryRegisterAdmin(key,startTime,endTime,doctorId,departmentId,status,startPage,pageSize);
        return ResponseWrapper().addMessage("查询成功").addData(page).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }
}

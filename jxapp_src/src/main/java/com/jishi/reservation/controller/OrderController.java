package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.*;
import com.jishi.reservation.dao.models.*;
import com.jishi.reservation.service.*;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.bean.ConfirmOrder;
import com.jishi.reservation.service.his.bean.ConfirmRegister;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/order")
@Slf4j
@Api(description = "订单相关接口")
public class OrderController extends MyBaseController {

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    HisOutpatient hisOutpatient;


    @Autowired
    AccountService accountService;

    @Autowired
    RegisterService registerService;


    @ApiOperation(value = "确认订单 两个参数传递其中一个")
    @RequestMapping(value = "confirmOrder", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sureOrder(
            @ApiParam(value = "订单id", required = false) @RequestParam(value = "orderId", required = false) Long orderId,
            @ApiParam(value = "订单编号", required = false) @RequestParam(value = "orderNumber", required = false) String orderNumber
    ) throws Exception {

        //执行his确认订单操作..
        //confirm.modify
        OrderVO orderVO = orderInfoService.queryOrderVoById(orderId,orderNumber);

        ConfirmRegister confirmRegister = orderInfoService.returnConfirmRegister(orderId,orderNumber);
        log.info("处理his的确认订单接口");
        ConfirmOrder confirmOrder = hisOutpatient.confirmRegister(confirmRegister);
        if(confirmOrder!=null){
            log.info("his系统处理成功，更新自己系统数据.");
            Integer status = orderInfoService.confirmOrderHis(orderId, orderNumber, confirmOrder);
            switch (status){
                case 200:
                    return ResponseWrapper().addData(orderVO).addMessage("确认成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
                case 406:
                    return ResponseWrapper().addMessage("该订单尚未付款，不能确认").ExeFaild(ReturnCodeEnum.FAILED.getCode());
            }
        }else {
            return  ResponseWrapper().addMessage("his系统订单确认失败").ExeFaild(ReturnCodeEnum.FAILED.getCode());

        }

        return ResponseWrapper().addMessage("系统错误").ExeFaild(ReturnCodeEnum.ERR.getCode());
    }



    @ApiOperation(value = "通过订单id查询相关信息",response = OrderVO.class)
    @RequestMapping(value = "query", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryOrder(
            @ApiParam(value = "订单ID") @RequestParam(value = "orderId", required = true) Long orderId
            ) throws Exception {

        Preconditions.checkNotNull(orderId,"请传入合适的参数：orderId");
        OrderVO orderVO = orderInfoService.queryOrderVoById(orderId,null);

        return ResponseWrapper().addData(orderVO).addMessage("查询成功").ExeSuccess(200);

    }


    @ApiOperation(value = "查询订单列表页  全部，1 待支付，2 已取消，0 预约成功 ",response = OrderListVO.class)
    @RequestMapping(value = "queryList", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryOrderList(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "状态 全部 不传，1 待支付，2 已取消，0 预约成功 ", required = false)
            @RequestParam(value = "status", required = false) Integer status,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "排序 ", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc
    ) throws Exception {

        List<OrderListVO> voList = new ArrayList<>();
        PageInfo pageInfo = orderInfoService.queryOrderList(accountId,status, EnableEnum.EFFECTIVE.getCode(), Paging.create(pageNum, pageSize, orderBy, desc));
        List<OrderInfo> orderList = pageInfo.getList();
        for (OrderInfo orderInfo : orderList) {
            Register register = registerService.queryByOrderId(orderInfo.getId());
            OrderListVO vo = new OrderListVO();
            vo.setOrderId(orderInfo.getId());
            vo.setAgreeTime(register.getAgreedTime());
            vo.setDepartment(register.getDepartment());
            vo.setStatus(orderInfo.getStatus());
            vo.setDoctorName(register.getDoctorName());
            vo.setPatientName(register.getPatientName());

            voList.add(vo);
        }

        return ResponseWrapper().addData(voList).addMessage("查询成功").ExeSuccess(200);

    }

}

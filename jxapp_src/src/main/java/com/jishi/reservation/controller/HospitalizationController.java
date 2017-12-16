package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.HospitalizationInfoVO;
import com.jishi.reservation.controller.protocol.OrderVO;
import com.jishi.reservation.controller.protocol.PrePaymentRecordVO;
import com.jishi.reservation.dao.models.OrderInfo;
import com.jishi.reservation.service.AccountService;
import com.jishi.reservation.service.HospitalizationService;
import com.jishi.reservation.service.OrderInfoService;
import com.jishi.reservation.service.PatientInfoService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.enumPackage.SuccessEnum;
import com.jishi.reservation.service.his.bean.DepositBalanceDetail;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.DateTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hospitalization")
@Slf4j
@Api(description = "住院相关接口")
public class HospitalizationController extends MyBaseController {

    @Autowired
    HospitalizationService hospitalizationService;

    @Autowired
    AccountService accountService;

    @Autowired
    PatientInfoService patientInfoService;

    @Autowired
    OrderInfoService orderInfoService;



    //selectTotalPayDetail
    @ApiOperation(value = "查询住院费用清单", response = HospitalizationService.PayItem.class)
    @RequestMapping(value = "queryHospitalDetail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryPrepay(
                @ApiParam(value = "brId", required = false) @RequestParam(value = "brId", required = false) String brId,
                @ApiParam(value = "入院的次数", required = false) @RequestParam(value = "rycs", required = false) Integer rycs,
                @ApiParam(value = "页数", required = false) @RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
                @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) throws Exception {

        if(startPage == 0){
            startPage =1;

        }
        if(pageSize ==0 ){
            pageSize = 100;
        }

        List<HospitalizationService.PayItem> list = hospitalizationService.queryPrepayDetail(brId, rycs);
        PageInfo<HospitalizationService.PayItem> page = hospitalizationService.wrapPage(list,startPage,pageSize);

        return ResponseWrapper().addMessage("ok").addData(page).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "查询住院人历史的住院信息 分页  by token", response = HospitalizationInfoVO.class)
    @RequestMapping(value = "queryAllInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryAllInfo(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "状态 0：查询在院记录  1：查询所有记录") @RequestParam(value = "status",required = false,defaultValue = "1") Integer status,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) throws Exception {

        List<HospitalizationInfoVO> list = new ArrayList<>();
        PageInfo<HospitalizationInfoVO> page = new PageInfo<>();

        if(startPage == 0){
            startPage =1;

        }
        if(pageSize ==0 ){
            pageSize = 100;
        }


        List<String> brIdList = patientInfoService.queryBrIdByAccountId(accountId);
        log.info("该账号拥有的病人id:"+JSONObject.toJSONString(brIdList));
        for (String brId : brIdList) {
            List<DepositBalanceDetail> depositBalanceDetails = hospitalizationService.queryAllInfo(brId);
            if (depositBalanceDetails != null){
                for (DepositBalanceDetail depositBalanceDetail : depositBalanceDetails) {

                  if(status == 0){
                      log.info("查询在院记录");
                      if(depositBalanceDetail.getZyzt().equals("1")){
                          list.add(getHospitalizationInfoVO(depositBalanceDetail,brId));
                      }
                  }else {
                      log.info("查询所有记录");
                      list.add(getHospitalizationInfoVO(depositBalanceDetail,brId));
                  }

            }

            }
        }

        page = patientInfoService.wrapListToPage(list,startPage,pageSize);
        return ResponseWrapper().addData(page).addMessage("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "查询住院人住院信息", response = HospitalizationInfoVO.class)
    @RequestMapping(value = "queryInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryInfo(
            @ApiParam(value = "brId", required = false) @RequestParam(value = "brId", required = false) String brId,
            @ApiParam(value = "入院的次数", required = false) @RequestParam(value = "rycs", required = false) Integer rycs
    ) throws Exception {
        DepositBalanceDetail depositBalanceDetail = hospitalizationService.queryInfo(brId, rycs,null, null);
        if (depositBalanceDetail == null)
            return ResponseWrapper().addData(null).addMessage("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
        HospitalizationInfoVO vo = getHospitalizationInfoVO(depositBalanceDetail,brId);
        return ResponseWrapper().addData(vo).addMessage("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "获取预交余额", response = HospitalizationInfoVO.class)
    @RequestMapping(value = "queryPrePayment", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryPrePayment(
            @ApiParam(value = "brId", required = true) @RequestParam(value = "brId", required = true) String brId,
            @ApiParam(value = "入院的次数", required = true) @RequestParam(value = "rycs", required = true) Integer rycs
    ) throws Exception {
       String prePayment = hospitalizationService.queryPrePayment(brId, rycs);

        return ResponseWrapper().addData(prePayment).addMessage("查询成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }



    @ApiOperation(value = "获取预交款缴款记录", response = HospitalizationInfoVO.class)
    @RequestMapping(value = "queryPaymentRecord", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryPaymentRecord(
            @ApiParam(value = "brId", required = true) @RequestParam(value = "brId", required = true) String brId,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) throws Exception {

        PageInfo<PrePaymentRecordVO> page = orderInfoService.queryPrePayment(brId,startPage,pageSize);
        return ResponseWrapper().addData(page).addMessage("查询成功").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "生成 预交款订单", response = HospitalizationInfoVO.class)
    @RequestMapping(value = "generatePrepaymentOrder", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject generatePrepaymentOrder(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "预交的名称 eg:住院预交款", required = true) @RequestParam(value = "subject", required = true) String subject,
            @ApiParam(value = "交易的金额", required = true) @RequestParam(value = "price", required = true) BigDecimal price,
            @ApiParam(value = "brId", required = true) @RequestParam(value = "brId", required = true) String brId,
            @ApiParam(value = "入院的次数", required = true) @RequestParam(value = "rycs", required = true) Integer rycs
    ) throws Exception {

        OrderInfo orderInfo = orderInfoService.generatePrepayment(subject, price, accountId, brId, rycs);

        return ResponseWrapper().addData(orderInfo).addMessage("订单生成成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }



    @ApiOperation(value = "预交款订单  确认订单，同步到his", response = OrderVO.class)
    @RequestMapping(value = "confirmPrePayment", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject confirmPrePayment(@ApiIgnore() @RequestAttribute(value= Constant.ATTR_LOGIN_ACCOUNT_ID) Long accountId,
            @ApiParam(value = "订单号", required = true) @RequestParam(value = "orderNumber", required = true) String orderNumber
            ) throws Exception {

        //PrePayment.Pay.Modify
        OrderVO vo = hospitalizationService.confirmPrePayment(orderNumber, accountId);
        if (vo.getStatus().equals(SuccessEnum.FAILED.getCode())){
            return ResponseWrapper().addMessage("确认失败!").addData(vo).ExeFaild(ReturnCodeEnum.FAILED.getCode());

        } else {
            return ResponseWrapper().addData(vo).addMessage("确认成功!").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

        }


    }


    private HospitalizationInfoVO getHospitalizationInfoVO(DepositBalanceDetail depositBalanceDetail,String brId) throws Exception {
        HospitalizationInfoVO hospitalizationInfoVO = new HospitalizationInfoVO();
        hospitalizationInfoVO.setState(depositBalanceDetail.getZyzt());
        hospitalizationInfoVO.setName(depositBalanceDetail.getJbxx().getXm());
        hospitalizationInfoVO.setKs(depositBalanceDetail.getJbxx().getZyks());
        hospitalizationInfoVO.setZzys(depositBalanceDetail.getJbxx().getZzys());
        hospitalizationInfoVO.setRysj(DateTool.format(depositBalanceDetail.getJbxx().getRysj(), "yyyy-MM-dd"));
        hospitalizationInfoVO.setCysj(DateTool.format(depositBalanceDetail.getJbxx().getCysj(), "yyyy-MM-dd"));
        hospitalizationInfoVO.setSyje(depositBalanceDetail.getFyxx().getWjfy().getSyje());
        hospitalizationInfoVO.setYjje(depositBalanceDetail.getFyxx().getWjfy().getYjye());
        hospitalizationInfoVO.setYyje(depositBalanceDetail.getFyxx().getYjfy().getZyfy());
        hospitalizationInfoVO.setRycs(depositBalanceDetail.getZycs());
        hospitalizationInfoVO.setYujiaojine(depositBalanceDetail.getYujiaojine());
        hospitalizationInfoVO.setBrid(brId);
        hospitalizationInfoVO.setZyh(depositBalanceDetail.getZyh());

        //todo
        hospitalizationInfoVO.setPayTime(new Date());
        hospitalizationInfoVO.setCreateTime(new Date());
        return hospitalizationInfoVO;
    }

}

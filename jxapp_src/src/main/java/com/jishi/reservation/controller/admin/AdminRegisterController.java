package com.jishi.reservation.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.protocol.RegisterAdminVO;
import com.jishi.reservation.controller.protocol.RegisterVO;
import com.jishi.reservation.service.RegisterService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/ad/register")
@Slf4j
@Api(description = "后台管理平台的预约接口")
public class AdminRegisterController extends MyBaseController {



    @Autowired
    RegisterService registerService;

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

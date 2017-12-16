package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.dao.models.Department;
import com.jishi.reservation.service.DepartmentService;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/department")
@Slf4j
@Api(description = "科室相关接口")
public class DepartmentController extends MyBaseController {

    @Autowired
    DepartmentService departmentService;

    @ApiOperation(value = "查询全部科室",response=Department.class)
    @RequestMapping(value = "queryAllDepartment", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryAllDepartment(
            @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "排序", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception {
        List<Department> departmentList = departmentService.queryAllDepartment();
        return ResponseWrapper().addData(departmentList).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }




}

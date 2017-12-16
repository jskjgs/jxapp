package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.DateVO;
import com.jishi.reservation.controller.protocol.DoctorVO;
import com.jishi.reservation.dao.models.Department;
import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.service.DoctorService;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.bean.RegisteredNumberInfo;
import com.jishi.reservation.service.support.AliOssSupport;
import com.jishi.reservation.service.support.DateSupport;
import com.jishi.reservation.util.Helpers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/doctor_i")
@Slf4j
@Api(description = "医生相关接口")
public class DoctorController extends MyBaseController {

    @Autowired
    DoctorService doctorService;


    @Autowired
    AliOssSupport ossSupport;


    @Autowired
    HisOutpatient hisOutpatient;


    @ApiOperation(value = "查询全部医生",response=DoctorVO.class)
    @RequestMapping(value = "queryAllDoctor", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryAllDoctor(
            @ApiParam(value = "科室ID", required = false) @RequestParam(value = "departmentId", required = false) Long departmentId,
            @ApiParam(value = "查询的名字", required = false) @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "排序", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception {
        List<DoctorVO> doctorVOList = new ArrayList<>();
        PageInfo doctors = doctorService.queryDoctorPageInfo(null,name,departmentId != null?String.valueOf(departmentId):null,null,EnableEnum.EFFECTIVE.getCode(),Paging.create(pageNum,pageSize,orderBy,desc));
        List<Doctor> doctorList = doctors.getList();
        for(Doctor doctor : doctorList){
            doctor.setIsTop(doctor.getOrderNumber().equals(0)?0:1);
            DoctorVO doctorVO = new DoctorVO();
            doctorVO.setDoctor(doctor);

            doctorVOList.add(doctorVO);
        }
        doctors.setList(doctorVOList);
        return ResponseWrapper().addData(doctors).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "查询医生",response=DoctorVO.class)
    @RequestMapping(value = "queryDoctor", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryDoctor(
            @ApiParam(value = "医生ID", required = false) @RequestParam(value = "doctorId", required = false) Long doctorId,
            @ApiParam(value = "his存的医生ID", required = false) @RequestParam(value = "hDoctorId", required = false) String hDoctorId,
            @ApiParam(value = "医生名称", required = false) @RequestParam(value = "doctorName", required = false) String doctorName,
            @ApiParam(value = "类型（0 普通医生 1 专家", required = false) @RequestParam(value = "type", required = false) String type) throws Exception {
        if(Helpers.isNullOrEmpty(doctorId) && Helpers.isNullOrEmpty(doctorName) && Helpers.isNullOrEmpty(type))
            throw new Exception("查询参数不能全部为空");
        DoctorVO doctorVO = new DoctorVO();
        List<Doctor> doctors = doctorService.queryDoctor(doctorId,hDoctorId,doctorName,null,null,EnableEnum.EFFECTIVE.getCode());
        if(doctors.size()>0){
            doctorVO.setDoctor(doctors.get(0));
            //doctorVO.setDepartmentList(departmentService.batchQueryDepartment(JSONObject.parseArray(doctors.get(0).getDepartmentIds(),String.class)));
        }
        return ResponseWrapper().addData(doctorVO).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "根据科室查找医生 支持分页",response=DoctorVO.class)
    @RequestMapping(value = "queryDoctorByDepartment", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryDoctorByDepartment(
            @ApiParam(value = "科室ID", required = true) @RequestParam(value = "departmentId", required = true) Long departmentId,
            @ApiParam(value = "查询的名字", required = false) @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(value = "排序", required = false) @RequestParam(value = "orderBy", required = false) String orderBy,
            @ApiParam(value = "是否是倒排序", required = false) @RequestParam(value = "desc", required = false) Boolean desc) throws Exception {
        if(Helpers.isNullOrEmpty(departmentId))
            throw new Exception("请传入有效的参数");
        PageInfo doctors = doctorService.queryDoctorPageInfo(null,name,String.valueOf(departmentId),null,EnableEnum.EFFECTIVE.getCode(),Paging.create(pageNum,pageSize,orderBy,desc));

        return ResponseWrapper().addData(doctors).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }

    @ApiOperation(value = "返回时间列表  最近五天的（暂时）")
    @RequestMapping(value = "dateList", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject dateList(){

        List<DateVO> dateVOList = DateSupport.generateTimeInteval();



        return ResponseWrapper().addMessage("返回时间列表").addData(dateVOList).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }

    @ApiOperation(value = "根据返回时间列表  最近五天的（暂时）")
    @RequestMapping(value = "dateListByDoctorId", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject dateList(@ApiParam(value = "医生ID", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId){

        List<DateVO> dateVOList = DateSupport.generateTimeInteval();
        return ResponseWrapper().addMessage("返回时间列表").addData(dateVOList).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }

    @ApiOperation(value = "返回所有科室的所有医生",response = Department.class)
    @RequestMapping(value = "queryDepDoc", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryDepartmentAndDoctor() throws Exception {

        List<Department> departmentList = doctorService.queryDepartmentAndDoctor();
        return ResponseWrapper().addMessage("查询成功！").addData(departmentList).ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }


    @ApiOperation(value = "从his抓取医生信息入到我们自己的库")
    @RequestMapping(value = "getDoctorFromHis", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getDoctorFromHis(
    ) throws Exception {

        RegisteredNumberInfo info = hisOutpatient.queryRegisteredNumber("", "", "", "", "", "", "", "");
        if(info.getGroup().getHblist().get(0)!=null) {
            List<RegisteredNumberInfo.Hb> hbList = info.getGroup().getHblist().get(0).getHbList();

            doctorService.getDoctorFromHis(hbList);
        }
        return ResponseWrapper().addMessage("操作成功！").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

    }

}

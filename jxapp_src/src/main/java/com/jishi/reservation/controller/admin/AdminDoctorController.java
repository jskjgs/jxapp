package com.jishi.reservation.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.DoctorVO;
import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.service.DoctorService;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.enumPackage.ReturnMessageEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.bean.RegisteredNumberInfo;
import com.jishi.reservation.service.support.AliOssSupport;
import com.jishi.reservation.service.support.FileSupport;
import com.jishi.reservation.util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/ad/doctor_i")
@Slf4j
@Api(description = "后台管理平台的医生接口")
public class AdminDoctorController extends MyBaseController {




    @Autowired
    DoctorService doctorService;


    @Autowired
    AliOssSupport ossSupport;


    @Autowired
    HisOutpatient hisOutpatient;



    @ApiOperation(value = "增加医生")
    @RequestMapping(value = "addDoctor", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject addDoctor(
            @ApiParam(value = "医生名称", required = true) @RequestParam(value = "doctorName", required = true) String doctorName,
            @ApiParam(value = "类型（0 普通医生 1 专家", required = true) @RequestParam(value = "type", required = true) String type,
            @ApiParam(value = "医生的图片"  )@RequestParam(value = "file")MultipartFile file,
            @ApiParam(value = "科室", required = true) @RequestParam(value = "departmentIds", required = true) String departmentIds,
            @ApiParam(value = "医生简介", required = false) @RequestParam(value = "about", required = false) String about,
            @ApiParam(value = "职称", required = false) @RequestParam(value = "title", required = false) String title,
            @ApiParam(value = "毕业学校", required = false) @RequestParam(value = "school", required = false) String school,
            @ApiParam(value = "位置", required = false) @RequestParam(value = "position", required = false) String position,
            @ApiParam(value = "擅长的介绍", required = false) @RequestParam(value = "goodDescribe", required = false) String goodDescribe) throws Exception {

        Preconditions.checkNotNull(doctorName,"请传入必须的参数：doctorName");
        Preconditions.checkNotNull(file,"请传入必须的参数：file");
        Preconditions.checkNotNull(departmentIds,"请传入必须的参数：departmentIds");

        //上传之前，校验文件格式和大小
        if(FileSupport.checkImageFile(file.getOriginalFilename(), file)){
            String headPortrait = ossSupport.uploadImage(file, Constant.DOCTOR_PATH);
            doctorService.addDoctor(doctorName,type,headPortrait,departmentIds,about,title,school,goodDescribe);
            return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
        }else {
            return ResponseWrapper().addMessage(ReturnMessageEnum.FILE_NOT_FIX.getMessage()).ExeFaild(ReturnCodeEnum.FAILED.getCode());
        }

    }




    @ApiOperation(value = "修改医生信息")
    @RequestMapping(value = "modifyDoctor", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyDoctor(
            @ApiParam(value = "医生ID", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId,
            @ApiParam(value = "医生名称", required = false) @RequestParam(value = "doctorName", required = false) String doctorName,
            @ApiParam(value = "类型（0 普通医生 1 专家", required = false) @RequestParam(value = "type", required = false) String type,
            @ApiParam(value = "医生的图片" ,required = false)@RequestParam(value = "file",required = false)MultipartFile file,
            @ApiParam(value = "医生简介", required = false) @RequestParam(value = "describe", required = false) String describe,
            @ApiParam(value = "职称", required = false) @RequestParam(value = "title", required = false) String title,
            @ApiParam(value = "毕业学校", required = false) @RequestParam(value = "school", required = false) String school,
            @ApiParam(value = "擅长的介绍", required = false) @RequestParam(value = "goodDescribe", required = false) String goodDescribe) throws Exception {
        Preconditions.checkNotNull(doctorId,"请传入必须的参数：doctorId");

        if(file!=null){
            if(FileSupport.checkImageFile(file.getOriginalFilename(), file)) {
                String headPortrait = ossSupport.uploadImage(file, Constant.DOCTOR_PATH);
                doctorService.modifyDoctor(doctorId, doctorName, type, headPortrait, describe, title, school, goodDescribe, null);
            }else {
                return ResponseWrapper().addMessage(ReturnMessageEnum.FILE_NOT_FIX.getMessage()).ExeFaild(ReturnCodeEnum.FAILED.getCode());

            }
        }else {
            doctorService.modifyDoctor(doctorId,doctorName,type,null,describe,title,school,goodDescribe,null);

        }
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }



    @ApiOperation(value = "失效医生")
    @RequestMapping(value = "failureDoctor", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject failureDoctor(
            @ApiParam(value = "医生ID", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId
    ) throws Exception {
        //todo 这儿其他信息都传null的话，那信息都更新为空了？？
        doctorService.modifyDoctor(doctorId,null,null,null,null,null,null,null, EnableEnum.INVALID.getCode());
        return ResponseWrapper().addData("ok").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());
    }


    @ApiOperation(value = "置顶/取消置顶 医生")
    @RequestMapping(value = "topDoctor", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject topDoctor(@ApiParam(value = "医生ID", required = true) @RequestParam(value = "doctorId", required = true) Long doctorId) throws Exception {

        Preconditions.checkNotNull(doctorId,"请传入必须的参数：doctorId");

        doctorService.topDoctor(doctorId);

        return ResponseWrapper().addMessage("操作成功！").ExeSuccess(ReturnCodeEnum.SUCCESS.getCode());

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
}

package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.doraemon.base.exceptions.ShowExceptions;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.service.DoctorService;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.HisUserManager;
import com.jishi.reservation.service.his.bean.Department;
import com.jishi.reservation.service.his.bean.DepartmentList;
import com.jishi.reservation.service.his.bean.RegisteredNumberInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by zbs on 2017/8/10.
 */
@RestController
@RequestMapping("/his_doctor")
@Slf4j
@Api(description = "对接了his系统的醫生相关接口")
public class HisDoctorController extends MyBaseController {

    @Autowired
    HisOutpatient hisOutpatient;

    @Autowired
    HisUserManager hisUserManager;

    @Autowired
    DoctorService doctorService;


    @ApiOperation(value = "查询指定天数内的可挂号科室列表", response = DepartmentList.DepartmentHis.class)
    @RequestMapping(value = "queryDepartment", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryDepartment(
            @ApiParam(value = "查询天数 7") @RequestParam(value = "cxts", required = true) String cxts) throws Exception {

        Preconditions.checkNotNull(cxts, "请传入合适的参数:cxtx");
        DepartmentList departmentList = hisOutpatient.selectDepartments("", cxts, "");
        log.info(JSONObject.toJSONString(departmentList));
        //todo : zhoubinshan  需求中需要去掉急诊科的预约
        if (departmentList != null && departmentList.getKslist() != null
                && departmentList.getKslist().getList() != null) {
            List<DepartmentList.DepartmentHis> departmentHisList = new ArrayList<>();
            for (DepartmentList.DepartmentHis departmentHis : departmentList.getKslist().getList()) {
                if ("急诊科".equals(departmentHis.getMc()))
                    departmentHisList.add(departmentHis);
            }
            if(departmentHisList!= null){
                for(DepartmentList.DepartmentHis del : departmentHisList)
                    departmentList.getKslist().getList().remove(del);
            }
        }
        departmentList.getKslist().getList();
        return ResponseWrapper().addData(departmentList).addMessage("查询成功").ExeSuccess(200);
    }


    @ApiOperation(value = "获取挂号号源", response = Doctor.class)
    @RequestMapping(value = "queryRegister", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryRegister(

            @ApiParam(value = "预约时间") @RequestParam(value = "agreeTime") Long agreeTime,
            @ApiParam(value = "科室id") @RequestParam(value = "ksid", defaultValue = "") String ksid,
            @ApiParam(value = "医生id") @RequestParam(value = "ysid", defaultValue = "") String ysid,
            @ApiParam(value = "医生姓名") @RequestParam(value = "name", defaultValue = "") String name,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws Exception {
        //todo zhoubinshan 临时对代码进行过滤处理,防传入时间格式错误
        if ( agreeTime == null || String.valueOf(agreeTime).length() < 10)
             throw new ShowExceptions("时间格式不正确");
        if(agreeTime != null && String.valueOf(agreeTime).length()==10)
            agreeTime = agreeTime*100;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = sdf.format(new Date(agreeTime));
        PageInfo<Doctor> pageInfo = new PageInfo<>();
        List<Doctor> list = new ArrayList<>();
        pageInfo.setList(list);
        if (startPage < 0)
            startPage = 1;
        //要与我们自己系统的医生信息结合。
        //12-27Z再结合doctor_work排班表
        List<Doctor> selfDoctorList = doctorService.queryDoctor(null, ysid.equals("") ? null : ysid, name.equals("") ? null : name,
                ksid.equals("") ? null : ksid, String.valueOf(0), agreeTime, 0);

        //如果我们库里面无信息，就直接返回空。
        if (selfDoctorList != null && selfDoctorList.size() != 0) {
            RegisteredNumberInfo info = hisOutpatient.queryRegisteredNumber("", timeStr, "", ksid, ysid, name, "", "");
            log.info("查询日期：" + timeStr);
            log.info("查询的时候，去his拉取的医生数据:" + JSONObject.toJSONString(info));
            if (info.getGroup().getHblist().get(0) != null) {
                List<RegisteredNumberInfo.HB> hbList = info.getGroup().getHblist().get(0).getHbList();
                List<Doctor> doctorList = new ArrayList<>();
                Integer startRow = (startPage - 1) * pageSize;
                if (hbList != null) {
                    int endRow = hbList.size() < startPage * pageSize - 1 ? hbList.size() : startPage * pageSize - 1;
                    if (startPage == endRow)
                        endRow += pageSize;
                    if (endRow == 0)
                        endRow += pageSize;

                    if (hbList.size() < endRow) {
                        endRow = hbList.size();
                    }
                    //遍历从his取过来的医生信息，并与我们自己系统的医生信息做整合
                    for (int i = startRow; i < endRow; i++) {
                        Doctor doctor = new Doctor();
                        RegisteredNumberInfo.HB hb = hbList.get(i);
                        if (selfDoctorList != null && selfDoctorList.size() != 0) {
                            for (Doctor self : selfDoctorList) {
                                if (hb.getYsid().equals(self.getHId())) {
                                    doctor.setGoodDescribe(self.getGoodDescribe());
                                    doctor.setAbout(self.getAbout());
                                    doctor.setHeadPortrait(self.getHeadPortrait());

                                    doctor.setName(hb.getYs());
                                    doctor.setXmid(hb.getXmid());
                                    doctor.setHymc(hb.getHymc());
                                    doctor.setDj(hb.getDj());
                                    doctor.setYsid(hb.getYsid());
                                    doctor.setKsmc(hb.getKsmc());
                                    doctor.setHm(hb.getHm());
                                    doctor.setDepartmentId(hb.getKsid());
                                    doctor.setHId(hb.getYsid());
                                    doctor.setTitle(hb.getZc());
                                    doctorList.add(doctor);

                                }
                            }
                        }


                    }


                }
                pageInfo.setList(doctorList);
                pageInfo.setTotal(hbList != null ? hbList.size() : 0);
                pageInfo.setPages(hbList != null ? hbList.size() / pageSize + 1 : 0);
                pageInfo.setPageNum(startPage);
                pageInfo.setPageSize(pageSize);
                pageInfo.setHasNextPage(hbList != null && hbList.size() / pageSize + 1 > startPage);

                log.info(JSONObject.toJSONString(info));
            }
        }

        return ResponseWrapper().addData(pageInfo).addMessage("查询成功").ExeSuccess(200);

    }


}

package com.jishi.reservation.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.MyBaseController;
import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.service.DoctorService;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.HisUserManager;
import com.jishi.reservation.service.his.bean.DepartmentList;
import com.jishi.reservation.service.his.bean.RegisteredNumberInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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


    @ApiOperation(value = "查询指定天数内的可挂号科室列表",response = DepartmentList.DepartmentHis.class)
    @RequestMapping(value = "queryDepartment", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryDepartment(
            @ApiParam(value = "查询天数 7") @RequestParam(value = "cxts",required = true) String cxts) throws Exception {

        Preconditions.checkNotNull(cxts,"请传入合适的参数:cxtx");
        DepartmentList departmentList = hisOutpatient.selectDepartments("", cxts, "");
        log.info(JSONObject.toJSONString(departmentList));

        return ResponseWrapper().addData(departmentList).addMessage("查询成功").ExeSuccess(200);
    }


    @ApiOperation(value = "获取挂号号源",response = Doctor.class)
    @RequestMapping(value = "queryRegister", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryRegister(
            @ApiParam(value = "科室id") @RequestParam(value = "ksid",defaultValue = "") String ksid,
            @ApiParam(value = "医生id") @RequestParam(value = "ysid",defaultValue = "") String ysid,
            @ApiParam(value = "医生姓名") @RequestParam(value = "name",defaultValue = "") String name,
            @ApiParam(value = "页数", required = false) @RequestParam(value = "startPage", defaultValue = "1") Integer startPage,
            @ApiParam(value = "每页多少条", required = false) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws Exception {

        PageInfo<Doctor> pageInfo = new PageInfo<>();
        List<Doctor> list = new ArrayList<>();
        pageInfo.setList(list);
        if(startPage <0)
            startPage = 1;
        //要与我们自己系统的医生信息结合。
        List<Doctor> selfDoctorList = doctorService.queryDoctor(null, ysid.equals("")?null:ysid, name.equals("")?null:name,
                ksid.equals("")?null:ksid, String.valueOf(0), 0);

        //如果我们库里面无信息，就直接返回空。
        if(selfDoctorList != null && selfDoctorList.size() != 0) {
            RegisteredNumberInfo info = hisOutpatient.queryRegisteredNumber("", "", "", ksid, ysid, name, "", "");
            if (info.getGroup().getHblist().get(0) != null) {
                List<RegisteredNumberInfo.Hb> hbList = info.getGroup().getHblist().get(0).getHbList();
                List<Doctor> doctorList = new ArrayList<>();
                Integer startRow = (startPage - 1) * pageSize;
                if (hbList != null) {
                    int endRow = hbList.size() < startPage * pageSize - 1 ? hbList.size() : startPage * pageSize - 1;
                    log.info(hbList.size() + "~~" + (startPage * pageSize - 1));
                    if (startPage == endRow)
                        endRow += pageSize;
                    if (endRow == 0)
                        endRow += pageSize;
                    log.info("start:" + startRow);
                    log.info("end:" + endRow);
                    log.info("list:" + hbList.size());
                    if (hbList.size() < endRow) {
                        endRow = hbList.size();
                    }
                    //遍历从his取过来的医生信息，并与我们自己系统的医生信息做整合
                    for (int i = startRow; i < endRow; i++) {
                        Doctor doctor = new Doctor();
                        RegisteredNumberInfo.Hb hb = hbList.get(i);
                        if (selfDoctorList != null && selfDoctorList.size() != 0) {
                            for (Doctor self : selfDoctorList) {
                                if (hb.getYsid().equals(self.getHId())) {
                                    doctor.setGoodDescribe(self.getGoodDescribe());
                                    doctor.setAbout(self.getAbout());
                                    doctor.setHeadPortrait(self.getHeadPortrait());

                                }
                            }
                        }

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

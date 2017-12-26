package com.jishi.reservation.worker;

import com.alibaba.fastjson.JSONObject;
import com.jishi.reservation.dao.mapper.DepartmentMapper;
import com.jishi.reservation.dao.models.Department;
import com.jishi.reservation.service.DepartmentService;
import com.jishi.reservation.service.DoctorService;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.bean.DepartmentList;
import com.jishi.reservation.service.his.bean.RegisteredNumberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sloan on 2017/11/17.
 */


/**
 * 定期从his拉取医生信息到我们自己的库的worker
 */

@Component
@Slf4j
public class PullHisDoctorToLocalWorker {

    //unlockRegister
    @Autowired
    HisOutpatient hisOutpatient;

    @Autowired
    DoctorService doctorService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    DepartmentMapper departmentMapper;

    private static final Long HALF_HOUR = 60*30*1000L;


    /**
     * 每天清早5点钟来拉取  先每五分钟
     * 新增
     * 更新
     * 删除（软删除）
     *
     */
    //@Scheduled(cron = "0 0 5 * * ? ")
    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void pullHisDepartmentInfoToLocal() throws Exception {

        log.info("==============================开始HIS科室扫描入库任务==============================");

        DepartmentList departmentList = hisOutpatient.selectDepartments("", "7", "");
        List<DepartmentList.DepartmentHis> list = departmentList.getKslist().getList();

        departmentService.getDepartmentFromHis(list);
        log.info("==============================结束HIS科室扫描入库任务==============================");

    }


    /**
     * 每天清早六点钟来拉取   先每5分钟扫一次
     * 新增
     * 更新
     * 删除（软删除）
     *
     */
    //@Scheduled(cron = "0 0 6 * * ? ")
    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void pullHisDoctorInfoToLocal() throws Exception {


        log.info("==============================开始HIS医生扫描入库任务==============================");

        RegisteredNumberInfo info = hisOutpatient.queryRegisteredNumber("", "", "", "", "", "", "", "");
        if(info.getGroup().getHblist().get(0)!=null) {
            List<RegisteredNumberInfo.HB> hbList = info.getGroup().getHblist().get(0).getHbList();


            doctorService.getDoctorFromHis(hbList);
        }

        log.info("==============================结束HIS医生扫描入库任务==============================");


    }

}

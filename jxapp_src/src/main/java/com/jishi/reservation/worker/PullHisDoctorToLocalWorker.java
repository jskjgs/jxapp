package com.jishi.reservation.worker;

import com.jishi.reservation.dao.mapper.OrderInfoMapper;
import com.jishi.reservation.dao.mapper.RegisterMapper;
import com.jishi.reservation.dao.models.OrderInfo;
import com.jishi.reservation.dao.models.Register;
import com.jishi.reservation.service.DoctorService;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.enumPackage.StatusEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.bean.RegisteredNumberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    private static final Long HALF_HOUR = 60*30*1000L;
    /**
     * 每天清早六点钟来拉取
     * 新增
     * 更新
     * 删除（软删除）
     *
     */
    @Scheduled(cron = "0 0 6 * * ? ")
    //@Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    public void pullHisDoctorInfoToLocal() throws Exception {


        RegisteredNumberInfo info = hisOutpatient.queryRegisteredNumber("", "", "", "", "", "", "", "");
        if(info.getGroup().getHblist().get(0)!=null) {
            List<RegisteredNumberInfo.Hb> hbList = info.getGroup().getHblist().get(0).getHbList();


            doctorService.getDoctorFromHis(hbList);
        }

    }

}

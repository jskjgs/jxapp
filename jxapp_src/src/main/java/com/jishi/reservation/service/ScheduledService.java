package com.jishi.reservation.service;

import com.jishi.reservation.dao.mapper.ScheduledMapper;
import com.jishi.reservation.dao.models.Scheduled;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.StatusEnum;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class ScheduledService {

    @Autowired
    private ScheduledMapper scheduledMapper;


    public void addRegister(Long doctorId, Long patientinfoId, Date agreedTime) {

        Scheduled param = new Scheduled();
        param.setDoctorId(doctorId);
        param.setPatientId(patientinfoId);
        param.setStartTime(agreedTime);
        Date endTime = new Date(agreedTime.getTime()+60*60*2);
        param.setEndTime(endTime);
        param.setEnable(EnableEnum.EFFECTIVE.getCode());
        param.setStatus(StatusEnum.REGISTER_STATUS_NO_PAYMENT.getCode());

        scheduledMapper.insert(param);
    }
}

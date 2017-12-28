package com.jishi.reservation.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.dao.mapper.BannerMapper;
import com.jishi.reservation.dao.mapper.DoctorWorkMapper;
import com.jishi.reservation.dao.models.Banner;
import com.jishi.reservation.service.enumPackage.DisplayEnum;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class DoctorWorkService {

    @Autowired
    DoctorWorkMapper doctorWorkMapper;

    public boolean isExit(String hDoctorId, String workDate) {


        return doctorWorkMapper.queryByHIdAndDate(hDoctorId,workDate) != null;
    }
}

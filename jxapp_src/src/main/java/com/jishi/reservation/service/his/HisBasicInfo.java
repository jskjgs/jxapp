package com.jishi.reservation.service.his;

import com.jishi.reservation.conf.HisConfiguration;
import com.jishi.reservation.mypackage.OutPatientResponseOutPatientResult;
import com.jishi.reservation.service.his.bean.LastPrice;
import lombok.extern.log4j.Log4j;
import org.apache.axis.message.MessageElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sloan on 2017/12/22.
 */

//医生，科室等基本信息
@Log4j
@Service
public class HisBasicInfo {



    @Autowired
    private HisConfiguration hisConfiguration;

    @Autowired
    private HisTool hisTool;




}

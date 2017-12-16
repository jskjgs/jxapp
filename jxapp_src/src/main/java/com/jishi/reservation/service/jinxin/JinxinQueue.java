package com.jishi.reservation.service.jinxin;

import com.jishi.reservation.service.jinxin.bean.QueueCurrentNumber;
import com.jishi.reservation.service.jinxin.bean.QueueDetail;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 锦欣医院排队服务接口
 * Created by liangxiong on 2017/11/14.
 */
@Log4j
@Service
public class JinxinQueue {

    public List<QueueCurrentNumber> queryCurrentNumber() {
        return Collections.emptyList();
    }

    public QueueDetail queryQueueByBrId(String brId) {
        return null;
    }

    public List<QueueDetail> queryQueueByDoctorHisId(String doctorId, int length) {
        return Collections.emptyList();
    }

    public List<QueueDetail> queryQueueByDepartHisId(String departId, int length) {
        return Collections.emptyList();
    }

}

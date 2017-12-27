package com.jishi.reservation.worker;

import cn.jpush.api.push.model.PushPayload;
import com.jishi.reservation.dao.mapper.AccountMapper;
import com.jishi.reservation.dao.models.Account;
import com.jishi.reservation.dao.models.PatientInfo;
import com.jishi.reservation.service.OutpatientQueueService;
import com.jishi.reservation.service.PatientInfoService;
import com.jishi.reservation.service.jinxin.bean.QueueCurrentNumber;
import com.jishi.reservation.service.support.JpushSupport;
import com.jishi.reservation.controller.protocol.OutpatientQueueDetailVO;
import com.jishi.reservation.worker.configurator.WorkerDispatcher;
import com.jishi.reservation.worker.configurator.WorkerTypeEnum;
import com.jishi.reservation.worker.model.PushData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liangxiong on 2017/11/10.
 */
@Component
@Slf4j
public class OutpatientQueueWorker {

    @Autowired
    private JpushSupport jpushSupport;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PatientInfoService patientInfoService;

    @Autowired
    private OutpatientQueueService outpatientQueueService;

    @Autowired
    private WorkerDispatcher workerDispatcher;


    // 根据查询的已修改的医生门诊号序进行推送
    @Scheduled(cron = "0 0/10 7-23 * * ? ")
    public void doWork() throws Exception {
        if (!workerDispatcher.hasPermission(WorkerTypeEnum.WORKER_OUTPATIENT_QUEUE)) {
            return;
        }
        log.info("********************* OutpatientQueueWorker begin *********************");
        outpatientQueueService.doNoticeRegisterQueue();
        log.info("EndTime: " + new Date());
        log.info("********************* OutpatientQueueWorker End *********************");
    }

    //  测试推送接口
    //@Scheduled(cron = "0 0/2 8-22 * * ? ")
    public void doWorkTest() throws Exception {
        if (!workerDispatcher.hasPermission(WorkerTypeEnum.WORKER_OUTPATIENT_QUEUE)) {
            return;
        }
        List<OutpatientQueueDetailVO> queueDetailList = outpatientQueueService.generateTestData(4);
        if (queueDetailList == null || queueDetailList.isEmpty()) {
          return;
        }
        log.info("=================OutpatientQueueWorker begin=====================");
        log.info("BeginTime: " + new Date() + " queueDetailList: " + queueDetailList.size());

        Account account = accountMapper.queryById(26L);//26
        //Account account1 = accountMapper.queryById(24L);//24
        List<PushPayload> pushPayloadList = new ArrayList<PushPayload>();

        for (OutpatientQueueDetailVO detail : queueDetailList) {
            jpushSupport.sendMessage(account.getPushId(), PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_OUT_QUEUE_INFO, detail);
        }
        log.info("EndTime: " + new Date());
        log.info("=================OutpatientQueueWorker End=====================");
    }


}

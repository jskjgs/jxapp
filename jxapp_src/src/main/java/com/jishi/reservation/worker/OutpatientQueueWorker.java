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
import com.jishi.reservation.worker.model.PushData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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


    // 根据查询的已修改的医生门诊号序进行推送
    //@Scheduled(cron = "0 0/1 8-22 * * ? ")
    public void doWork() throws Exception {
        List<QueueCurrentNumber> queueCurrentNumberList = outpatientQueueService.queryModifiedVisitCurrentNum();
        if (queueCurrentNumberList == null || queueCurrentNumberList.isEmpty()) {
            return;
        }
        log.info("********************* OutpatientQueueWorker begin *********************");
        log.info("BeginTime: " + new Date() + "QueueCurrentNumber size: " + queueCurrentNumberList.size());
        for (QueueCurrentNumber currentNumber : queueCurrentNumberList) {
            List<OutpatientQueueDetailVO> queueDetailList = outpatientQueueService.queryQueueByDoctorHisId(currentNumber.getDoctorHisId());
            if (queueDetailList == null || queueDetailList.isEmpty()) {
                continue;
            }
            List<PushPayload> pushPayloadList = new ArrayList<PushPayload>();
            for (OutpatientQueueDetailVO detail : queueDetailList) {
                PatientInfo patientInfo = patientInfoService.queryByBrIdAndAccountId(detail.getBrId(),detail.getAccountId());
                if (patientInfo == null) {
                    log.info("当前病人未添加, brid: " + detail.getBrId());
                    continue;
                }
                Account account = accountMapper.queryById(patientInfo.getAccountId());
                if (account == null) {
                    log.warn("当前病人账号为空, brid: " + detail.getBrId());
                    continue;
                }
                String pushMessage = PushData.create().msgType(PushData.PushDataMsgTypeDef.PUSH_DATA_OUTPATIENT_QUEUE).content(detail).toJSON();
                log.info("accountId: " + account.getId() + " msg: " + pushMessage);
                pushPayloadList.add(JpushSupport.buildPushObjMessage(account.getPushId(), pushMessage));
            }
            // 批量异步推送
            jpushSupport.sendPush(pushPayloadList);
        }
        log.info("EndTime: " + new Date());
        log.info("********************* OutpatientQueueWorker End *********************");
    }

    //  测试推送接口
    //@Scheduled(cron = "0 0/2 8-22 * * ? ")
    public void doWorkTest() throws Exception {
        List<OutpatientQueueDetailVO> queueDetailList = outpatientQueueService.generateTestData(2);
        if (queueDetailList == null || queueDetailList.isEmpty()) {
          return;
        }
        log.info("=================OutpatientQueueWorker begin=====================");
        log.info("BeginTime: " + new Date() + " queueDetailList: " + queueDetailList.size());

        Account account = accountMapper.queryById(30L);//26
        //Account account1 = accountMapper.queryById(24L);//24
        List<PushPayload> pushPayloadList = new ArrayList<PushPayload>();

        for (OutpatientQueueDetailVO detail : queueDetailList) {
            String pushMessage = PushData.create().msgType(PushData.PushDataMsgTypeDef.PUSH_DATA_OUTPATIENT_QUEUE).content(detail).toJSON();
            log.info("accountId: " + account.getId() + " msg: " + pushMessage);
            pushPayloadList.add(JpushSupport.buildPushObjMessage(account.getPushId(), pushMessage));
            //pushPayloadList.add(JpushSupport.buildPushObjMessage(account1.getPushId(), pushMessage));
        }
        jpushSupport.sendPush(pushPayloadList);
        log.info("EndTime: " + new Date());
        log.info("=================OutpatientQueueWorker End=====================");
    }


}

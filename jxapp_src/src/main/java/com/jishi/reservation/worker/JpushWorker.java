package com.jishi.reservation.worker;

import com.jishi.reservation.dao.mapper.AccountMapper;
import com.jishi.reservation.dao.mapper.RegisterMapper;
import com.jishi.reservation.dao.models.Register;
import com.jishi.reservation.service.RegisterService;
import com.jishi.reservation.service.support.JpushSupport;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.DateTool;
import com.jishi.reservation.worker.model.PushData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by sloan on 2017/9/28.
 */

@Component
@Slf4j
public class JpushWorker {


    @Autowired
    public RegisterMapper registerMapper;

    @Autowired
    public JpushSupport jpushSupport;

    @Autowired
    public AccountMapper accountMapper;

    @Autowired
    public RegisterService registerService;


    @Scheduled(cron = "0 0 9 * * ? ")
    public void SendCommandPush() {

        //查找所有还没有预约的挂号信息，预约时间，与当前时间比较，
        List<Register> list = registerMapper.queryEnableRegister();
        for (Register register : list) {

            log.info("预约id:"+register.getId()+",预约时间："+register.getAgreedTime().getTime());
            if(DateTool.isToday(register.getAgreedTime().getTime())){
                // 如果预约时间和当前时间相差一天，给当天的提示
                log.info("id是"+register.getId()+"的预约是今天，发送提醒推送通知");
                jpushSupport.sendNotification(accountMapper.queryById(register.getAccountId()).getPushId(), Constant.REGISTER_TODAY_MSG,
                        PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_REGISTER_TIME_INFO, registerService.generateRegisterVO(register));

            }
            if(DateTool.isToday(register.getAgreedTime().getTime()+ Constant.DAY_MS)){
                // 如果预约时间和当前时间相差两天天，给明天的提示
                log.info("id是"+register.getId()+"的预约是明天，发送提醒推送通知");
                jpushSupport.sendNotification(accountMapper.queryById(register.getAccountId()).getPushId(), Constant.REGISTER_TOMORROW_MSG,
                        PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_REGISTER_TIME_INFO, registerService.generateRegisterVO(register));
            }
        }

    }

    //@Scheduled(cron = "0 0/2 * * * ? ")
    public void SendCommandPushTest() {

        //查找所有还没有预约的挂号信息，预约时间，与当前时间比较，
        Register register = registerMapper.queryById(1363L);

        jpushSupport.sendNotification(accountMapper.queryById(24L).getPushId(), Constant.REGISTER_TODAY_MSG,
                PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_REGISTER_TIME_INFO, registerService.generateRegisterVO(register));

        jpushSupport.sendNotification(accountMapper.queryById(26L).getPushId(), Constant.REGISTER_TODAY_MSG,
                PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_REGISTER_TIME_INFO, registerService.generateRegisterVO(register));
    }

}

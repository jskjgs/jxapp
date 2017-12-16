package com.jishi.reservation.worker;

import com.jishi.reservation.dao.mapper.OrderInfoMapper;
import com.jishi.reservation.dao.mapper.RegisterMapper;
import com.jishi.reservation.dao.models.OrderInfo;
import com.jishi.reservation.dao.models.Register;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.enumPackage.StatusEnum;
import com.jishi.reservation.service.his.HisOutpatient;
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
 * 预约过期的workder，下预约订单半小时后，还没有支付的订单，都置为过期，并且去his那边解除锁号
 */

@Component
@Slf4j
public class RegisterOverdueWorker {

    //unlockRegister
    @Autowired
    HisOutpatient hisOutpatient;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    RegisterMapper registerMapper;

    private static final Long HALF_HOUR = 60*30*1000L;
    /**
     * 每30分钟扫描一次
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    @Transactional
    public void setRegisterOverdue() throws Exception {

        log.info("==============开始执行预约订单过期扫描=====================");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<OrderInfo> orderInfoList =  orderInfoMapper.queryAllWaitPayRegister();
        for (OrderInfo orderInfo : orderInfoList) {
            log.info("被检测的订单id:"+orderInfo.getId());
            Date now = new Date();
            //如果下单30分钟之后
            if(now.getTime() - HALF_HOUR > orderInfo.getCreateTime().getTime()){
                log.info("订单id是"+orderInfo.getId()+"的订单超过半小时为支付，设置为过期订单");
                orderInfo.setStatus(StatusEnum.REGISTER_STATUS_CANCEL.getCode());
                orderInfo.setEnable(EnableEnum.INVALID.getCode());
                Register register = registerMapper.queryByOrderId(orderInfo.getId());
                register.setStatus(StatusEnum.REGISTER_STATUS_CANCEL.getCode());
                register.setEnable(EnableEnum.INVALID.getCode());
                log.info("过期信息同步到his，进行解锁操作..");
                //String unlockResult = hisOutpatient.unlockRegister(register.getHm(), sdf.format(register.getAgreedTime()), register.getHx());
                String unlockResult = "todo";
                if(unlockResult!=null && !"".equals(unlockResult)){

                    orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                    registerMapper.updateByPrimaryKeySelective(register);
                    log.info("过期成功！");

                }else {
                    log.info("his 过期失败..请查询原因");
                }
            }



        }
        log.info("==========================过期结束，半小时后再来一次==================");

    }

}

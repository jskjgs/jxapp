package com.jishi.reservation.worker;

import com.jishi.reservation.dao.mapper.DiaryInfoMapper;
import com.jishi.reservation.dao.mapper.DiaryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liangxiong on 2017/12/18.
 */
@Component
@Slf4j
public class DiaryUpdateInfoWorker {

    @Autowired
    private DiaryInfoMapper diaryInfoMapper;

    @Scheduled(cron = "0 0 4 * * ? ")
    public void updateScanAndLikedNums() {
        Date begin = new Date();
        log.info("======== do updateScanandLikedNums begin: " + begin);
        diaryInfoMapper.updateScanAndLikedNumber();
        Date end = new Date();
        log.info("======== do updateScanandLikedNums end: " + end);
        log.info("======== do updateScanandLikedNums takes: " + (end.getTime() - begin.getTime()) + "ms");
    }
}

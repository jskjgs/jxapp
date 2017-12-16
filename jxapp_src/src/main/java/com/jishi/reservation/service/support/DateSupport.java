package com.jishi.reservation.service.support;

import com.jishi.reservation.controller.protocol.DateVO;
import com.jishi.reservation.controller.protocol.TimeIntervalVO;
import com.jishi.reservation.service.enumPackage.DateEnum;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.DateTool;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sloan on 2017/9/1.
 */

@Slf4j
public class DateSupport {


    public static  List<DateVO> generateTimeInteval() {
        Date now = new Date();

        Calendar morring = Calendar.getInstance();
        morring.set(Calendar.HOUR_OF_DAY, 8);
        morring.set(Calendar.MINUTE, 0);
        morring.set(Calendar.SECOND, 0);
        Date morringTime = morring.getTime();

        Date afternoonTime = new Date(morringTime.getTime() + Constant.SIX_HOURS);

        List<Date> dateList = new ArrayList<>();

        if(now.getTime() < morringTime.getTime()){
            log.info("当天早上八点之前");
            dateList.add(DateTool.getMorringTime(0));
            dateList.add(DateTool.getAfternoonTime(0));
        }
        if(now.getTime() > morringTime.getTime() && now.getTime()<afternoonTime.getTime()){
            log.info("当天八点到14点之间");
            dateList.add(DateTool.getAfternoonTime(0));

        }
        if(now.getTime()>afternoonTime.getTime()){
            log.info("当天14点之后");

        }
        for(int i = 1;i<8;i++){
            dateList.add(DateTool.getMorringTime(i));
            dateList.add(DateTool.getAfternoonTime(i));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日hh");

        List<String> dayList = new ArrayList<>() ;
        for (Date date : dateList) {
            String format = sdf.format(date).substring(0,6);
            dayList.add(format);

        }
        ArrayList<String> result = new ArrayList<String>();

        for(String s: dayList){
            if(Collections.frequency(result, s) < 1) result.add(s);
        }

        List<DateVO> dateVOList = new ArrayList<>();
        for (String day : result) {
            DateVO dateVO = new DateVO();
            dateVO.setDay(day);
            List<TimeIntervalVO> timeIntervalVOList = new ArrayList<>();
            for (Date date : dateList) {
                String format = sdf.format(date).substring(0,6);
                if(day.equals(format)){
                    TimeIntervalVO timeIntervalVO = new TimeIntervalVO();
                    timeIntervalVO.setType(sdf.format(date).substring(6,8).equals("08")? DateEnum.MORNING.getCode():DateEnum.AFTERNOON.getCode());
                    timeIntervalVO.setDate(date.getTime());
                    timeIntervalVOList.add(timeIntervalVO);

                }

            }
            dateVO.setDuring(timeIntervalVOList);
            dateVOList.add(dateVO);
        }

        return dateVOList;

    }
}

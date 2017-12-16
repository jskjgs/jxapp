package com.jishi.reservation.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zbs on 2017/7/19.
 */

public class DateTool {

    long nd = 1000 * 24 * 60 * 60;
    long nh = 1000 * 60 * 60;
    long nm = 1000 * 60;

    public static DateTool create() {
        return new DateTool();
    }

    public DateBean getLastWeek() {
        Calendar lastWeekCalendar = Calendar.getInstance();
        lastWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        lastWeekCalendar.add(Calendar.DAY_OF_WEEK, -7);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(lastWeekCalendar.get(Calendar.YEAR), lastWeekCalendar.get(Calendar.MONTH), lastWeekCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Calendar stopCalendar = Calendar.getInstance();
        lastWeekCalendar.add(Calendar.DAY_OF_WEEK, 6);
        stopCalendar.set(lastWeekCalendar.get(Calendar.YEAR), lastWeekCalendar.get(Calendar.MONTH), lastWeekCalendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

        return new DateBean(startCalendar.getTime(), stopCalendar.getTime());
    }

 //   public static void main(String[] args) throws Exception {
  //      DateBean dateBean = DateTool.create().getLastWeek();
 //       System.out.print(DateTool.create().diffDay(dateBean.getStartDate(),dateBean.getStopDate()));
 //   }

    public static Date getMorringTime(Integer days) {
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date date = calendar.getTime();
        return date;
    }

    public static Date getAfternoonTime(Integer days) {
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date date = calendar.getTime();
        return date;
    }



    public DateBean getLastMonth() {
        Calendar lastMonthCalendar = Calendar.getInstance();
        lastMonthCalendar.add(Calendar.MONTH, -1);
        int MaxDay = lastMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(lastMonthCalendar.get(Calendar.YEAR), lastMonthCalendar.get(Calendar.MONTH), 1, 0, 0, 0);
        Calendar stopCalendar = Calendar.getInstance();
        stopCalendar.set(lastMonthCalendar.get(Calendar.YEAR), lastMonthCalendar.get(Calendar.MONTH), MaxDay, 23, 59, 59);
        return new DateBean(startCalendar.getTime(), stopCalendar.getTime());
    }

    public DateBean getLastYear() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set((Calendar.getInstance().get(Calendar.YEAR) - 1), 0, 1, 0, 0, 0);
        Calendar stopCalendar = Calendar.getInstance();
        stopCalendar.set((Calendar.getInstance().get(Calendar.YEAR) - 1), 11, 31, 23, 59, 59);
        return new DateBean(startCalendar.getTime(), stopCalendar.getTime());

    }

    public DateBean getLastDay() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date());
        startCalendar.add(Calendar.DATE, -1);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        Calendar stopCalendar = Calendar.getInstance();
        stopCalendar.setTime(new Date());
        stopCalendar.add(Calendar.DATE, -1);
        stopCalendar.set(Calendar.HOUR_OF_DAY, 23);
        stopCalendar.set(Calendar.MINUTE, 59);
        stopCalendar.set(Calendar.SECOND, 59);
        return new DateBean(startCalendar.getTime(), stopCalendar.getTime());

    }

    public DateBean getDay() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date());
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        return new DateBean(startCalendar.getTime(), new Date());

    }

    public long diffMinute(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / nm;
    }

    public long diffDay(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / nd == 0 ? 1 : (diff / nd) +1;
    }


    private Date format(Date date) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return simpleDateFormat.parse(dateStr);
    }

    public static Date format(String dateStr,String format) throws Exception {
        if(dateStr == null || "".equals(dateStr) || format == null || "".equals(format))
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }

    public class DateBean {

        public DateBean() {
        }

        public DateBean(Date startDate, Date stopDate) {
            this.setStartDate(startDate);
            this.setStopDate(stopDate);
        }

        Date startDate;
        Date stopDate;

        public Date getStartDate() throws Exception {
            return format(startDate);
        }

        public Date getStopDate() throws Exception {
            return format(stopDate);
        }


        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public void setStopDate(Date stopDate) {
            this.stopDate = stopDate;
        }
    }


    //判断选择的日期是否是今天
    public static boolean isToday(long time)
    {
        return isThisTime(time,"yyyy-MM-dd");
    }
    //判断选择的日期是否是本月
    public static boolean isThisMonth(long time)
    {
        return isThisTime(time,"yyyy-MM");
    }
    private static boolean isThisTime(long time,String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        return param.equals(now);
    }


}

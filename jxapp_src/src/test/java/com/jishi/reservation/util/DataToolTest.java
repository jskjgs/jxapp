package com.jishi.reservation.util;

import org.junit.Test;

/**
 * Created by zbs on 2017/7/26.
 */
public class DataToolTest {

    @Test
    public void test() throws Exception {
        DateTool.DateBean dateBean = DateTool.create().getLastDay();
        System.out.println(dateBean.getStartDate());
        System.out.println(dateBean.getStopDate());
    }




}

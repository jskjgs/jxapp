package com.jishi.reservation.util;

import com.doraemon.base.util.RandomUtil;

import java.util.Random;

/**
 * Created by zbs on 2017/8/10.
 */
public class NewRandomUtil extends RandomUtil {

    private static String[] Num = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static String getRandomNum(int length) throws Exception {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; ++i) {
            sb.append(Num[r.nextInt(Num.length)]);
        }
        return sb.toString();
    }


}

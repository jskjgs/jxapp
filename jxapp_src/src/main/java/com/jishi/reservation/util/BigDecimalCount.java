package com.jishi.reservation.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 有关钱的计算和四舍五入
 * Created by zbs on 16/7/7.
 */
public class BigDecimalCount {

    /**
     * 除法 返回整数,并且 1=1 , 1.1=2 , 1.5 =2 , 1.6=2
     * @param one
     * @param two
     * @return
     */
    public static BigDecimal divide(BigDecimal one,Integer two){
        if(one == null || two == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal =  one.divide(new BigDecimal(two),0,BigDecimal.ROUND_UP);
        return bigDecimal;
    }

    /**
     * 除法 返回整数,并且 1=1 , 1.1=2 , 1.5 =2 , 1.6=2
     * @param one
     * @param two
     * @return
     */
    public static BigDecimal divide(BigDecimal one,Integer two,Integer decimalPlaces){
        if(one == null || two == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal =  one.divide(new BigDecimal(two),decimalPlaces,BigDecimal.ROUND_UP);
        return bigDecimal;
    }

    /**
     * 乘法
     * @param one
     * @param two
     * @return
     */
    public static BigDecimal multiply(BigDecimal one,Integer two){
        if(one == null || two == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal =  one.multiply(new BigDecimal(two));
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }


    /**
     * 乘法
     * @param one
     * @param two
     * @return
     */
    public static BigDecimal multiply(BigDecimal one,BigDecimal two){
        if(one == null || two == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = one.multiply(two);
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 乘法
     * @param values
     * @return
     */
    public static BigDecimal multiply(BigDecimal ... values){
        if(values == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = null;
        for(BigDecimal value : values){
            if(bigDecimal == null){
                bigDecimal = value;
            }else{
                bigDecimal = bigDecimal.multiply(value);
            }
        }
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal.setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * 乘法
     * @param values
     * @return
     */
    public static BigDecimal multiply(List<BigDecimal> values){
        if(values == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = null;
        for(BigDecimal value : values){
            if(bigDecimal == null){
                bigDecimal = value;
            }else{
                bigDecimal = bigDecimal.multiply(value);
            }
        }
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal.setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * 加法
     * @param one
     * @param two
     * @return
     */
    public static BigDecimal add(BigDecimal one,BigDecimal two){
        if(one == null || two == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = one.add(two);
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal.setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * 加法
     * @param one
     * @param two
     * @return
     */
    public static BigDecimal add(BigDecimal one,Integer two){
        if(one == null || two == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = one.add(new BigDecimal(two));
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal.setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * 加法
     * @param values
     * @return
     */
    public static BigDecimal add(BigDecimal ... values){
        if(values == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for(BigDecimal value : values){
            bigDecimal = bigDecimal.add(value);
        }
        return bigDecimal.setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * 加法
     * @param values
     * @return
     */
    public static BigDecimal add(List<BigDecimal> values){
        if(values == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for(BigDecimal value : values){
            bigDecimal = bigDecimal.add(value);
        }
        return bigDecimal.setScale(2,RoundingMode.HALF_UP);
    }

    /**
     * 减法
     * @param one
     * @param two
     * @return
     */
    public static  BigDecimal subtract(BigDecimal one, BigDecimal two){
        if(one == null || two == null)
            return BigDecimal.ZERO;
        BigDecimal bigDecimal = one.subtract(two);
        return bigDecimal == null ? BigDecimal.ZERO : bigDecimal.setScale(2,RoundingMode.HALF_UP);
    }
}

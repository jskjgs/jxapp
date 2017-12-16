package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import java.util.List;

/**
 * Created by zbs on 2017/10/5.
 */
@XStreamAlias("ROOT")
@Data
public class DepositBalanceLog {

    //是否在院，1-是，0-否
    @XStreamAlias("SFZY")
    Integer sfzy;
    @XStreamAlias("GROUP")
    DB2 group;

    @Data
    public class  DB2{
        @XStreamImplicit(itemFieldName="ITEM")
        List<DB3> item;
    }

    @Data
    public class DB3{
        //缴款时间
        @XStreamAlias("JKSH")
        String jksh;
        //金额
        @XStreamAlias("JE")
        String je;
        //结帐，预交(如果是结帐，则为退回病人的钱)
        @XStreamAlias("LX")
        String lx;
        //支付方式，现金、支票、支付宝等
        @XStreamAlias("ZFFS")
        String zffs;
    }
}

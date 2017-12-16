package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;

import java.util.List;

/**
 * 住院详情
 * Created by zbs on 2017/10/5.
 */
@Data
@XStreamAlias("ROOT")
public class DepositBalanceDetail {

    //基本信息
    @XStreamAlias("JBXX")
    Jbxx jbxx;
    //费用信息
    @XStreamAlias("FYXX")
    Fyxx fyxx;
    //诊断信息
    @XStreamAlias("ZDXX")
    Zdxx zdxx;
    //手术信息
    @XStreamAlias("SSXX")
    SsxxItem ssxxItem;
    //住院状态，0-出院，1-在院，2-出院未结帐
    @XStreamOmitField
    String zyzt;
    //住院次数
    @XStreamOmitField
    String zycs;

    //住院次数
    @XStreamOmitField
    String yujiaojine;

    String zyh;

    @Data
    public class Jbxx{
        //姓名
        @XStreamAlias("XM")
        String xm;
        //性别
        @XStreamAlias("XB")
        String xb;
        //年龄
        @XStreamAlias("NL")
        String nl;
        //床号
        @XStreamAlias("CH")
        String ch;
        //住院号
        @XStreamAlias("ZYH")
        String zyh;
        //住院科室
        @XStreamAlias("ZYKS")
        String zyks;
        //科室ID
        @XStreamAlias("KSID")
        String ksid;
        //主治医师
        @XStreamAlias("ZZYS")
        String zzys;
        //医生ID
        @XStreamAlias("YSID")
        String ysid;
        //责任护士
        @XStreamAlias("ZRHS")
        String zrhs;
        //入院时间
        @XStreamAlias("RYSJ")
        String rysj;
        //出院时间
        @XStreamAlias("CYSJ")
        String cysj;

    }

    @Data
    public class Fyxx{
        //未结金额
        @XStreamAlias("WJFY")
        Wjfy wjfy;
        //已结金额
        @XStreamAlias("YJFY")
        Yjfy yjfy;
    }

    @Data
    public class Wjfy{
        //已结金额
        @XStreamAlias("YJYE")
        String yjye;
        //未结金额
        @XStreamAlias("WJJE")
        String wjje;
        //剩余金额(预交余额-未结金额)
        @XStreamAlias("SYJE")
        String syje;
        //医保报销(医保预结金额)
        @XStreamAlias("YBBX")
        String ybbx;
    }

    @Data
    public class Yjfy{
        //住院总费用
        @XStreamAlias("ZYFY")
        String zyfy;
        //医保报销(医保预结金额)
        @XStreamAlias("YBBX")
        String ybbx;
        //个人支付总费用
        @XStreamAlias("GRZF")
        String grzf;
    }

    @Data
    public class Zdxx{
        //入院诊断，多个诊断用“,”间隔
        @XStreamAlias("RYZD")
        String ryzd;
        //出院诊断，多个诊断用“,”间隔
        @XStreamAlias("CYZD")
        String cyzd;
        //并发症，多个诊断用“,”间隔
        @XStreamAlias("BFZ")
        String bfz;
    }

    @Data
    public class SsxxItem{
        @XStreamImplicit(itemFieldName="ITEM")
        List<Ssxx> ssxx;
    }

    @Data
    public class Ssxx{
        @XStreamAlias("SSRQ")
        String ssrq;
        @XStreamAlias("SSMC")
        String ssmc;
    }
}

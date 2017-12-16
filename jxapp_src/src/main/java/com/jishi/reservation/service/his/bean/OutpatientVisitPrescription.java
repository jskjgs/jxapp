package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * 指定就诊中的单据信息
 * Created by liangxiong on 2017/10/28.
 */
@XStreamAlias("ROOT")
@Data
public class OutpatientVisitPrescription {

    //prescription
    @XStreamImplicit(itemFieldName="GROUP")
    private List<Prescription> prescriptionList;

    @Data
    //@XStreamAlias("GROUP")
    public class Prescription {
        //单据号
        @XStreamAlias("DJH")
        private String djh;
        //日期
        @XStreamAlias("RQ")
        private String rq;
        //医生姓名
        @XStreamAlias("YS")
        private String ys;
        //单据费用
        @XStreamAlias("ZFY")
        private String zfy;
        //诊断信息
        @XStreamAlias("ZD")
        private String zd;
        @XStreamImplicit(itemFieldName="ITEM")
        private List<PrescriptionItem> itemList;
    }

    @Data
    //@XStreamAlias("ITEM")
    public class PrescriptionItem {
        //类别，处方、检验、检查等等
        @XStreamAlias("LB")
        private String lb;
        @XStreamImplicit(itemFieldName="GROUP")
        private List<PrescriptionYF> group;
    }

    @Data
    public class PrescriptionYF {
        //用法
        @XStreamAlias("YF")
        private String yf;
        @XStreamImplicit(itemFieldName="MX")
        private List<PrescriptionMX> mxList;
    }

    @Data
    //明细
    //@XStreamAlias("MX")
    public class PrescriptionMX {
        //名称
        @XStreamAlias("MC")
        private String mc;
        //规格(LB为处方时返回)
        @XStreamAlias("GG")
        private String gg;
        //单量(LB为处方时返回)
        @XStreamAlias("DL")
        private String dl;
        //用量(LB为处方时返回)
        @XStreamAlias("YL")
        private String yl;
        //单位(LB为处方时返回)
        @XStreamAlias("DW")
        private String dw;
        //病历ID(LB为检验、检查时返回)，HIS中报告的唯一标识
        @XStreamAlias("BLID")
        private String blid;
        //报告来源，1-HIS，2-外检报告(LB为检验、检查时返回)
        @XStreamAlias("BGLY")
        private String bgly;
        //报告来源说明(LB为检验、检查时返回)
        @XStreamAlias("BGLYSM")
        private String bglysm;
    }
}

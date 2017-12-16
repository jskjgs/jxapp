package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.List;

/**
 * 历次门诊就诊记录
 * Created by liangxiong on 2017/10/28.
 */
@XStreamAlias("ROOT")
@Data
public class OutpatientVisitRecord {

    //无此节点表示没找到记录
    @XStreamAlias("INFOLIST")
    private List<VisitRecord> infolist;

    @Data
    public class VisitRecordInfo {
        @XStreamAlias("INFO")
        private List<VisitRecord> list;
    }

    @Data
    @XStreamAlias("INFO")
    public class VisitRecord {
        //日期
        @XStreamAlias("RQ")
        private String rq;
        //挂号单号
        @XStreamAlias("GHDH")
        private String ghdh;
        //诊断信息
        @XStreamAlias("ZDXX")
        private String zdxx;
        //就诊科室
        @XStreamAlias("JZKS")
        private String jzks;
        //就诊科室ID
        @XStreamAlias("JZKSID")
        private String jzksid;
        //主治医生
        @XStreamAlias("YS")
        private String ys;
        //费用金额
        @XStreamAlias("JE")
        private String je;
        //
        @XStreamAlias("XMLIST")
        private List<RecordMX> mxlist;
    }

    @Data
    public class RecordMXList {
        @XStreamAlias("XM")
        private List<RecordMX> list;
    }

    @Data
    @XStreamAlias("XM")
    public class RecordMX {
        //单据名称
        @XStreamAlias("MC")
        private String mc;
        //单据数量
        @XStreamAlias("SL")
        private String sl;
    }
}

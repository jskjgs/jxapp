package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * 	获取挂号号源
 * Created by zbs on 2017/10/6.
 */
@XStreamAlias("ROOT")
@Data
public class RegisteredNumberInfo {

    @XStreamAlias("GROUP")
    Group group;

    @Data
    public class Group{
        //日期，格式(YYYY-MM-DD)
        @XStreamAlias("RQ")
        String rq;
        @XStreamImplicit(itemFieldName="HBLIST")
        List<Hblist> hblist;

    }

    @Data
    public class Hblist{
        //出诊记录ID，该节点可能没有，如没有时，处理为NUL
        @XStreamAlias("CZJLID")
        String czjlid;
        //科室ID
        @XStreamImplicit(itemFieldName="HB")
        List<Hb> hbList;
    }

    @Data
    public class Hb{
        //项目ID
        @XStreamAlias("XMID")
        String xmid;
        //号源名称(主任医师号、急诊号等)
        @XStreamAlias("HYMC")
        String hymc;
        //号码
        @XStreamAlias("HM")
        String hm;
        //医生ID
        @XStreamAlias("YSID")
        String ysid;
        //医生姓名
        @XStreamAlias("YS")
        String ys;
        //职称
        @XStreamAlias("ZC")
        String zc;
        //科室ID
        @XStreamAlias("KSID")
        String ksid;
        //科室名称
        @XStreamAlias("KSMC")
        String ksmc;
        //已挂号数
        @XStreamAlias("YGHS")
        String yghs;
        //剩余号数
        @XStreamAlias("SYHS")
        String syhs;
        //单价
        @XStreamAlias("DJ")
        String dj;
        //挂号类型
        @XStreamAlias("HL")
        String hl;
        //是否包含失效号或者缓冲限号，1-是，0-否
        @XStreamAlias("HCXH")
        String hcxh;
        //是否分时段控制，0-不分时段，1-分时段
        @XStreamAlias("FSD")
        String fsd;
        //可挂时间，格式：HH24:MI
        @XStreamAlias("KGSJ")
        String kgsj;
        //时间段（上午、下午等）
        @XStreamAlias("FWMC")
        String fwmc;
        //分时段列表。如果FSD节点为0，没有此节点
        @XStreamAlias("SPANLIST")
        Spanlist spanlist;
    }

    @Data
    public class Spanlist{
        @XStreamAlias("SPAN")
        Span span;
    }

    @Data
    public class Span{
        //时间段，格式:HH24:MM-HH24:MM
        @XStreamAlias("SJD")
        String sjd;
        //可挂号总数量
        @XStreamAlias("GHZS")
        String ghzs;
        //剩余可挂号数量
        @XStreamAlias("SL")
        String sl;
    }
}

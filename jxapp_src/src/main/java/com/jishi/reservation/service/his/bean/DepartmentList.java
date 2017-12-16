package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * Created by wang on 2017/9/28.
 */

//@XStreamAlias("KSLIST")
@XStreamAlias("ROOT")
@Data
public class DepartmentList {

    //@XStreamImplicit(itemFieldName="KSLIST")
    @XStreamAlias("KSLIST")
    KSLIST kslist;


    @Data
    public class KSLIST{
        @XStreamImplicit(itemFieldName="KS")
        List<DepartmentHis> list ;
    }


    @Data
    public class DepartmentHis{
        //门诊号ID
        @XStreamAlias("ID")
        String id;

        //门诊名称
        @XStreamAlias("MC")
        String mc;

        //当前科室所有号源中最大可预约天数
        @XStreamAlias("ZDYYTS")
        String zdyyts;
    }

}

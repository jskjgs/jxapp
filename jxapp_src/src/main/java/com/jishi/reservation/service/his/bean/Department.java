package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * Created by wang on 2017/9/28.
 */
@Data
public class Department {
    //科室ID
    @XStreamAlias("ID")
    String id;
    //科室名称
    @XStreamAlias("MC")
    String mc;
    //当前科室所有号源中最大可预约天数
    @XStreamAlias("ZDYYTS")
    String zdyyts;

}

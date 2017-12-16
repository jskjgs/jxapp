package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * Created by sloan on 2017/11/6.
 */

@XStreamAlias("ROOT")
@Data
public class LastPrice {

    //实收金额
    @XStreamAlias("JE")
    String je;

    //优惠金额
    @XStreamAlias("YHJE")
    String yhje;

    //费别 （普通）
    @XStreamAlias("FB")
    String fb;

}

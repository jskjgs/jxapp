package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * Created by sloan on 2017/10/25.
 */


@XStreamAlias("ROOT")
@Data
public class ConfirmOrder {

    @XStreamAlias("CZSJ")
    private String czsj;
    @XStreamAlias("GHDH")
    private String ghdh;
    @XStreamAlias("JZID")
    private String jzid;
}

package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * Created by wang on 2017/9/28.
 */
@XStreamAlias("ROOT")
@Data
public class PatientsList {

    @XStreamAlias("LIST")
    LIST list;

    @Data
    public class LIST{
        @XStreamImplicit(itemFieldName="JZK")
        List<Credentials> jzkList;
    }

    @Data
    public class Credentials {
        //类别
        @XStreamAlias("LB")
        String idType;
        //卡号
        @XStreamAlias("KH")
        String idNumber;
        //his唯一ID
        @XStreamAlias("BRID")
        String BRID;
        //病人门诊号
        @XStreamAlias("MZH")
        String MZH;
    }

}

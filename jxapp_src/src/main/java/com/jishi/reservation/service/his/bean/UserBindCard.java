package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * Created by zbs on 2017/10/6.
 */
@XStreamAlias("ROOT")
@Data
public class UserBindCard {

    @XStreamAlias("LIST")
    ListDate listDate;

    @Data
    public class ListDate{

        @XStreamImplicit(itemFieldName="JZK")
        List<Jzk> jzkList;
    }

    @Data
    public class Jzk{
        @XStreamAlias("LB")
        String lb;
        @XStreamAlias("KH")
        String kh;

    }
}

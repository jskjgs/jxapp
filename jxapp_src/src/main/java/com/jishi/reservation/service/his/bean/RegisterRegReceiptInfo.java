package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 *  获取挂号号源
 * Created by zbs on 2017/10/6.
 */
@XStreamAlias("ROOT")
@Data
public class RegisterRegReceiptInfo {

    @XStreamAlias("GHLIST")
    GhList ghList;



    @Data
    public class GhList{

        @XStreamImplicit(itemFieldName="GH")
        List<Gh> ghs;
    }

    @Data
    public class Gh{
        @XStreamAlias("DJH")
        String djh;
        @XStreamAlias("HL")
        String hl;
        @XStreamAlias("YYSJ")
        String yysj;
        @XStreamAlias("KDKS")
        String kdks;
        @XStreamAlias("ZXZT")
        String zxzt;
        @XStreamAlias("DJLX")
        String djlx;
        @XStreamAlias("ZFZT")
        String zfzt;
        @XStreamAlias("SFYY")
        String sfyy;
        @XStreamAlias("JE")
        String je;
        @XStreamAlias("SFJSK")
        String sfjsk;
    }
}

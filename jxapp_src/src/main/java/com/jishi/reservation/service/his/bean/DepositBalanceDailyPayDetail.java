package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * 日费用清单
 * Created by zbs on 2017/10/5.
 */
@Data
@XStreamAlias("ROOT")
public class DepositBalanceDailyPayDetail {

    @XStreamImplicit(itemFieldName="ITEM")
    List<Item> itemList;
    //费用合计
    @XStreamAlias("FYHJ")
    Fyhj fyhj;

    @Data
    public class Item{
        @XStreamAlias("FM")
        String fm;
        @XStreamAlias("FY")
        String fy;
        @XStreamImplicit(itemFieldName="MX")
        List<Mx> mxList;
    }

    @Data
    public class Mx{
        @XStreamAlias("YBLX")
        String yblx;
        @XStreamAlias("SFXM")
        String sfxm;
        @XStreamAlias("SL")
        String sl;
        @XStreamAlias("DJ")
        String dj;
        @XStreamAlias("DW")
        String dw;
        @XStreamAlias("JE")
        String je;
    }

    @Data
    public class Fyhj{
        @XStreamAlias("ZFY")
        String zfy;
        @XStreamImplicit(itemFieldName="ITEM")
        List<FyhjItem> fyhjItemList;
    }

    @Data
    public class FyhjItem{
        @XStreamAlias("YBJE")
        String ybje;
        @XStreamAlias("YBLX")
        String yblx;
    }
}

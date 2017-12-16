package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * Created by zbs on 2017/10/5.
 */
@Data
@XStreamAlias("ROOT")
public class DepositBalanceHistoryDetail {
    @XStreamAlias("GROUP")
    Group group;

    @Data
    public class Group {
        @XStreamImplicit(itemFieldName = "ITEM")
        List<Item> itemList;
    }

    @Data
    public class Item {
        //病人ID
        @XStreamAlias("BRID")
        String brid;
        //住院次数
        @XStreamAlias("ZYCS")
        String zycs;
        //住院号
        @XStreamAlias("ZYH")
        String zyh;
        //科室
        @XStreamAlias("KS")
        String ks;
        //科室ID
        @XStreamAlias("KSID")
        String ksid;
        //诊断结果(入院诊断)
        @XStreamAlias("ZD")
        String zd;
        //住院时间
        @XStreamAlias("RYSJ")
        String rysj;
        //出院时间，如果未出院时间则为空
        @XStreamAlias("CYSJ")
        String cysj;
        //住院状态，0-出院，1-在院，2-出院未结帐
        @XStreamAlias("ZYZT")
        String zyzt;
        //总费用
        @XStreamAlias("ZFY")
        String zfy;
    }


}

package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * 总住院费用清单
 * Created by zbs on 2017/10/5.
 */
@XStreamAlias("ROOT")
@Data
public class TotalDepositBalancePayDetail {

    @XStreamImplicit(itemFieldName="ITEM")
    List<Item> itemList;
    //费用合计
    @XStreamAlias("FYHJ")
    Fyhj fyhj;


    @Data
    public class Item{
        //类别名称，根据分类类型的不同返回，按时间就返回日期，按费目就返回费目名称
        @XStreamAlias("LBMC")
        String lbmc;
        //费用金额
        @XStreamAlias("FYJE")
        String fyje;

    }


    @Data
    public class Fyhj{
        //总费用
        @XStreamAlias("ZFY")
        String zfy;
        @XStreamImplicit(itemFieldName="ITEM")
        List<FyhjItem> itemList;
    }

    @Data
    public class FyhjItem{
        //医保类型
        @XStreamAlias("YBLX")
        String yblx;
        //医保金额
        @XStreamAlias("YBJE")
        String ybje;
    }

}

package com.jishi.reservation.service.his.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * 指定就诊的费用信息
 * Created by liangxiong on 2017/10/28.
 */
@XStreamAlias("ROOT")
@Data
public class OutpatientVisitReceipt {

    //receipt
    @XStreamImplicit(itemFieldName="GROUP")
    private List<Receipt> receiptList;

    @Data
    public class Receipt {
        //单据号
        @XStreamAlias("DJH")
        private String djh;
        //支付方式
        @XStreamAlias("ZFFS")
        private String zffs;
        //时间
        @XStreamAlias("SJ")
        private String sj;
        //医生
        @XStreamAlias("YS")
        private String YS;
        //费用
        @XStreamAlias("FY")
        private String fy;
        //receiptItem
        @XStreamImplicit(itemFieldName="ITEM")
        private List<ReceiptItem> itemList;
    }

    @Data
    public class ReceiptItem {
        //费目，如药品费、检验费等
        @XStreamAlias("FM")
        private String fm;
        //receiptMX
        @XStreamImplicit(itemFieldName="MX")
        private List<ReceiptMX> mxList;
    }

    @Data
    public class ReceiptMX {
        //名称
        @XStreamAlias("MC")
        private String mc;
        //规格
        @XStreamAlias("GG")
        private String gg;
        //数量
        @XStreamAlias("SL")
        private String sl;
        //单位
        @XStreamAlias("DW")
        private String dw;
        //单价
        @XStreamAlias("DJ")
        private String dj;
        //金额
        @XStreamAlias("JE")
        private String je;
    }
}

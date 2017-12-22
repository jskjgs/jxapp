package com.jishi.reservation.service.his.util;

/**
 * Created by liangxiong on 2017/12/22.
 */
public enum HisMedicalCardType {
    IC_CARD("IC卡"),
    ID_CARD("二代身份证"),
    MEDICAL_CARD("就诊卡"),
    HEALTH_CARD("居民健康卡"),
    WEICHAT_PAY_CARD("微信"),
    ALI_PAY_CARD("支付宝");

    private String cardType;

    HisMedicalCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }
}

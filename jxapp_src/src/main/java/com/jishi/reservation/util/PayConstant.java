package com.jishi.reservation.util;

import com.alipay.api.internal.util.AlipaySignature;

/**
 * Created by sloan on 2017/10/16.
 */
public class PayConstant {

    //阿里支付相关

    //APP_ID  锦欣通
    public final static String APP_ID = "2017092508916490";
    //应用私钥

    public final static String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAAS" +
            "CBKYwggSiAgEAAoIBAQC11sE3ILb+aLAwOnsPUHAX/wa/A4p5c+loIibzOx3DBefyde84SM" +
            "kPZpyEvPvc9YP5HYodQFnu9NMpKTiFQjZjnWHrzfLY+nCISiZvSYgZlH2SEu1uQlp3vwVdJ/jYe" +
            "/+rKl/zfDgDybdgrV4InAPG1Ud8l98jWm2HVgAYSXuP3ZxjsuJT8n5GKAYEA6rk5f+IJmpr6I0DTVrO" +
            "t8MuUD8VUGQjkg3zczMOuivDs3m7Lwl7iiXwj5hmbjkUolgQbRO+M8evkrT3uFj2UBFCpLxBzWZJpIDt" +
            "DHFgpbhBL5IsVanFv/xzmOFiWI8JUu0QxWBbUcVipcWQnpzbZcIzctLLAgMBAAECggEAJ8Bt9tZBC" +
            "vbp5XEza9Ki3qwS1nk4sXpkgS5OI4g95l+JKFkd1ckbozFEcEQ8Lz7A/hm2EtT5lCKTcwEe6Qrjhv" +
            "1l5b3dJBADv9et9VA235i1ptRacWsyCdGJo6bUYMciylF2Lm1udW2ImCruTqWEsdf4S8WLs1fWb+qeF" +
            "LEjwPxLgpHfsjW5qkSounJUHStM+M3Hbh88d95OQIZHwMJTATSeUonXEmhmloEzTX/3EPHXUZfYyadt" +
            "JfM2tSjldrAPD+vBdvt+N6OrWeTy7A+8BUsmBWlrwrq8/cfMWkf3vHIwgZGxZhvn+QCK1+JNLMzpsW9EE" +
            "6NL+0OUdSfesFQ+EQKBgQDxQ17b3tfQejSimCWEQkKrhInjVnypvs2fxPZaWjhSUclWpz5jEOcfuhudi" +
            "p5zsHiaceeDKVDdk+bUdRflBUTvZHT592uzPWaSDU4gVj4Kk40fKEn7/a9ejRwZKrJbXTsOLv5w3zeMoXgVm" +
            "NTfju9HX2nugOo1LZCzHsMylFcVuQKBgQDA8iraDZXUcjEx8SWYsrG6QUeTyjOcGJUif7sUQcANb3/XoKsM/" +
            "uVT+t7QSaQsvw9FQO9WgdcrFSK6lMqINQfpdPG4tfkildomA231aIDHyIhMsfjiK1/dscUoDCM6jdB0diSJWX" +
            "+d0Zw85NAV2ZGCj9s7qTnrAVcSMg9dPz7uowKBgCxoaSpxXyoJhGy1Mpqgk6L8d2D4B2Q7MaAJ4gqJdrp4QWY" +
            "IzKvxbYlieTjLThUfU2OiOanjzfWrBp7umqJWEPU7eWiVSSap05pohovfM/ZAaIZmpQ+UB1aRBK4BNOkqRPd8YOs" +
            "nuxLQmcjn467uoU5y2o3C/tkg9PRzYhyKP2lJAoGAXKuzgzmlLf2OvVL6YMXq8O9rpoMmmKGB96jrvMX7rThRacqE" +
            "fWBr2xMSM247STaW6gdPLPh91PZ40K6oPBVkcG+3raTRPRhGR528EXOY7tvVAykzxcVZbQx2Ck8SGIyGg3k/AS" +
            "0YSz+iz+iCPQxbhB7+CEC2TbY+EMI0eO/olQsCgYA+H76fT+9cY2XcUHCYZF7HREbK75Z0RiA5ecFY9NvXtEskW" +
            "5oBtPAiIjtHoDjFsCwnkLN6ESGjoJ9TvRiRfp/fvlf0feJL4OpnnNU5La7kR2QoWzYgyQVYOI9IrjguUHjaVY2O" +
            "YomplqOtQ9Zrxv/xoyAmGXe8N/l9lA5fyEbNvA==";

   //支付宝公钥
   public final static String ALI_PAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCA" +
           "QEAg18yOJ5ZqQ1rwqPNpjbdZVw6Dph0r6E49IUuMoBP24rwxzFJfAJTzKbTVFAw/fjN/dG68zwWJDRxZtVBinLd7IyqN" +
           "7+hZJJtd2loVXSRxMGpKjCuaxkKhIC8oH1qBLWd5WlRfkyte0w2GJRo2l1Ri6mK5qPlzFJegQ/vC8mR4uQ8WPkZ+MWVn" +
           "vbqIdeMARtUhtKDYDBOL6/0O/X7Wpc7oYupiwh3yRcDmdvKVfie4utG8y8s29i9Ufal6QmEYKiiX4pnUyOP92znjhF" +
           "EZl1+oroWCdZ2O9R33M5asqS8scrT/ubM1gtpI+JT3XevKqaw718GZLg1OAP0jSUQScmf8wIDAQAB";

    public final static String SERVER_URL = "http://openapi.alipay.com/gateway.do";
    public final static String SERVER_URL_REFUND = "https://openapi.alipay.com/gateway.do";
    public final static String DATA_FORMAT = "json";
    public final static String CHARSET = "utf-8";
    public final static String CHARSET_GBK = "GBK";
    public final static String ENCRYPT = "RSA2";
    //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
    public final static String QUICK_MSECURITY_PAY = "QUICK_MSECURITY_PAY";

    //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
    //注：若为空，则默认为15d。
    public final static String TIME_OUT_EXPRESS = "30m";


}

package com.jishi.reservation.dao.hisData;


import lombok.extern.log4j.Log4j;


/**
 * Created by zbs on 2017/9/18.
 */
@Log4j
public class SendData {

    static final String token = "359894CB16E4B68531A11083F2046B0E";

    public static String toXml(String data, String serviceName) {
        StringBuffer sb = new StringBuffer();
        sb.append("");
        sb.append("<ROOT>");
        sb.append("<SERVICE>" + serviceName + "</SERVICE>");
        sb.append("<TOKEN>" + token + "</TOKEN>");
        sb.append("<DATAPARAM>" + data + "<DATAPARAM>");
        sb.append("</ROOT>");
        return sb.toString();
    }
    //todo  切换成可以用的地址...
    static final String url = "http://hpx10ddns.xicp.io/EXTERNALSERVICES/ZL_INFORMATIONSERVICE.ASMX";
    //static final String url = "http://192.168.100.13:8086/EXTERNALSERVICES/ZL_INFORMATIONSERVICE.ASMX/";

//    public static void main(String[] args) throws Exception {
//        String xmlInput =toXml("","Basic.MCTest.Query");
//        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//        String wsUrl = "http://192.168.100.13:8086/ExternalServices/ZL_InformationService.asmx?wsdl";
//        String method = "Public";//webservice的方法名
//
//        Client client = dcf.createClient(wsUrl);
//        Object[] res = null;
//        try {
//            res = client.invoke(method, xmlInput);//调用webservice，cxf客户端调用webservice就是这么简单
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.exit(0);
//        // END SNIPPET: client
//
//        String a = "  <DJH>R0664518,R0664519,R0664756,R0664757,R0664758,R0664759,R0664760,R0664761</DJH>\n" +
//                "  <JE>488.50</JE>\n" +
//                "  <SFGH>0</SFGH>\n" +
//                "  <BRID>810553</BRID>\n" +
//                "  <JSLIST>\n" +
//                "   <JS>\n" +
//                "    <JSKLB>21</JSKLB>\n" +
//                "    <JSKH/>\n" +
//                "    <JSFS>weixin_wap</JSFS>\n" +
//                "    <JSJE>488.50</JSJE>\n" +
//                "    <JYLSH>4003632001201709141980077107</JYLSH>\n" +
//                "   </JS>\n" +
//                "  </JSLIST>";
//
//        String b = "Bla7xAv4MZzg9KU+T8ASuFCNnxa2rBtfCuCaRaU1CGqjjV4K/KtOCFUp0z2wgJrKzikhBtNaMvWEYOzWqBuYf8xLESRbm77Q/p6bbx7Scv0vUjX/a3TGbOB63DmCfPqUjE1N+wf74qIoaEF2o5bzQxRX2mCLtHSXI1n3dLeVXD4XxlLFBpfp8vp7hzBWhbiLSp/yQ2j23TF1SQ2yxwoluHp961kLQgMUqwA+1SSWAC8DYxlN599NTe/pEWdz0YYY9tbpIEFGNyArtNIXCmtjiT3i9cZhPqDndj72awGcGUizXfmEg7JGbzq1qgwlMHAyWP2LwXoOSStW1oxs+iOBbW0ZmzBgQC6CiExAv4H6RjPycedFAxNRgEuKSXScuTIwAOpNF3pjoRDmxzgR3WLMR+i3aElZk9CmOMHQn9y0lRA=";
//        String c = Codec.Encrypt(a);
//        System.out.println(Codec.Encrypt(a));
//        System.out.println(Codec.Decrypt(c));
//        System.out.println(Codec.Encrypt(token));
//    }
}



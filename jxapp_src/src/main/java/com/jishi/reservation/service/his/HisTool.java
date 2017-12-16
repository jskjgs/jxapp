package com.jishi.reservation.service.his;

import com.jishi.reservation.conf.HisConfiguration;
import com.jishi.reservation.util.Codec;
import com.thoughtworks.xstream.XStream;
import lombok.extern.log4j.Log4j;
import org.apache.axis.message.MessageElement;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wang on 2017/9/28.
 */
@Log4j
@Component
public class HisTool {

    @Autowired
    private HisConfiguration hisConfiguration;

    public Object toBean(Class type, String xml) {
        if(xml == null || "".equals(xml))
            return null;
        XStream xstream = new XStream();
        xstream.processAnnotations(type);//显示声明使用注解
        xstream.autodetectAnnotations(true);
        return xstream.fromXML(xml);
    }

    /**
     * 解析his系统返回的xml,提取其中的data param
     * @param messageElement
     * @return
     * @throws Exception
     */
    public String getHisDataparam(MessageElement messageElement, String url) throws Exception {
        String xml = messageElement.getAsString();
        log.info(url+"解密前的完整xml为 : "+xml);
        String state = getXmlAttribute(xml,"STATE");
        if(!"T".equals(state)){
            log.error("his系统返回失败信息——>"+xml);
            return null;
        }
        String req =  "<ROOT>"+Codec.Decrypt(getXmlAttribute(xml,"DATAPARAM"), hisConfiguration.getKey())+"</ROOT>";
        log.info("获取到解密后的数据部分为(<ROOT>节点为之后添加)  :  "+req);
        return req;
    }


    /**
     * 解析his系统返回的xml,提取其中的data param  不打印日志
     * @param messageElement
     * @return
     * @throws Exception
     */
    public String getHisDataparamNoLog(MessageElement messageElement, String url) throws Exception {
        String xml = messageElement.getAsString();
        String state = getXmlAttribute(xml,"STATE");
        if(!"T".equals(state)){
            log.error("his系统返回失败信息——>"+xml);
            return null;
        }
        String req =  "<ROOT>"+Codec.Decrypt(getXmlAttribute(xml,"DATAPARAM"), hisConfiguration.getKey())+"</ROOT>";
        return req;

    }

    /**
     * 获取xml某个节点的属性
     *
     * @param xml
     * @param attributeName
     * @return
     * @throws Exception
     */
    public String getXmlAttribute(String xml, String attributeName) throws Exception {
        //如果不是以<ROOT>开头的就先加上<ROOT>节点
        xml = !xml.startsWith("<ROOT") ? "<ROOT>" + xml + "</ROOT>" : xml;
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        return root.elementTextTrim(attributeName);
    }


    public String toXMLString(String serviceName, String data) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("<ROOT>");
        sb.append("<TOKEN>");
        sb.append("<![CDATA[");
        sb.append(Codec.Encrypt(hisConfiguration.getToken(), hisConfiguration.getKey()));
        sb.append("]]></TOKEN>");
        sb.append("<SERVICE>");
        sb.append("<![CDATA[");
        sb.append(serviceName);
        sb.append("]]></SERVICE>");
        sb.append("<INSIDEKEY>");
        sb.append("<![CDATA[");
        sb.append("]]></INSIDEKEY>");
        sb.append("<DATAPARAM>");
        sb.append("<![CDATA[");
        sb.append(Codec.Encrypt(data, hisConfiguration.getKey()));
        sb.append("]]></DATAPARAM>");
        sb.append("</ROOT>");
      //  log.info(sb.toString());
        return sb.toString();
    }

}

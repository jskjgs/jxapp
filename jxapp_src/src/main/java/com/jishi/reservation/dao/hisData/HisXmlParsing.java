package com.jishi.reservation.dao.hisData;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by zbs on 2017/9/18.
 */
public class HisXmlParsing {

    public HisXmlBean parsing(String xmlStr) throws DocumentException {
        Document document = DocumentHelper.parseText(xmlStr);
        //获取根节点
        Element root = document.getRootElement();
        String state = root.element("STATE").asXML();
        HisXmlBean hisXmlBean = new HisXmlBean();
        if("F".equals(state)){
            String errorcode = root.element("ERROR_ERRCODE").asXML();
            String msg = root.element("ERROR_MSG").asXML();
            HisErrorBean hisErrorBean = new HisErrorBean();
            hisErrorBean.setCode(errorcode);
            hisErrorBean.setMsg(msg);
            hisXmlBean.setState(state);
            hisXmlBean.setError(hisErrorBean);
            return hisXmlBean;
        }
        Element data = root.element("DATAPARAM");
        hisXmlBean.setState(state);
        hisXmlBean.setData(data);
        return hisXmlBean;
    }
}

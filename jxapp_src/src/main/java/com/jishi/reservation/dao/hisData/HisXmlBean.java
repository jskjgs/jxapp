package com.jishi.reservation.dao.hisData;

import lombok.Data;
import org.dom4j.Element;

/**
 * Created by zbs on 2017/9/18.
 */
@Data
public class HisXmlBean {
    String state;
    Element data;
    HisErrorBean error;
}

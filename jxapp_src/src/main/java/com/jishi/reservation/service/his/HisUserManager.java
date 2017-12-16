package com.jishi.reservation.service.his;

import com.jishi.reservation.mypackage.UserManagerResponseUserManagerResult;
import com.jishi.reservation.mypackage.ZL_InformationServiceLocator;
import com.jishi.reservation.mypackage.ZL_InformationServiceSoap_PortType;
import com.jishi.reservation.service.his.bean.Credentials;
import com.jishi.reservation.service.his.bean.PatientsList;
import com.jishi.reservation.conf.HisConfiguration;
import lombok.extern.log4j.Log4j;
import org.apache.axis.message.MessageElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * Created by wang on 2017/9/28.
 */
@Service
@Log4j
public class HisUserManager {

    @Autowired
    private HisConfiguration hisConfiguration;

    @Autowired
    private HisTool hisTool;


    /**
     * 获取用户信息
     *
     * @param idNumber 证件号
     * @param idNumberType 证件信息
     */
    public PatientsList getUserInfoByCode(String idNumber, String idNumberType) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("<ZJH>").append(idNumber).append("</ZJH>");
        sb.append("<ZJLX>").append(idNumberType).append("</ZJLX>");
        String reData = hisTool.toXMLString("BindCard.UserInfoByCardNO.Query", sb.toString());
        UserManagerResponseUserManagerResult result = execute(reData);
        for (MessageElement me : result.get_any()) {
            String xml = hisTool.getHisDataparam(me,"BindCard.UserInfoByCardNO.Query");
            return (PatientsList)hisTool.toBean(PatientsList.class,xml);
        }
        return null;
    }

    /**
     * 获取用户信息2,通过登记号  返回得有brid
     * @param idNumber
     * @param idNumberType
     * @param name
     * @param code
     * @param codeType
     * @return
     * @throws Exception
     */
    public Credentials getUserInfoByRegNO(String idNumber, String idNumberType, String name, String code, String codeType) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("<ZJH>").append(idNumber).append("</ZJH>");
        sb.append("<ZJLX>").append(idNumberType).append("</ZJLX>");
        sb.append("<XM>").append(name).append("</XM>");
        sb.append("<KH>").append(code).append("</KH>");
        sb.append("<KLB>").append(codeType).append("</KLB>");
        String reData = hisTool.toXMLString("BindCard.UserInfoByRegNO.Query", sb.toString());
        UserManagerResponseUserManagerResult result = execute(reData);
        for (MessageElement me : result.get_any()) {
            log.info(me.getAsString());
            String xml = hisTool.getHisDataparam(me,"BindCard.UserInfoByRegNO.Query");
            return (Credentials)hisTool.toBean(Credentials.class,xml);
        }
        return null;
    }


    /**
     * 增加病人信息
     * @param idNumber
     * @param idNumberType
     * @param name
     * @param phone
     * @return
     * @throws Exception
     */
    //todo 增加病人信息需要返回BRID和MZH ？？？？
    public Credentials addUserInfo(String idNumber,String idNumberType,String name,String phone) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("<ZJH>").append(idNumber).append("</ZJH>");
        sb.append("<ZJLX>").append(idNumberType).append("</ZJLX>");
        sb.append("<XM>").append(name).append("</XM>");
        sb.append("<SJH>").append(phone).append("</SJH>");
        String reData = hisTool.toXMLString("BindCard.CreateUser.Modify", sb.toString());
        UserManagerResponseUserManagerResult result = execute(reData);
        for (MessageElement me : result.get_any()) {
            log.info(me.getAsString());

            String xml = hisTool.getHisDataparam(me,"BindCard.CreateUser.Modify");
            return (Credentials)hisTool.toBean(Credentials.class,xml);

        }
       return null;
    }


    private UserManagerResponseUserManagerResult execute(String reData) throws RemoteException, ServiceException {
        ZL_InformationServiceLocator locator = new ZL_InformationServiceLocator();
        locator.setZL_InformationServiceSoapEndpointAddress(hisConfiguration.getHisBaseUrl());
        locator.setZL_InformationServiceSoap12EndpointAddress(hisConfiguration.getHisBaseUrl());
        ZL_InformationServiceSoap_PortType service = locator.getZL_InformationServiceSoap();
        return service.userManager(reData);
    }

}

package com.jishi.reservation.service;

import com.jishi.reservation.controller.protocol.OutpatientPaymentInfoVO;
import com.jishi.reservation.dao.mapper.PatientInfoMapper;
import com.jishi.reservation.dao.models.Account;
import com.jishi.reservation.dao.models.PatientInfo;
import com.jishi.reservation.service.his.HisHospitalization;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.HisTool;
import com.jishi.reservation.service.support.JpushSupport;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.worker.model.PushData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by liangxiong on 2017/12/21.
 */
@Slf4j
@Service
public class HisMessageService {

    @Autowired
    private HisTool hisTool;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private JpushSupport jpushSupport;


    private static final String SERVICE_NAME_REGISTER_SUCCESS = "Trans.RegisterT.Push";     //挂号成功后进行消息推送
    private static final String SERVICE_NAME_REGISTER_CANCELED = "Trans.CancelRegT.Push";   //退号成功后进行消息推送
    private static final String SERVICE_NAME_PAID_SUCCESS = "Trans.PaymentT.Push";          //缴费成功后进行消息推送
    private static final String SERVICE_NAME_REFUND_SUCCESS = "Trans.PayRefundT.Push";      //退费成功后进行消息推送
    private static final String SERVICE_NAME_PREPAID_SUCCESS = "Trans.PrepaidPayT.Push";    //预交款缴款成功后进行消息推送
    private static final String SERVICE_NAME_PREPAID_REFUND_SUCCESS = "Trans.RefPrepaidPayT.Push";//预交款退款成功后进行消息推送
    private static final String SERVICE_NAME_SETTLEMENT_SUCCESS = "Trans.SettlementT.Push";//结算成功后消息推送
    private static final String SERVICE_NAME_REMIND_MEDICINE = "Remind.MedicineT.Push";     //发药完成后进行消息推送
    private static final String SERVICE_NAME_REFUND_MEDICINE = "Remind.RefMedicineT.Push";  //退药完成后进行消息推送
    private static final String SERVICE_NAME_REMIND_TAKE_MEDICINE = "Remind.TakeMedicineT.Push";    //根据口服药执行时间推送消息提醒病人服药
    private static final String SERVICE_NAME_OUTEX_REPORT = "Remind.OutExReportT.Push";     //当检查报告完成后进行消息推送
    private static final String SERVICE_NAME_REMIND_NEED_TO_PAY = "Remind.NeedToPay.Push";  //当有待缴费费用时推送消息
    private static final String SERVICE_NAME_REMIND_NEED_TO_PREPAY = "Remind.NeedToPrePay.Push";    //每天定时对预交款欠费的病人推送消息
    private static final String SERVICE_NAME_REMIND_DAILYPAY_DETAIL = "Remind.DailyPayDetail.Push"; //每天定时发送一日清单消息
    private static final String SERVICE_NAME_DOCTORPLANCHANGED = "Remind.DoctorPlanChanged.Push";    //HIS进行停诊、换诊操作后向相关病人推送消息
    private static final String SERVICE_NAME_APPOINTMENT_NOTICE = "Remind.AppointmentNotice.Push";  //医生对复诊病人进行预约登记，在复诊时间前对病人进行提醒。
    private static final String SERVICE_NAME_REFUND_HISREG_CANCEL = "Refund.HISRegCancel.Push";     //HIS中对第三方支付的挂号单据进行退号，通知第三方进行退款，退款金额原支付通道退还到病人支付帐户
    private static final String SERVICE_NAME_REFUND_HISPAY_CANCEL = "Refund.HISPayCancel.Push";         //HIS中对第三方支付的收费单据进行退费，通知第三方进行退款，退款金额原支付通道退还到病人支付帐户
    //=========================================未完待续=========================================


    public void executeMessage(String serviceName, String serviceData) throws Exception {
        if (serviceName == null || serviceData == null) {
            return;
        }
        switch (serviceName) {
            case SERVICE_NAME_REMIND_NEED_TO_PAY: needToPay(serviceData); break;
            case SERVICE_NAME_REMIND_NEED_TO_PREPAY: needToPrepay(serviceData); break;
            case SERVICE_NAME_APPOINTMENT_NOTICE: appointmentNotice(serviceData); break;
            default: {
                log.info("未处理的HIS消息通知 serviceName：" + serviceName);
            }
        }
    }

    // 门诊待缴费
    private void needToPay(String serviceData) throws Exception {

        String BRID = hisTool.getXmlAttribute(serviceData,"BRID");
        String BRXM = hisTool.getXmlAttribute(serviceData,"XM");
        String FY = hisTool.getXmlAttribute(serviceData,"FY");
        String LX = hisTool.getXmlAttribute(serviceData,"LX");
        String YSID = hisTool.getXmlAttribute(serviceData,"YSID");
        String YSXM = hisTool.getXmlAttribute(serviceData,"YSXM");
        String KSID = hisTool.getXmlAttribute(serviceData,"KSID");
        String KS = hisTool.getXmlAttribute(serviceData,"KS");
        String DJH = hisTool.getXmlAttribute(serviceData,"DJH");
        String MZH = hisTool.getXmlAttribute(serviceData,"MZH");
        String GHD = hisTool.getXmlAttribute(serviceData,"GHD");
        String GHSJ = hisTool.getXmlAttribute(serviceData,"GHSJ");

        OutpatientPaymentInfoVO info = new OutpatientPaymentInfoVO();
        info.setBrid(BRID);
        info.setPatientName(BRXM);
        info.setUnpaidAmount(getBigDecimal(FY));
        info.setDocumentType(Integer.valueOf(LX));
        info.setDoctorId(YSID);
        info.setDoctorName(YSXM);
        info.setDepartment(KS);
        info.setUnpaidDocIds(DJH);
        info.setDocumentNum(GHD);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        info.setLastDocDate(dateFormat.parse(GHSJ));

        jpushSupport.sendNotification(getPushId(BRID), Constant.MSG_OUT_UNPAID_DOC, PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_OUT_UNPAID_DOC);
    }

    // 预交款欠费
    private void needToPrepay(String serviceData) throws Exception {
        String BRID = hisTool.getXmlAttribute(serviceData,"BRID");
        String NR = hisTool.getXmlAttribute(serviceData,"NR");
        jpushSupport.sendNotification(getPushId(BRID), NR, PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_HOS_PRE_PAY);
    }

    private void doctorPlanChanged(String serviceData) throws Exception {
    }

    // 复诊提醒
    private void appointmentNotice(String serviceData) throws Exception {
//        String BRXM = hisTool.getXmlAttribute(serviceData,"XM");
//        String date = hisTool.getXmlAttribute(serviceData,"XM");
//        String BRID = hisTool.getXmlAttribute(serviceData,"BRID");
//        String notification = "复诊患者：" + BRXM + "，记得准时就诊哦";
//        jpushSupport.sendNotification(getPushId(BRID), notification, PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_APPOINTMENT_NOTICE);
    }

    private String getPushId(String brid) {
        if (brid == null || brid.isEmpty()) {
            return null;
        }
        List<PatientInfo> patientInfoList = patientInfoMapper.queryByBrId(brid);
        if (patientInfoList == null || patientInfoList.isEmpty()) {
            return null;
        }
        Account account = accountService.queryAccountById(patientInfoList.get(0).getAccountId());
        return account != null ? account.getPushId() : null;
    }

    private BigDecimal getBigDecimal(String amount) {
        return new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}

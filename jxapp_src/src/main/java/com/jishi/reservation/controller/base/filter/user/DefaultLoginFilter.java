package com.jishi.reservation.controller.base.filter.user;

import javax.servlet.annotation.WebFilter;

/**
 * Created by liangxiong on 2017/11/21.
 */
@WebFilter(filterName = "DefaultLoginFilter", urlPatterns = {
      "/reservation/department/*",          // 科室
      "/reservation/diray/*",               // 日记
      "/reservation/doctor_i/*",            // 医生
      "/reservation/his_account/*",         // 对接了his系统的账号
      "/reservation/his_doctor/*",          // 对接了his系统的醫生
      "/reservation/hospitalization/*",     // 住院
      "/reservation/im/*",                  // im
      "/reservation/order/*",               // 订单
      "/reservation/outpatient/*",          // 门诊
      "/reservation/patientInfo/*",         // 病人
      "/reservation/pregnant/*",            // 孕妇信息
      "/reservation/register/*"})           // 预约挂号
public class DefaultLoginFilter extends VerifyLoginFilter {
}

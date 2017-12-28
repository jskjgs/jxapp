package com.jishi.reservation.service;

import com.alibaba.fastjson.JSON;
import com.doraemon.base.redis.RedisOperation;
import com.jishi.reservation.controller.protocol.OutpatientQueueDetailVO;
import com.jishi.reservation.dao.mapper.QueueLengthMapper;
import com.jishi.reservation.dao.models.*;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.jinxin.JinxinQueue;
import com.jishi.reservation.service.jinxin.bean.QueueDetail;
import com.jishi.reservation.service.support.JpushSupport;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.worker.model.PushData;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by liangxiong on 2017/11/14.
 */
@Service
@Log4j
public class OutpatientQueueService {

    @Autowired
    private PatientInfoService patientInfoService;
    @Autowired
    private JinxinQueue jinxinQueue;
    @Autowired
    private QueueLengthMapper queueLengthMapper;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private RedisOperation redisOperation;
    @Autowired
    private JpushSupport jpushSupport;
    @Autowired
    private AccountService accountService;

    // 默认门诊队列通知长度
    public static final int DEFAULT_NOTIFY_LENGTH = 10;
    public static final int MIN_NOTIFY_LENGTH = 5;


    /**
     * @description 获取门诊排队信息，账号id，brid参数提供一个
     * @param accountId 账号id
     * @param brId 病人id
     * @throws Exception
    **/
    public List<OutpatientQueueDetailVO> queryVisitQueueInfo(Long accountId, String brId) throws Exception {
        if (brId != null && !brId.isEmpty()) {
            OutpatientQueueDetailVO vo = queryVisitQueueInfo(brId);
            if (vo == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(vo);
        }
        List<PatientInfo> patientInfoList = patientInfoService.queryPatientInfo(null, accountId, 0);
        if (patientInfoList == null || patientInfoList.isEmpty()) {
            log.info(accountId + ": 病人列表为空");
            return Collections.emptyList();
        }
        List<OutpatientQueueDetailVO> queueDetailVOList = new ArrayList<OutpatientQueueDetailVO>();
        for (PatientInfo info : patientInfoList) {
            OutpatientQueueDetailVO queueDetailVO = queryVisitQueueInfo(info.getBrId());
            if (queueDetailVO == null) {
                continue;
            }
            queueDetailVOList.add(queueDetailVO);
        }
        return queueDetailVOList;
    }

    /**
     * @description 获取门诊排队信息
     * @param brId 病人id
     * @throws Exception
     **/
    public OutpatientQueueDetailVO queryVisitQueueInfo(String brId) throws Exception {
        String value = redisOperation.usePool().get(brId);
        log.info("病人id为 " + brId + " 的分诊排队信息redis缓存为：" + value);
        if (value == null || value.isEmpty()) {
            return null;
        }
        QueueDetail queueDetail = JSON.parseObject(value, QueueDetail.class);
        return getDetailVO(queueDetail);
    }

    /**
     * @description 获取有过改变的所有排队信息
     **/
    public List<QueueDetail> queryQueueByDepartmentId(String departmentId) throws Exception {
        List<QueueDetail> allData = jinxinQueue.queryQueueByDepartmentId(departmentId);
        if (allData == null || allData.isEmpty()) {
            return Collections.emptyList();
        }
        return allData;
    }

    public void doNoticeRegisterQueue() throws Exception {
        List<Department> departmentList = departmentService.queryAllDepartment();
        if (departmentList == null || departmentList.isEmpty()) {
            return;
        }
        for (Department department : departmentList) {
            if (EnableEnum.EFFECTIVE.equals(department.getEnable())) {
                List<QueueDetail> allData = queryQueueByDepartmentId(department.getHId());
                queueNotification(allData);
            }
        }
    }

    public void queueNotification(List<QueueDetail> queueDetailList) throws Exception {
        if (queueDetailList == null || queueDetailList.isEmpty()) {
            return;
        }
        for (QueueDetail queueDetail : queueDetailList) {
            String detailJson = JSON.toJSONString(queueDetail);
            String value = redisOperation.usePool().get(queueDetail.getPatientId());
            if (detailJson.equals(value)) {
                continue;
            }
            redisOperation.usePool().set(queueDetail.getPatientId(), detailJson);
            redisOperation.usePool().expire(queueDetail.getPatientId(), Constant.QUEUE_EXPIRE_TIME_CACHE);
            List<PatientInfo> patientInfo = patientInfoService.queryByBrId(queueDetail.getPatientId());
            if (patientInfo == null || patientInfo.isEmpty()) {
                log.info("当前病人未添加, brid: " + queueDetail.getPatientId());
                continue;
            }
            Account account = accountService.queryAccountById(patientInfo.get(0).getAccountId());
            if (account == null) {
                log.warn("当前病人账号为空, brid: " + queueDetail.getPatientId());
                continue;
            }
            jpushSupport.sendMessage(account.getPushId(), PushData.PushDataMsgTypeDef.PUSH_DATA_TYPE_OUT_QUEUE_INFO
                    , getDetailVO(queueDetail));
        }
    }

    /**
     * @description 获取医生下的通知队列长度
     * @param doctorHisId his医生id
     * @throws
    **/
    public int getQueueNotifyLength(String doctorHisId) {
        if (doctorHisId != null && !doctorHisId.isEmpty()) {
            QueueLength queueLength = queueLengthMapper.queryByDoctorHisId(doctorHisId);
            if (queueLength == null) {
                Doctor doctor = doctorService.queryDoctorByHid(doctorHisId);
                queueLength = queueLengthMapper.queryByDepartHisId(doctor.getDepartmentId());
            }
            if (queueLength != null && queueLength.getLength() >= MIN_NOTIFY_LENGTH) {
                return queueLength.getLength();
            }
        }
        return DEFAULT_NOTIFY_LENGTH;
    }

    /**
     * @description 获取所有的通知队列长度信息
    **/
    public List<QueueLength> queryQueueLengthAll() {
        return queueLengthMapper.queryAll();
    }


    /**
     * @description 获取医生的通知队列长度信息
     * @param doctorHisId his医生id
     **/
    public QueueLength queryDoctorQueueLength(String doctorHisId) {
        return queueLengthMapper.queryByDoctorHisId(doctorHisId);
    }


    /**
     * @description 获取部门的通知队列长度信息
     * @param departHisId his部门id
     **/
    public QueueLength queryDepartQueueLength(String departHisId) {
        return queueLengthMapper.queryByDepartHisId(departHisId);
    }

    /**
     * @description 添加通知队列长度信息
     **/
    public boolean addQueueLength(String targetId, int length, boolean isDepartment) {
        QueueLength queueLength = new QueueLength();
        queueLength.setLength(length);
        if (isDepartment) {
            List<Department> departmentList = departmentService.queryByHisId(targetId);
            if (departmentList == null || departmentList.size() != 0) {
                log.info("addQueueLengthDoctor 未找到科室：" + targetId);
            }
            Department department = departmentList.get(0);
            queueLength.setDepartHisId(department.getHId());
            queueLength.setDepartName(department.getName());
        } else {
            Doctor doctor = doctorService.queryDoctorByHid(targetId);
            queueLength.setDoctorHisId(doctor.getHId());
            queueLength.setDoctorName(doctor.getName());
            queueLength.setDepartHisId(doctor.getDepartmentId());
            queueLength.setDepartName(doctor.getKsmc());
        }
        return queueLengthMapper.insertReturnId(queueLength) > 0;
    }

    /**
     * @description 修改通知队列长度信息
     **/
    public boolean updateQueueLength(String targetId, int length, boolean isDepartment) {
        if (targetId == null || targetId.isEmpty()) {
            return false;
        }
        if (length < MIN_NOTIFY_LENGTH) {
            log.warn("排队叫号通知队列最小长度为：" + MIN_NOTIFY_LENGTH + "传入参数: " + length);
        }
        return isDepartment ? queueLengthMapper.updateByDepartHisId(targetId, length) :
                              queueLengthMapper.updateByDoctorHisId(targetId, length);
    }

    public OutpatientQueueDetailVO getDetailVO(QueueDetail detail) {
        if (detail == null) {
          return null;
        }
        OutpatientQueueDetailVO vo = new OutpatientQueueDetailVO();
        vo.setBrId(detail.getPatientId());
        vo.setDepartId(Long.valueOf(detail.getDepartmentId()));
        //vo.setDepartName(detail.getKSMC());
        vo.setDoctorHisId(detail.getDoctorId());
        //vo.setDoctorName(detail.getYS());
        //vo.setDoctorTitle(detail.getZC());
        //vo.setName(detail.getBR());
        //vo.setQueueeMinder(detail.getBRPDTX());
        //vo.setQueueInfo(detail.getBRPD());
        //vo.setRegisterDate(detail.getRQ());
        vo.setRegisterType(detail.getRegisterType());
        vo.setCurrentNum(detail.getCurrentNum());
        vo.setQueueNum(detail.getQueueNum());
        vo.setNeedWaitNum(detail.getWaitingNum());
        vo.setStatus(detail.getState());
        return vo;
    }


    public List<OutpatientQueueDetailVO> generateTestData() throws Exception {
        return generateTestData(Integer.MAX_VALUE);
    }

    // 生成测试数据
    public List<OutpatientQueueDetailVO> generateTestData(int length) throws Exception {
        //测试的账号
        List<Long> accIds = Arrays.asList(24L, 21L, 30L, 26L);
        List<Register> registerList = new ArrayList<>();
        for (Long accId : accIds) {
            List<Register> data = registerService.queryRegister(null, accId, 0, 0);
            if (data != null && !data.isEmpty()) {
                registerList.addAll(data);
            }
        }
        length = length < 0 ? 1 : length;
        if (registerList.size() > length) {
            registerList = registerList.subList(0, length);
        }
        List<OutpatientQueueDetailVO> detailList = new ArrayList<OutpatientQueueDetailVO>();
        for (int i = 0; i < registerList.size(); i++) {
            Register register = registerList.get(i);
            OutpatientQueueDetailVO vo = null;
            int status = i % 4;
            if (status == 0) vo = newQueueRegistered();
            else if (status == 1) vo = newQueueWait();
            else if(status == 2) vo = newQueueSeeing();
            else vo = newQueuePassed();

            vo.setBrId(register.getBrId());
            vo.setDepartId(Long.valueOf(register.getDepartmentId()));
            vo.setDepartName(register.getDepartment());
            //vo.setDoctorId(register.getDoctorId());
            vo.setDoctorHisId(register.getDoctorId());
            vo.setDoctorName(register.getDoctorName());
            vo.setDoctorTitle("主任医师");
            vo.setName(register.getPatientName());
            vo.setQueueeMinder("您预约的门诊正在排队，请注意！");
            vo.setQueueInfo("您预约的门诊正在排队，请注意！");
            vo.setRegisterDate(register.getCreateTime());
            vo.setRegisterType("普通");
            vo.setCurrentNum(0);
            vo.setQueueNum(Integer.parseInt(register.getHm()));
            vo.setAccountId(register.getAccountId());

            detailList.add(vo);
        }
        return detailList;
    }

    private OutpatientQueueDetailVO newQueueRegistered() {
        OutpatientQueueDetailVO vo = new OutpatientQueueDetailVO();
        vo.setCurrentNum(0);
        vo.setQueueNum(12);
        vo.setNeedWaitNum(6);
        vo.setStatus(0);
        return vo;
    }

    private OutpatientQueueDetailVO newQueueWait() {
        OutpatientQueueDetailVO vo = new OutpatientQueueDetailVO();
        vo.setCurrentNum(5);
        vo.setQueueNum(12);
        vo.setNeedWaitNum(6);
        vo.setStatus(1);
        return vo;
    }

    private OutpatientQueueDetailVO newQueueSeeing() {
        OutpatientQueueDetailVO vo = new OutpatientQueueDetailVO();
        vo.setCurrentNum(12);
        vo.setQueueNum(12);
        vo.setNeedWaitNum(0);
        vo.setStatus(2);
        return vo;
    }

    private OutpatientQueueDetailVO newQueuePassed() {
        OutpatientQueueDetailVO vo = new OutpatientQueueDetailVO();
        vo.setCurrentNum(15);
        vo.setQueueNum(12);
        vo.setNeedWaitNum(1);
        vo.setStatus(3);
        return vo;
    }
}

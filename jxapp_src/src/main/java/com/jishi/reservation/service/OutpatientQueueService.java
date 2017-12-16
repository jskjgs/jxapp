package com.jishi.reservation.service;

import com.jishi.reservation.controller.protocol.OutpatientQueueDetailVO;
import com.jishi.reservation.dao.mapper.QueueLengthMapper;
import com.jishi.reservation.dao.models.*;
import com.jishi.reservation.service.jinxin.JinxinQueue;
import com.jishi.reservation.service.jinxin.bean.QueueCurrentNumber;
import com.jishi.reservation.service.jinxin.bean.QueueDetail;
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
    public RegisterService registerService;

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
            return Arrays.asList(queryVisitQueueInfo(brId));
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
    public OutpatientQueueDetailVO queryVisitQueueInfo(String brId) {
        QueueDetail detail = jinxinQueue.queryQueueByBrId(brId);
        return getDetailVO(detail);
    }

    /**
     * @description 获取有过改变的门诊信息
     * @throws
    **/
    public List<QueueCurrentNumber> queryModifiedVisitCurrentNum() {
        return jinxinQueue.queryCurrentNumber();
    }

    /**
     * @description 获取医生id下的排队信息
     * @param doctorHisId
     * @throws
    **/
    public List<OutpatientQueueDetailVO> queryQueueByDoctorHisId(String doctorHisId) {
        List<QueueDetail> queueDetailList = jinxinQueue.queryQueueByDoctorHisId(doctorHisId, getQueueNotifyLength(doctorHisId));
        if (queueDetailList == null || queueDetailList.isEmpty()) {
            return Collections.emptyList();
        }
        List<OutpatientQueueDetailVO> detailVOList = new ArrayList<OutpatientQueueDetailVO>();
        for (QueueDetail detail : queueDetailList) {
            OutpatientQueueDetailVO detailVO = getDetailVO(detail);
            if (detailVO != null) {
                detailVOList.add(detailVO);
            }
        }
        return detailVOList;
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
        vo.setBrId(detail.getBRID());
        vo.setDepartId(detail.getKSID());
        vo.setDepartName(detail.getKSMC());
        vo.setDoctorHisId(detail.getYSID());
        vo.setDoctorName(detail.getYS());
        vo.setDoctorTitle(detail.getZC());
        vo.setName(detail.getBR());
        vo.setQueueeMinder(detail.getBRPDTX());
        vo.setQueueInfo(detail.getBRPD());
        vo.setRegisterDate(detail.getRQ());
        vo.setRegisterType(detail.getHL());
        // TODO 解析当前号码，病人的号码
        vo.setCurrentNum(0);
        vo.setQueueNum(12);
        vo.setNeedWaitNum(6);
        vo.setStatus(1);
        return vo;
    }


    public List<OutpatientQueueDetailVO> generateTestData() throws Exception {
        return generateTestData(Integer.MAX_VALUE);
    }

    // 生成测试数据
    public List<OutpatientQueueDetailVO> generateTestData(int length) throws Exception {
        //测试的账号
        List<Long> accIds = Arrays.asList(24L, 21L, 30L);
        List<Register> registerList = new ArrayList<>();
        for (Long accId : accIds) {
            List<Register> data = registerService.queryRegister(null, accId, 2, 0);
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

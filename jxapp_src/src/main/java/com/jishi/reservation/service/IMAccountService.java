package com.jishi.reservation.service;

import com.jishi.reservation.controller.protocol.IMChatInfo;
import com.jishi.reservation.dao.mapper.IMAccessRecordMapper;
import com.jishi.reservation.dao.mapper.IMAccountMapper;
import com.jishi.reservation.dao.models.Account;
import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.dao.models.IMAccessRecord;
import com.jishi.reservation.dao.models.IMAccount;
import com.jishi.reservation.otherService.im.IMException;
import com.jishi.reservation.otherService.im.neteasy.IMClientNeteasy;
import com.jishi.reservation.otherService.im.neteasy.model.IMUser;
import com.jishi.reservation.service.enumPackage.ReturnCodeEnum;
import com.jishi.reservation.service.exception.BussinessException;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.Helpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liangxiong on 2017/10/27.
 */
@Service
@Slf4j
public class IMAccountService {
    @Autowired
    private AccountService accountService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private IMAccountMapper imAccountMapper;

    @Autowired
    private IMAccessRecordMapper imAccessRecordMapper;

    private IMClientNeteasy imClientNeteasy = IMClientNeteasy.getInstance(
            Constant.IM_NETEASY_APPKEY, Constant.IM_NETEASY_APPSECRET);

    /**
     * @description 获取普通用户im的账号，没有im账户则创建，没有token则更新
     * @param accId 用户id
     * @throws Exception
    **/
    public IMAccount getUserIMAccount(Long accId) throws Exception {
        IMAccount imAccount = imAccountMapper.selectByAccountId(accId);
        if (imAccount == null) {
            log.info("用户没有创建IM账户，现在创建。本地系统accId：" + accId);
            return createUserByAccountId(accId);
        } else if (imAccount.getImToken() == null || imAccount.getImToken().isEmpty()) {
            log.info("用户im token为空，刷新token。本地系统accId：" + accId);
            imAccount.setImToken(refreshToken(imAccount));
        }
        log.info("用户accId：" + accId + " im token：" + imAccount.getImToken());
        return imAccount;
    }

    /**
     * @description 获取医生im的账号，没有im账户则创建，没有token则更新
     * @param doctorId 本地系统医生id
     * @throws Exception
    **/
    public IMAccount getDoctorIMAccount(Long doctorId) throws Exception {
        IMAccount imAccount = imAccountMapper.selectByDoctorId(doctorId);
        if (imAccount == null) {
            log.info("医生没有创建IM账户，现在创建。本地系统doctorId：" + doctorId);
            return createUserByDoctorId(doctorId);
        } else if (imAccount.getImToken() == null || imAccount.getImToken().isEmpty()) {
            log.info("医生im token为空，刷新token。本地系统doctorId：" + doctorId);
          imAccount.setImToken(refreshToken(imAccount));
        }
        log.info("医生doctorId：" + doctorId + " im token" + imAccount.getImToken());
        return imAccount;
    }

    /**
     * @description 刷新im的token
     * @param accId 用户id
     * @throws Exception
    **/
    public String refreshUserToken(Long accId) throws Exception {
        if (accId == null) {
            log.error("refreshUserToken：用户accId不能为空");
            return null;
        }
        IMAccount imAccount = imAccountMapper.selectByAccountId(accId);
        String token = refreshToken(imAccount);
        log.info("用户accId：" + accId + " im token" + token);
        return token;
    }

    /**
     * @description 刷新im的token
     * @param doctorId 医生doctorId
     * @throws Exception
     **/
    public String refreshDoctorToken(Long doctorId) throws Exception {
        if (doctorId == null) {
          log.error("refreshUserToken：医生doctorId不能为空");
          return null;
        }
        IMAccount imAccount = imAccountMapper.selectByDoctorId(doctorId);
        String token = refreshToken(imAccount);
        log.info("医生doctorId：" + doctorId + " im token" + token);
        return token;
    }

    private String refreshToken(IMAccount imAccount) throws Exception {
        String token = imClientNeteasy.getUserOperation().refreshToken(imAccount.getImAccId());
        imAccount.setImToken(token);
        imAccountMapper.updateByPrimaryKey(imAccount);
        return token;
    }

    /**
     * @description 创建IM账户
     * @param accId 普通用户id
     * @throws
     **/
    public IMAccount createUserByAccountId(Long accId) throws Exception {
        log.info("创建普通用户IM账户，accId" + accId);
        return createUser(accId,null, null);
    }

    /**
     * @description 创建IM账户
     * @param doctorId 医生本地系统id
     * @throws Exception
     **/
    public IMAccount createUserByDoctorId(Long doctorId) throws Exception {
        log.info("创建医生IM账户，doctorId" + doctorId);
        return createUser(null,doctorId, null);
    }

    /**
     * @description 创建IM账户
     * @param doctorHisId 医生his系统id
     * @throws Exception
     **/
    public IMAccount createUserByDoctorHisId(String doctorHisId) throws Exception {
        log.info("创建医生IM账户，doctorHisId" + doctorHisId);
        return createUser(null, null, doctorHisId);
    }

    /**
     * @description 创建IM账户 参数传一个
     * @param accId 普通用户id
     * @param doctorId 医生本地系统id
     * @param doctorHisId 医生his系统id
     * @throws Exception
    **/
    private IMAccount createUser(Long accId, Long doctorId, String doctorHisId) throws Exception {
        if (accId == null && doctorId == null && Helpers.isNullOrEmpty(doctorHisId)) {
            log.error("accId doctorId doctorHisId不能全为空");
            return null;
        }

        IMAccount imAccount = new IMAccount();
        IMUser imUser;
        if (accId != null) {
            Account account = accountService.queryAccountById(accId);
            if (account == null) {
                log.error("未找到用户：" + accId);
                return null;
            }
            imUser = generateIMUser(account);
            imAccount.setAccountId(accId);
            imAccount.setType(0); //普通用户
        } else {
            List<Doctor> doctorList = doctorService.queryDoctor(doctorId, doctorHisId, null, null, null, 0);
            if (doctorList == null || doctorList.size() != 1) {
                log.error("医生列表为空或大于1：doctorId " + doctorId + " doctorHisId " +  doctorHisId);
                return null;
            }
            Doctor doctor = doctorList.iterator().next();
            imUser = generateIMUser(doctor);
            imAccount.setDoctorId(doctor.getId());
            imAccount.setDoctorHisId(doctor.getHId());
            imAccount.setType(1); //医生
        }

        IMUser resUser = null;
        try {
            resUser = imClientNeteasy.getUserOperation().createUser(imUser);
        } catch (IMException e) {
            log.info(e.toString());
            if (e.getCode() == 414) { // 网易IM参数错误，账号已存在
                IMAccount record = new IMAccount();
                record.setAccountId(accId);
                record.setDoctorId(doctorId);
                record.setDoctorHisId(doctorHisId);
                imAccount = imAccountMapper.selectOne(record);
                if (imAccount == null) {
                    throw new BussinessException(ReturnCodeEnum.IM_ERR_GET_ACCOUNT_FAILED);
                }
            }
            else {
                throw new BussinessException(ReturnCodeEnum.IM_ERR_CREATE_ACCOUNT_FAILED);
            }
        }
        if (resUser != null) {
            log.info("网易云信创建账号成功:  imaccid " + resUser.getAccid() + " token " + resUser.getToken());
            imAccount.setImAccId(resUser.getAccid());
            imAccount.setImToken(resUser.getToken());
            imAccountMapper.insertReturnId(imAccount);
        }

        return imAccount;
    }

    /**
     * @description 咨询医生，返回im信息
     * @param accountId 用户id
     * @param doctorId 医生id
     * @throws
    **/
    public IMChatInfo chatToDocter(Long accountId, Long doctorId) throws Exception {
        IMAccount imUserAccount = getUserIMAccount(accountId);
        IMAccount imDoctorAccount = getDoctorIMAccount(doctorId);
        IMChatInfo info = new IMChatInfo();
        info.setImSourceId(imUserAccount.getImAccId());
        info.setImDestId(imDoctorAccount.getImAccId());
        info.setImToken(imUserAccount.getImToken());

        updateVisitRecord(accountId, doctorId);
        return info;
    }

    /**
     * @description 更新用户访问记录
     * @param accountId 用户id
     * @param doctorId 医生id
     * @throws Exception
    **/
    public void updateVisitRecord(Long accountId, Long doctorId) {
        List<IMAccessRecord> recordList = imAccessRecordMapper.selectAppointRecord(accountId, doctorId);
        if (recordList == null || recordList.isEmpty()) {
            IMAccessRecord record = new IMAccessRecord();
            record.setUserId(accountId);
            record.setDoctorId(doctorId);
            Date date = new Date();
            record.setFirstAccessDate(date);
            record.setLastAccessDate(date);
            imAccessRecordMapper.insertReturnId(record);
        } else {
            IMAccessRecord record = recordList.get(0);
            record.setLastAccessDate(new Date());
            imAccessRecordMapper.updateByPrimaryKey(record);
        }
    }

    /**
     * @description 获取用户咨询列表
     * @param accountId 用户账号ID
     * @throws Exception
    **/
    public List<Doctor> queryUserIMAccessRecord(Long accountId) throws Exception {
        List<IMAccessRecord> accessRecordList = imAccessRecordMapper.selectByUserId(accountId, Constant.IM_HISTORY_VISIT_DOCTOR_SIZE);
        if (accessRecordList == null || accessRecordList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Doctor> doctorList = new ArrayList<Doctor>();
        List<Doctor> doctorAllList = doctorService.queryDoctor(null, null, null, null, null, 0);
        for (IMAccessRecord accessRecord : accessRecordList) {
            for (Doctor item : doctorAllList) {
                if (item.getId().equals(accessRecord.getDoctorId())) {
                    doctorList.add(item);
                    break;
                }
            }
        }
        return doctorList;
    }

    /**
     * @description 获取IM账号信息
     * @param id 用户id或医生id
     * @throws Exception
    **/
    public IMUser queryUser(Long id, boolean isDoctor) throws Exception {
        IMAccount imAccount = isDoctor ? imAccountMapper.selectByDoctorId(id) : imAccountMapper.selectByAccountId(id);
        if (imAccount == null) {
            return null;
        }
        List<IMUser> imUserList = imClientNeteasy.getUserOperation().getUinfos(Collections.singletonList(imAccount.getImAccId()));
        if (imUserList != null && imUserList.size() == 1) {
            return imUserList.get(0);
        }
        return null;
    }

    /**
     * @description 更新IM账号信息
     * @param accountId 本系统账号id
     * @throws Exception
    **/
    public boolean updateUser(Long accountId) throws Exception {
        log.info("更新IM账号信息: accountId: " + accountId);
        IMAccount imAccount = imAccountMapper.selectByAccountId(accountId);
        Account account = accountService.queryAccountById(accountId);
        if (imAccount == null || account == null) {
            log.info("账号不存在: imAccount: " + imAccount + "  account: " + account);
            return false;
        }
        IMUser imUser = generateIMUser(imAccount.getImAccId(), imAccount.getImToken(), account);
        return imClientNeteasy.getUserOperation().updateUinfo(imUser);
    }

    /**
     * @description 更新IM账号信息
     * @param doctorId 本系统医生id
     * @throws Exception
     **/
    public boolean updateDoctor(Long doctorId) throws Exception {
        log.info("更新IM账号信息: doctorId: " + doctorId);
        IMAccount imAccount = imAccountMapper.selectByDoctorId(doctorId);
        Doctor doctor = doctorService.queryDoctorById(imAccount.getDoctorId());
        if (imAccount == null || doctor == null) {
            log.info("账号不存在: imAccount: " + imAccount + "  doctor: " + doctor);
            return false;
        }
        IMUser imUser = generateIMUser(imAccount.getImAccId(), imAccount.getImToken(), doctor);
        return imClientNeteasy.getUserOperation().updateUinfo(imUser);
    }

    private IMUser generateIMUser(Account account) {
        String identify = Constant.IM_ACCOUNT_PREFIX_USER + account.getId();
        String imToken = UUID.randomUUID().toString().replace("-", "");
        return generateIMUser(identify, imToken, account);
    }

    private IMUser generateIMUser(String imAccId, String imToken, Account account) {
        IMUser imUser = new IMUser();
        imUser.setAccid(imAccId);
        imUser.setToken(imToken);
        imUser.setName(account.getNick());
        imUser.setMobile(account.getPhone());
        imUser.setEmail(account.getEmail());
        imUser.setIcon(account.getHeadPortrait());
        imUser.setProps(account.getAccount());
        return imUser;
    }

    private IMUser generateIMUser(Doctor doctor) {
        String identify = Constant.IM_ACCOUNT_PREFIX_DOCTOR + doctor.getId();
        String imToken = UUID.randomUUID().toString().replace("-", "");
        return generateIMUser(identify, imToken, doctor);
    }

    private IMUser generateIMUser(String imAccId, String imToken, Doctor doctor) {
        IMUser imUser = new IMUser();
        imUser.setAccid(imAccId);
        imUser.setToken(imToken);
        imUser.setName(doctor.getName());
        imUser.setIcon(doctor.getHeadPortrait());
        imUser.setSign(doctor.getAbout());
        imUser.setProps(doctor.getId().toString());
        return imUser;
    }
}

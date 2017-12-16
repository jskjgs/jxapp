package com.jishi.reservation.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.controller.protocol.HospitalizationInfoVO;
import com.jishi.reservation.dao.mapper.PatientInfoMapper;
import com.jishi.reservation.dao.mapper.PregnantMapper;
import com.jishi.reservation.dao.models.PatientInfo;
import com.jishi.reservation.dao.models.Pregnant;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.his.HisUserManager;
import com.jishi.reservation.service.his.bean.Credentials;
import com.jishi.reservation.util.CheckIdCard;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class PatientInfoService {

    @Autowired
    PatientInfoMapper patientInfoMapper;

    @Autowired
    PregnantMapper pregnantMapper;

    @Autowired
    HisUserManager hisUserManager;

    /**
     * 增加就诊人信息
     * @param accountId
     * @param name
     * @param phone
     * @param idCard
     * @throws Exception
     */

    @Transactional
    public Long addPatientInfo(Long accountId, String name, String phone, String idCard,String idCardType) throws Exception {
        if (Helpers.isNullOrEmpty(accountId))
            throw new Exception("账号ID为空");
        String errorInfo = CheckIdCard.IDCardValidate(idCard);
        if (errorInfo != null && !"".equals(errorInfo)) {
            log.error(errorInfo);
            throw new Exception("无效的身份证信息");
        }
        //判断一个账号最大病号数是否超过5个
        if(!this.checkMaxPatientNum(accountId)){
            throw new Exception("该账号最大病号数已达最大5个");
        }

        // 判断身份证不能重复 11-29 单独根据身份证来判断
        Preconditions.checkState(isExistPatientByIdcard(idCard),"此身份证已存在,添加失败");

        //添加到his系统
        Credentials credentials = hisUserManager.addUserInfo(idCard, idCardType, name, phone);
        log.info("his系统返回的病人信息：\n"+ JSONObject.toJSONString(credentials));


        PatientInfo newPatientInfo = new PatientInfo();
        newPatientInfo.setAccountId(accountId);
        newPatientInfo.setName(name);
        newPatientInfo.setPhone(phone);
        newPatientInfo.setIdCard(idCard);
        newPatientInfo.setEnable(EnableEnum.EFFECTIVE.getCode());
        newPatientInfo.setBrId(credentials.getBRID());
        newPatientInfo.setMzh(credentials.getMZH());
        patientInfoMapper.insertReturnId(newPatientInfo);


        Pregnant newPregnant = new Pregnant();
        newPregnant.setName(name);
        newPregnant.setAccountId(accountId);
        newPregnant.setCreateTime(new Date());
        newPregnant.setEnable(EnableEnum.EFFECTIVE.getCode());
        newPregnant.setPatientId(newPatientInfo.getId());
        pregnantMapper.insert(newPregnant);

        return newPatientInfo.getId();

    }


    //单独根据身份证判断是不是有这个病人  11/29
    private boolean isExistPatientByIdcard(String idCard) {
        PatientInfo patientInfo =  patientInfoMapper.queryForExistByIdcard(idCard);
        return patientInfo == null;
    }

    private boolean isExistPatient(Long accountId, String name, String idCard) {

        PatientInfo patientInfo =  patientInfoMapper.queryForExist(accountId,name,idCard);
        return patientInfo == null;
    }

    /**
     * 判断账号下病号数是否达到最大值5
     * @param accountId
     * @return
     */
    private boolean checkMaxPatientNum(Long accountId) {
        return patientInfoMapper.findMaxPatientNum(accountId) < 5;
    }


    /**
     * 查询就诊人信息
     * @param id
     * @param accountId
     * @param enable
     * @return
     * @throws Exception
     */
    public List<PatientInfo> queryPatientInfo(Long id, Long accountId, Integer enable) throws Exception {
        PatientInfo queryPatientInfo = new PatientInfo();
        queryPatientInfo.setId(id);
        queryPatientInfo.setAccountId(accountId);
        queryPatientInfo.setEnable(enable);
        return patientInfoMapper.select(queryPatientInfo);
    }

    /**
     * 查询就诊人信息,分页
     * @param patientInfoId
     * @param accountId
     * @param enable
     * @param paging
     * @return
     */
    public PageInfo<PatientInfo> queryPatientInfoPagaInfo(Long patientInfoId, Long accountId, Integer enable, Paging paging) throws Exception {
        if(paging != null)
            PageHelper.startPage(paging.getPageNum(),paging.getPageSize(),paging.getOrderBy());
        return new PageInfo(queryPatientInfo(patientInfoId,accountId,enable));
    }


    /**
     * 修改就诊人信息
     * @param name
     * @param phone
     * @param idCard
     * @throws Exception
     */
    public void modifyPatientInfo(Long accountId,Long id, String name, String phone, String idCard,Integer enable) throws Exception {
        if (Helpers.isNullOrEmpty(id))
            throw new Exception("就诊人ID为空");
        if(queryPatientInfo(id,null,null) == null)
            throw new Exception("没有查询到就诊人");
        if(idCard != null && !"".equals(idCard)){
            String errorInfo = CheckIdCard.IDCardValidate(idCard);
            if (errorInfo != null && !"".equals(errorInfo)) {
                log.error(errorInfo);
                throw new Exception("无效的身份证信息");
            }
        }
        PatientInfo oldPatient = patientInfoMapper.queryById(id);
        PatientInfo modifyPatientInfo = new PatientInfo();

        modifyPatientInfo.setId(id);
        modifyPatientInfo.setName(name!=null?name:oldPatient.getName());
        modifyPatientInfo.setPhone(phone !=null?phone:oldPatient.getPhone());
        modifyPatientInfo.setIdCard(idCard!=null?idCard:oldPatient.getIdCard());
        modifyPatientInfo.setEnable(enable!=null?enable:oldPatient.getEnable());
        Preconditions.checkState(patientInfoMapper.updateByPrimaryKeySelective(modifyPatientInfo) == 1,"更新失败!");
    }

    /**
     * 删除就诊人
     * @param patientInfoId
     * @throws Exception
     */
    public void deletePatientInfo(Long patientInfoId) throws Exception {
        if (Helpers.isNullOrEmpty(patientInfoId))
            throw new Exception("就诊人ID为空");
        if(queryPatientInfo(patientInfoId,null,null) == null)
            throw new Exception("没有查询到就诊人");
        patientInfoMapper.deleteByPrimaryKey(patientInfoId);
    }

    public void wrapPregnant(List<PatientInfo> patientInfos) {

        if(patientInfos!=null && patientInfos.size() !=0){
            for (PatientInfo patientInfo : patientInfos) {
                Pregnant pregnant = pregnantMapper.queryByPatientId(patientInfo.getId());
                if(pregnant != null){
                    patientInfo.setRemark(pregnant.getRemark());
                    patientInfo.setBirth(pregnant.getBirth());
                    patientInfo.setPregnantEnable(pregnant.getEnable());
                    patientInfo.setHusbandName(pregnant.getHusbandName());
                    patientInfo.setHusbandTelephone(pregnant.getHusbandTelephone());
                    patientInfo.setLastMenses(pregnant.getLastMenses());
                    patientInfo.setLivingAddress(pregnant.getLivingAddress());
                    patientInfo.setPregnantId(pregnant.getId());
                }

            }
        }

    }

    public List<String> queryBrIdByAccountId(Long accountId) {

        List<String> brIdList = patientInfoMapper.queryBrIdByAccountId(accountId);
        brIdList.removeAll(Collections.singleton(null));
        return brIdList;
    }

    public PageInfo<HospitalizationInfoVO> wrapListToPage(List<HospitalizationInfoVO> list, Integer startPage, Integer pageSize) {


        if(pageSize == 0){
            pageSize = list.size();
        }
        PageInfo<HospitalizationInfoVO>  page = new PageInfo<>();
        List<HospitalizationInfoVO> result = new ArrayList<>();
        int startRow = (startPage - 1)*pageSize;
        int endRow = list.size()<startPage*pageSize-1?list.size():startPage*pageSize-1;
        if(startPage == endRow)
            endRow+=pageSize;
        if(endRow == 0)
            endRow+=pageSize;

        log.info("endRow :"+endRow);
        if(list.size()<endRow){
            endRow = list.size();
        }
        for(int i = startRow;i<endRow;i++){
            result.add(list.get(i));
        }
        page.setTotal(list.size());
        page.setList(result);
        Integer pages = (list.size()-1)/pageSize+1;
        page.setPages(pages);
        page.setPageNum(startPage);
        page.setHasNextPage(pages>startPage);

        return page;
    }

    public PatientInfo queryByBrIdAndAccountId(String brId,Long accountId) {

        return patientInfoMapper.queryByById(brId,accountId);
    }

    public boolean isAccountIdMatchBrid(Long accountId, String brId) {

        //传入brid是否和account_id对应
        return patientInfoMapper.queryAccountIdByBrId(brId).equals(accountId);

    }
}

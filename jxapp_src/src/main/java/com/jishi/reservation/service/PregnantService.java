package com.jishi.reservation.service;


import com.google.common.base.Preconditions;
import com.jishi.reservation.dao.mapper.PregnantMapper;

import com.jishi.reservation.dao.models.Pregnant;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by csrr on 2017/8/27.
 */
@Service
@Log4j
public class PregnantService {

    @Autowired
    private PregnantMapper pregnantMapper;


    @Autowired
    private AccountService accountService;

    @Autowired
    private PatientInfoService patientInfoService;


    /**
     * 增加孕妇信息
     */
    public void addPregnant(Long accountId, Long patientinfoId, String name, Date birth, String livingAddress, Date lastMenses, String telephone, String husbandName, String husbandTelephone,String remark) throws Exception {

        if(Helpers.isNullOrEmpty(accountId) || accountService.queryAccount(accountId,null, EnableEnum.EFFECTIVE.getCode()) == null)
            throw new Exception("账户信息为空.");
        if(Helpers.isNullOrEmpty(patientinfoId)  || patientInfoService.queryPatientInfo(patientinfoId,null, EnableEnum.EFFECTIVE.getCode()) == null)
            throw new Exception("就诊人信息为空.");

        Pregnant pregnant = new Pregnant();
        pregnant.setAccountId(accountId);
        pregnant.setPatientId(patientinfoId);
        pregnant.setName(name);
        pregnant.setBirth(birth);
        pregnant.setLivingAddress(livingAddress);
        pregnant.setLastMenses(lastMenses);
        pregnant.setTelephone(telephone);
        pregnant.setHusbandName(husbandName);
        pregnant.setHusbandTelephone(husbandTelephone);
        pregnant.setRemark(remark);
        pregnant.setEnable(EnableEnum.EFFECTIVE.getCode());
        pregnant.setCreateTime(new Date());
        pregnantMapper.insert(pregnant);


    }

    public void updatePregnant(Long patientId, String name, Long birth, String livingAddress, Long lastMenses, String telephone, String husbandName, String husbandTelephone,Integer enable,String remark) throws Exception {

        log.info("修改孕妇信息  patientId:"+patientId+",name :"+name+",birth:"+birth+",livingAddress:"+livingAddress+
                ",lastMenses:"+lastMenses+ ",telephone:"+telephone+",husbandName:"+husbandName+",husbandTelephone:"+husbandTelephone);

        List<Pregnant> pregnantList = queryPregnant(null, patientId, null, EnableEnum.EFFECTIVE.getCode());
        if(Helpers.isNullOrEmpty(patientId) || pregnantList.size() == 0)
            throw new Exception("孕妇信息为空.");
        Pregnant pregnant = new Pregnant();
        pregnant.setId(pregnantList.get(0).getId());
        pregnant.setPatientId(patientId);
        pregnant.setName(name!=null?name:pregnantList.get(0).getName());
        pregnant.setBirth(birth!=null?new Date(birth):pregnantList.get(0).getBirth());
        pregnant.setLivingAddress(livingAddress!=null?livingAddress:pregnantList.get(0).getLivingAddress());
        pregnant.setLastMenses(lastMenses!=null?new Date(lastMenses):pregnantList.get(0).getLastMenses());
        pregnant.setTelephone(telephone!=null?telephone:pregnantList.get(0).getTelephone());
        pregnant.setHusbandName(husbandName!=null?husbandName:pregnantList.get(0).getHusbandName());
        pregnant.setHusbandTelephone(husbandTelephone!=null?husbandTelephone:pregnantList.get(0).getHusbandTelephone());
        pregnant.setRemark(remark!=null?remark:pregnantList.get(0).getRemark());

        Preconditions.checkState(pregnantMapper.updateByPrimaryKeySelective(pregnant) == 1,"更新失败");


    }


    public List<Pregnant> queryPregnant(Long id, Long patientinfoId, String name, Integer enable) {

        log.info("查询孕妇 id:"+id+" patientinfoId:"+patientinfoId+" name:"+name +" enable:"+enable);
        Pregnant queryPregnant = new Pregnant();

        queryPregnant.setId(id);
        queryPregnant.setPatientId(patientinfoId);
        queryPregnant.setName(name);
        queryPregnant.setEnable(enable);

        return pregnantMapper.select(queryPregnant);
    }

    public void deleteById(Long pregnantId) {
        pregnantMapper.deleteById(pregnantId);
    }
}

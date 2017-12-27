package com.jishi.reservation.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.jishi.reservation.controller.base.Paging;
import com.jishi.reservation.dao.mapper.DepartmentMapper;
import com.jishi.reservation.dao.mapper.DoctorMapper;
import com.jishi.reservation.dao.mapper.DoctorWorkMapper;
import com.jishi.reservation.dao.models.Department;
import com.jishi.reservation.dao.models.Doctor;
import com.jishi.reservation.dao.models.DoctorWork;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.his.bean.RegisteredNumberInfo;
import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class DoctorService {

    @Autowired
    DoctorMapper doctorMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    private IMAccountService imAccountService;

    @Autowired
    private DoctorWorkMapper doctorWorkMapper;

    @Autowired
    private DoctorWorkService doctorWorkService;

    /**
     * 增加科室
     * @param doctorName
     * @param type
     * @param headPortrait
     * @param departmentIds
     * @param about
     * @param title
     * @param school
     * @param goodDescribe
     */
    public void addDoctor(String doctorName, String type,String headPortrait, String departmentIds,String about, String title,String school,String goodDescribe){
        log.info("增加科室 doctorName:"+doctorName+" type:"+type+" headPortrait:"+headPortrait+" about:"+about+" title:"+title+" school:"+school+" goodDescribe:"+goodDescribe);
        Doctor newDoctor = new Doctor();
        List<String> list = new ArrayList<>();
        if(departmentIds!=null && departmentIds.length() != 0){
            String[] split = departmentIds.split(",");
            for (String s : split) {
                list.add(s);
            }
            log.info("转化后的部门id:"+JSONObject.toJSONString(list));
        }
        newDoctor.setName(doctorName);
        newDoctor.setType(type);
        newDoctor.setHeadPortrait(headPortrait);
        newDoctor.setDepartmentIds(JSONObject.toJSONString(list));
        newDoctor.setAbout(about);
        newDoctor.setTitle(title);
        newDoctor.setSchool(school);
        newDoctor.setGoodDescribe(goodDescribe);
        newDoctor.setEnable(EnableEnum.EFFECTIVE.getCode());
        doctorMapper.insert(newDoctor);
    }

    /**
     * 查询科室
     * @param doctorId
     * @param doctorName
     * @param type
     */
    public List<Doctor> queryDoctor(Long doctorId, String hDoctorId,String doctorName, String departmentId,String type,Long agreeTime,Integer enable) throws Exception {

        log.info("查询医生 doctorId:"+doctorId+" doctorName:"+doctorName+" type:"+type +" enable:"+enable);
        Doctor queryDoctor = new Doctor();
        queryDoctor.setType(type);
        queryDoctor.setName(doctorName);
        queryDoctor.setId(doctorId);
        queryDoctor.setEnable(enable);
        queryDoctor.setHId(hDoctorId);
        List<Doctor> list = doctorMapper.select(queryDoctor);
        log.info("查询长度："+list.size());
        if(agreeTime == null){


            return list;
        }else {

            log.info("查询的日期不为空");
            List<Doctor> resultList = new ArrayList<>();
            Date date = new Date(agreeTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //结合排班表去查找
            for (Doctor doctor : list) {
                DoctorWork doctorWork = doctorWorkMapper.queryByHIdAndDate(doctor.getHId(), sdf.format(date));
                if(doctorWork != null)
                    resultList.add(doctor);
            }

            return resultList;

        }

    }

    /**
     * 查询全部科室
     */
    public PageInfo<Doctor> queryDoctorPageInfo(Long doctorId, String doctorName,String departmentId, String type,Integer enable,Paging paging) throws Exception {
        log.info("查询全部科室.");
        if(!Helpers.isNullOrEmpty(paging))
            PageHelper.startPage(paging.getPageNum(),paging.getPageSize(),paging.getOrderBy());
        return new PageInfo(queryDoctor(doctorId,null,doctorName,departmentId,type,null,enable));
    }

    /**
     * 修改科室信息
     * @param doctorId
     * @param doctorName
     * @param type
     * @param headPortrait
     * @param about
     * @param title
     * @param school
     * @param goodDescribe
     * @param enable
     */
    public void modifyDoctor(Long doctorId,String doctorName, String type,String headPortrait, String about, String title,String school,String goodDescribe,Integer enable) throws Exception {
        log.info("修改科室信息 doctorId:"+doctorId+" doctorName:"+doctorName+" type:"+type+" headPortrait:"+headPortrait+" about:"+about+" title:"+title+" school:"+school+" goodDescribe:"+goodDescribe);
        if(Helpers.isNullOrEmpty(doctorId))
            throw new Exception("医生ID不能为空.");

        Doctor oldDoctor = doctorMapper.queryById(doctorId);
        Doctor modifyDoctor = new Doctor();
        modifyDoctor.setId(doctorId);
        modifyDoctor.setName(doctorName!=null?doctorName:oldDoctor.getName());
        modifyDoctor.setType(type!=null?type:oldDoctor.getType());
        modifyDoctor.setHeadPortrait(headPortrait!=null?headPortrait:oldDoctor.getHeadPortrait());
        modifyDoctor.setAbout(about!=null?about:oldDoctor.getAbout());
        modifyDoctor.setTitle(title!=null?title:oldDoctor.getTitle());
        modifyDoctor.setSchool(school!=null?school:oldDoctor.getSchool());
        modifyDoctor.setGoodDescribe(goodDescribe!=null?goodDescribe:oldDoctor.getGoodDescribe());
        modifyDoctor.setEnable(enable!=null?enable:oldDoctor.getEnable());
        Preconditions.checkState(doctorMapper.updateByPrimaryKeySelective(modifyDoctor) == 1,"更新失败!");
        imAccountService.updateDoctor(doctorId);
    }

    public List<Doctor> queryDoctorByDepartment(Long departmentId, Integer enable) {

        Doctor param = new Doctor();
        param.setEnable(enable);
        List<Doctor> allDoctorList = doctorMapper.select(param);
        List<Doctor> list = new ArrayList<>();
        for (Doctor doctor : allDoctorList) {
            String[] split =  doctor.getDepartmentIds().split(",");
            for (String existDepartment : split) {
                if(Long.valueOf(existDepartment).equals(departmentId)){
                    list.add(doctor);
                }
            }
        }

        return  list;
    }

    public void topDoctor(Long doctorId) throws Exception {
        if(Helpers.isNullOrEmpty(doctorId))
            throw new Exception("医生ID不能为空.");
        List<Doctor> doctorList = this.queryDoctor(doctorId, null,null, null, null, null,EnableEnum.EFFECTIVE.getCode());
        if(doctorList.size()==0)
            throw new Exception("没有查询到医生");


        Integer maxOrderNumber = doctorMapper.queryMaxOrderNumber();
        doctorList.get(0).setOrderNumber(doctorList.get(0).getOrderNumber().equals(0)?maxOrderNumber+1:0);

        doctorMapper.updateByPrimaryKeySelective(doctorList.get(0));

    }

    public void getDoctorFromHis(List<RegisteredNumberInfo.HB> hbList,Long date) {

        List<Doctor> list = new ArrayList<>();
        List<DoctorWork> workList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        log.info("传入的查询排班时间："+sdf.format(date));

        for (RegisteredNumberInfo.HB hb : hbList) {
            Date workDate = new Date(date);
            Doctor doctor = new Doctor();
            DoctorWork doctorWork = new DoctorWork();
            doctorWork.setEnable(EnableEnum.EFFECTIVE.getCode());
            doctorWork.setHDoctorId(hb.getYsid());
            doctorWork.setWorkingTime(workDate);
            doctorWork.setCzjlid(hb.getCzjlid());
            log.info("查询条件：医生 id:"+hb.getYsid()+"，时间："+sdf2.format(date));
            if(!doctorWorkService.isExit(hb.getYsid(),sdf2.format(date))){
                log.info("该医生排班表不存在,医生id:"+hb.getYsid()+",预约时间："+workDate.toLocaleString());
                workList.add(doctorWork);
            }

            doctor.setName(hb.getYs());
            doctor.setHId(hb.getYsid());
            doctor.setDepartmentId(hb.getKsid());
            doctor.setKsmc(hb.getKsmc());
            doctor.setHeadPortrait(Constant.DEFAULT_AVATAR);
            doctor.setOrderNumber(0);
            doctor.setEnable(0);
            doctor.setType("0");
            doctor.setDj(hb.getDj());
            doctor.setHm(hb.getHm());
           // doctor.setCzjlid(hb.getCzjlid());
            //如果不存在，就添加进去...
            if(!isExist(hb.getYsid())){
                log.info(hb.getYs()+"不存在"+hb.getYsid());
                list.add(doctor);
            }else { //有了的话就修改...
                log.info(hb.getYs()+"已经存在"+hb.getYsid()+",执行更新操作...");
                Doctor existDoctor = doctorMapper.queryByHid(hb.getYsid());
                existDoctor.setDj(hb.getDj());
                existDoctor.setHm(hb.getHm());
                existDoctor.setKsmc(hb.getKsmc());
                existDoctor.setName(hb.getYs());
                existDoctor.setDepartmentId(hb.getKsid());

                doctorMapper.updateByPrimaryKeySelective(existDoctor);

            }

        }

        if(list.size() != 0){

            log.info("将要入库的数据："+JSONObject.toJSONString(list));
            doctorMapper.insertList(list);
        }

        if(workList.size() != 0){
            log.info("将要入库的排班数据："+JSONObject.toJSONString(workList));
            doctorWorkMapper.insertList(workList);
        }

        //如果库里有，但是his拉去过来的没有，那就软删除

        List<Doctor> doctorList =  doctorMapper.queryAllValidDoctor();
        for (Doctor doctor : doctorList) {
            Boolean flag = false;
            for (RegisteredNumberInfo.HB hb : hbList) {
                if(doctor.getHId().equals(hb.getYsid())){
                    //有的话不管，没有的话，改变flag
                    log.info(doctor.getHId()+"his 医生id存在于本地库中");
                    flag = true;
                    break;
                }else {


                }

            }
            if(!flag){
                log.info("his 医生id是"+doctor.getHId()+"的医生在his里面拉去不到了，做软删除操作.");
                doctor.setEnable(1);  //软删除...
                doctorMapper.updateByPrimaryKeySelective(doctor);
            }
        }
    }

    private boolean isExist(String hId) {
        return doctorMapper.queryByHid(hId) != null;
    }



    public Doctor queryDoctorById(Long id) {
        return doctorMapper.queryById(id);
    }

    public Doctor queryDoctorByHid(String hDoctorId) {
        return doctorMapper.queryByHdoctorId(hDoctorId);
    }



    public  List<Department> queryDepartmentAndDoctor() {
        List<Department> departmentList =  departmentMapper.queryAllEnable();
        for (Department department : departmentList) {
            List<Doctor> doctorList =  doctorMapper.queryByDepartmentHid(department.getHId());
            department.setDoctorList(doctorList);
        }

        return departmentList;
    }
}

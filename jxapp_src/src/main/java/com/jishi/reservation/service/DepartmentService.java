package com.jishi.reservation.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.jishi.reservation.dao.mapper.DepartmentMapper;
import com.jishi.reservation.dao.models.Department;
import com.jishi.reservation.service.enumPackage.EnableEnum;
import com.jishi.reservation.service.his.HisOutpatient;
import com.jishi.reservation.service.his.bean.DepartmentList;
import com.jishi.reservation.util.Helpers;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;


    @Autowired
    HisOutpatient hisOutpatient;

    /**
     * 增加科室
     * @param departmentName
     * @throws Exception
     */
    public void addDepartment(String departmentName,String position) throws Exception {
        log.info("增加科室 departmentName:"+departmentName);
        if(queryDepartment(null,departmentName).size()>0)
            throw new Exception("科室名称重复!");
        Department department = new Department();
        department.setName(departmentName);
        department.setPosition(position);
        department.setEnable(EnableEnum.EFFECTIVE.getCode());
        departmentMapper.insert(department);
    }

    /**
     * 查询科室
     * @param departmentId
     * @param departmentName
     * @return
     */
    public List<Department> queryDepartment(Long departmentId, String departmentName){
        log.info("查询科室 departmentId:"+departmentId+" departmentName:"+departmentName);
        Department queryDepartment = new Department();
        queryDepartment.setId(departmentId);
        queryDepartment.setName(departmentName);

        return departmentMapper.select(queryDepartment);
    }

    /**
     * 查询科室
     * @param departmentHisId
     */
    public List<Department> queryByHisId(String departmentHisId){
        log.info("查询科室 departmentHisId: "+departmentHisId);
        Department queryDepartment = new Department();
        queryDepartment.setHId(departmentHisId);

        return departmentMapper.select(queryDepartment);
    }

    /**
     * 通过ID号批量查询科室
     * @param departmentIds
     * @return
     * @throws Exception
     */
    public List<Department> batchQueryDepartment(List<String>  departmentIds) throws Exception {
        List<Department> departmentIdList = new ArrayList<>();
        for(String id : departmentIds)
            departmentIdList.addAll(queryDepartment(Long.valueOf(id),null));
        return departmentIdList;
    }

    /**
     * 查询全部科室
     * @return
     */
    public List<Department>  queryAllDepartment(){
        log.info("查询全部科室");
        return departmentMapper.selectAll();
    }

    /**
     * 修改科室
     * @param departmentId
     * @param departmentName
     * @param enable
     * @throws Exception
     */
    public void modifyDepartment(Long departmentId,String departmentName,String position,Integer enable) throws Exception {
        log.info("修改科室 departmentId:"+departmentId+" departmentName:"+departmentName+" enable:"+enable);
        if(Helpers.isNullOrEmpty(departmentId))
            throw new Exception("科室ID不能为空!");
        if(queryDepartment(departmentId,null).size()==0)
            throw new Exception("科室不存在!");
        Department newDepartment = new Department();
        newDepartment.setName(departmentName);
        newDepartment.setId(departmentId);
        newDepartment.setPosition(position);
        newDepartment.setEnable(enable);
        Preconditions.checkState(departmentMapper.updateByPrimaryKeySelective(newDepartment) == 1,"更新失败");
    }

    /**
     * 把科室置为无效
     * @param departmentId
     * @throws Exception
     */
    public void failureDepartment(Long departmentId) throws Exception {
        log.info("把科室置为无效 departmentId:"+departmentId);
        if(queryDepartment(departmentId,null).size()==0)
            throw new Exception("科室不存在!");
        modifyDepartment(departmentId,null,null, EnableEnum.INVALID.getCode());
    }

    public void pullFromHis() throws Exception {

        DepartmentList departmentList = hisOutpatient.selectDepartments("", "", "");
        DepartmentList.KSLIST kslist = departmentList.getKslist();
        List<DepartmentList.DepartmentHis> list = kslist.getList();
        List<Department> departmentList1 = new ArrayList<>();
        for (DepartmentList.DepartmentHis d : list) {
            log.info(JSONObject.toJSON(d));
            Department department = new Department();
            department.setName(d.getMc());
            department.setHId(d.getId());
            department.setEnable(0);
            department.setPosition("锦欣医院");
            departmentList1.add(department);

        }

        departmentMapper.insertList(departmentList1);

    }
}

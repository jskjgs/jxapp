package com.jishi.reservation.service;

import com.jishi.reservation.dao.mapper.PermissionMapper;
import com.jishi.reservation.dao.models.Permission;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zbs on 2017/8/10.
 */
@Service
@Log4j
public class PermissionService {

    @Autowired
    PermissionMapper permissionMapper;



    public List<Permission> queryAll() {
        return permissionMapper.selectAll();
    }
}

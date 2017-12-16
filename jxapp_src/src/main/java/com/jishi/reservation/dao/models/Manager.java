package com.jishi.reservation.dao.models;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by sloan on 2017/9/10.
 */


@Data
@Table(name="manager")
public class Manager {

    @Id
    private Long id;
    private String account;
    private String password;
    private Integer enable;
    private String permission;

    @Transient
    private List<Permission> permissionList;



}

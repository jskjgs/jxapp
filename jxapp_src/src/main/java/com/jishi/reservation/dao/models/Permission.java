package com.jishi.reservation.dao.models;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by sloan on 2017/10/17.
 */

@Data
@Table(name = "permission")
public class Permission {

    @Id
    private Long id ;
    private String permissionId;
    private String name;

}

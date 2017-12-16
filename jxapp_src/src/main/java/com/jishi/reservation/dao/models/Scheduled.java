package com.jishi.reservation.dao.models;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by sloan on 2017/9/1.
 */

@Data
@Table(name = "scheduled")
public class Scheduled {

    @Id
    private Long id;
    private Long doctorId;
    private Long patientId;
    private Date startTime;
    private Date endTime;
    private Integer status;
    private Integer enable;


}

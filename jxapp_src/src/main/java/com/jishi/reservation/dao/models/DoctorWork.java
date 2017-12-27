package com.jishi.reservation.dao.models;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by sloan on 2017/12/27.
 */

@Data
@Table(name = "doctor_work")
public class DoctorWork {

    private Long id;
    private String hDoctorId;
    private Date workingTime;
    private Integer enable;
}

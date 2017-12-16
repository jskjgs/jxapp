package com.jishi.reservation.controller.protocol;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by sloan on 2017/8/31.
 */

@Data
public class DateVO {

    private String day;
    private List<TimeIntervalVO> during;

}



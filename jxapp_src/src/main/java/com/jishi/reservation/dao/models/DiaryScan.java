package com.jishi.reservation.dao.models;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by sloan on 2017/10/27.
 */

@Data
@Table(name = "diary_scan")
public class DiaryScan {

    private String accountId;
    private Long diaryId;
    private Date createTime;
}

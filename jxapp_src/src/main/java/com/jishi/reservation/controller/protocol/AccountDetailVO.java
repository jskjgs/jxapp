package com.jishi.reservation.controller.protocol;

import com.jishi.reservation.dao.models.Account;
import com.jishi.reservation.dao.models.Diary;
import com.jishi.reservation.dao.models.PatientInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by sloan on 2017/11/2.
 */

@Data
public class AccountDetailVO {

    private Account account;

    private List<PatientInfo> patientInfoList;

    private List<Diary> diaryList;


}

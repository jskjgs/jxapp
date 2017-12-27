package com.jishi.reservation.worker.configurator;

/**
 * Created by liangxiong on 2017/12/27.
 */
public enum WorkerTypeEnum {
    WORKER_DIARY_UPDATE_INFO(30 * 60),
    WORKER_REGISTER_OVER_DUE(30 * 60),
    WORKER_PULL_HIS_DEPARTMENT(30 * 60),
    WORKER_PULL_HIS_DOCTOR(30 * 60),
    WORKER_OUTPATIENT_QUEUE(3 * 60),
    WORKER_REGISTER_NOTICE(30 * 60);

    // 任务过期时间（秒数）
    private int expireTime;

    WorkerTypeEnum(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getExpireTime() {
        return expireTime;
    }
}

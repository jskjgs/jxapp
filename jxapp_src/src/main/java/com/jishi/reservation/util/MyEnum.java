package com.jishi.reservation.util;

/**
 * Created by zbs on 2017/8/9.
 */
public enum MyEnum {

    /**
     * 成功
     */
    SUCCESS("SUCCESS", "成功"),

    /**
     * 数据库异常
     */
    DATABASE_EXCEPTION("DATABASE_EXCEPTION", "数据库异常"),

    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION", "未知异常"),

    /**
     * 数据库异常
     */
    DATAAPI_EXCEPTION("DATAAPI_EXCEPTION", "数据平台异常"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),

    /**
     * 空值对象
     */
    NULL_OBJECT("NULL_OBJECT", "对象为NULL"),

    /**
     * 探寻关系唯一id参数少于2
     */
    INQUIRY_KEY_LESS_THAN_TWO("INQUIRY_KEY_LESS_THAN_TWO", "探寻关系唯一id参数少于2"),

    /**
     * 空对象
     */
    EMPTY_OBJECT("EMPTY_OBJECT", "空对象"),

    /**
     * 对象小于0
     */
    LESS_THAN_ZERO("LESS_THAN_ZERO", "对象小于0"),

    /**
     * 任务已存在
     */
    JOB_EXISTS("JOB_EXISTS", "任务已存在"),

    /**
     * 任务未完成
     */
    JOB_NOT_FINISH("JOB_NOT_FINISH", "任务未完成"),

    /**
     * 文件不存在
     */
    FILE_NOT_EXIST("FILE_NOT_EXIST", "文件不存在"),

    /**
     * JOB不存在
     */
    JOB_NOT_EXIST("JOB_NOT_EXIST", "JOB不存在"),

    /**
     * 模板名称已经存在
     */
    TEMPLATE_NAME_EXIST("TEMPLATE_NAME_EXIST", "模板名称已经存在"),

    INVALID_JOB_ID("INVALID_JOB_ID", "无效的jobId"),

    /**
     * JOB在数据库中不存在
     */
    JOB_NOT_EXIST_IN_DB("JOB_NOT_EXIST_IN_DB", "JOB在数据库中不存在"),

    /**
     * 关联方数据错误：没有目标公司
     */
    RELATION_DATA_NO_TARGET_NODE("RELATION_DATA_NO_TARGET_NODE", "关联方数据错误：没有目标公司 "),

    /**
     * 关联方数据错误：未知的关联关系
     */
    RELATION_DATA_UNKNOWN_RELATION_TYPE("RELATION_DATA_UNKNOWN_RELATION_TYPE", "关联方数据错误：未知的关联关系 "), ;

    /**
     * 枚举值
     */
    private String code;

    /**
     * 枚举描述
     */
    private String desc;

    /**
     * 构造函数
     *
     * @param code 枚举值
     * @param desc 枚举描述
     */
    private MyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

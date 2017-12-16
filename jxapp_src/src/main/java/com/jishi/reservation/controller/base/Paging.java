package com.jishi.reservation.controller.base;

import com.jishi.reservation.util.Helpers;
import lombok.Data;

/**
 * Created by zbs on 2017/8/11.
 */
@Data
public class Paging {

    private Paging(){}

    private Paging(int pageNum,int pageSize,String orderBy){
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.orderBy = orderBy;
    }

    public static Paging create(Integer pageNum,Integer pageSize,String orderBy,Boolean desc){
        //如果全为空,给一个初始值
        if(Helpers.isNullOrEmpty(pageNum,pageSize,orderBy,desc))
            return new Paging(1,10000,null);
        Paging paging = new Paging();
        if(!Helpers.isNull(desc) && !Helpers.isNull(orderBy))
            paging.setOrderBy(orderBy + (desc ? " desc" : ""));
        else
            paging.setOrderBy(orderBy);
        paging.setPageNum(pageNum!=null?pageNum:1);
        paging.setPageSize(pageSize!=null?pageSize:1000);
        return paging;
    }

    int pageNum = 1;
    int pageSize = 10;
    String orderBy = null;
    Boolean desc = null;
}

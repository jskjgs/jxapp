package com.jishi.reservation.dao.mapper;

import com.doraemon.base.dao.base.MyMapper;
import com.jishi.reservation.dao.models.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper extends MyMapper<Account> {


    @Select({
            "SELECT * FROM account where account = ${account} and passwd = ${password}"
    })
    Account selectByAccountAndPassword(@Param("account") String account,@Param("password") String password);


    @Select({
            "select * from account where account = #{phone}"
    })
    Account queryByTelephone(@Param("phone") String phone);


    @Select({
            "select * from account where id = #{accountId}"
    })
    Account queryById(@Param("accountId") Long accountId);


    @Select({
            "<script>select  * from account where 1 =1  " +
                    "<if test = \"key != null\"> AND ( nick like concat('%',#{key},'%') or phone like concat('%',#{key},'%') ) </if>" +

                    "</script>"
    })
    List<Account> queryCondition(@Param("key") String key);
}

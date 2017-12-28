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
            "SELECT * FROM account where account = ${account} and passwd = ${password} and enable=0"
    })
    Account selectByAccountAndPassword(@Param("account") String account,@Param("password") String password);


    @Select({
            "select * from account where account = #{phone} and enable=0"
    })
    Account queryByTelephone(@Param("phone") String phone);


    @Select({
            "select * from account where id = #{accountId} and enable=0"
    })
    Account queryById(@Param("accountId") Long accountId);


    @Select({
            "<script>select  * from account where enable=0  " +
                    "<if test = \"key != null\"> AND ( nick like concat('%',#{key},'%') or phone like concat('%',#{key},'%') ) </if>" +

                    "</script>"
    })
    List<Account> queryCondition(@Param("key") String key);
}

package com.jishi.reservation.dao.mapper;

import com.jishi.reservation.dao.models.IdentityInfo;
import com.doraemon.base.dao.base.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentityInfoMapper extends MyMapper<IdentityInfo>{


    @Select({
            "select * from identity_info where account_id = #{accountId}"
    })
    List<IdentityInfo> queryByAccountId(@Param("accountId") Long accountId);

    @Select({
            "select * from identity_info where account_id = #{accountId} and identity_code = #{identityCode}"
    })
    IdentityInfo queryByAccountIdAndIdentity(@Param("accountId") Long accountId,@Param("identityCode") String identityCode);
}

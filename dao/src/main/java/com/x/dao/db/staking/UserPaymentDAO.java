package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface UserPaymentDAO {

    int save(@Param("entity") UserPaymentEntity user);

    int saveBulk(@Param("entities") List<UserPaymentEntity> users);

    List<UserPaymentEntity> selectByChainAndAddresses(@Param("chainId") Long chainId, @Param("addresses") Set<String> addresses);
}

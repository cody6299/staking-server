package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface UserDailyPaymentDAO {

    int save(@Param("entity") UserDailyPaymentEntity user);

    int saveBulk(@Param("entities") List<UserDailyPaymentEntity> users);

    List<UserDailyPaymentEntity> selectByChainAndDateAndAddresses(@Param("chainId") Long chainId, @Param("date") Date date, @Param("addresses") Set<String> addresses);

    List<UserDailyPaymentEntity> selectRemainedByChain(@Param("chainId") Long chainId, @Param("from") int from, @Param("size") int size);
}

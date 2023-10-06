package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface UserDailyRewardDAO {

    int save(@Param("entity") UserDailyRewardEntity user);

    int saveBulk(@Param("entities") List<UserDailyRewardEntity> users);

    int updateSended(@Param("entities") List<UserDailyRewardEntity> users);

    List<UserDailyRewardEntity> selectUnsended(@Param("from") int from, @Param("size") int size);

    BigDecimal selectTotalSendedByChanAndUserLessDate(@Param("chainId") Long chainId, @Param("chainAddress") String chainAddress, @Param("date") Date date);

}

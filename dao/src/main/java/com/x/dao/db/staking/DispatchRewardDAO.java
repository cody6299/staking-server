package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface DispatchRewardDAO {

    int save(@Param("entity") DispatchRewardEntity user);

    int saveBulk(@Param("entities") List<DispatchRewardEntity> users);

    int updateHash(@Param("entities") List<DispatchRewardEntity> users);

    List<DispatchRewardEntity> selectByDate(@Param("date") Date date, @Param("from") int from, @Param("size") int size);

    List<DispatchRewardEntity> selectByAddressesAndDate(@Param("date") Date date, @Param("addresses") Set<String> addresses);

}

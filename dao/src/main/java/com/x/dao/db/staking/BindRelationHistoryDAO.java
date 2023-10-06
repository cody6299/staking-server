package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface BindRelationHistoryDAO {

    int save(@Param("entity") BindRelationHistoryEntity user);

    List<BindRelationHistoryEntity> selectByChainAndAddresses(@Param("chainId") Long chainId, @Param("addresses") Set<String> addresses);

}

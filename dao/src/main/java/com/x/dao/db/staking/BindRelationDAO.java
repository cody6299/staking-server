package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface BindRelationDAO {

    int save(@Param("entity") BindRelationEntity user);

    List<BindRelationEntity> selectByChainAndAddresses(@Param("chainId") Long chainId, @Param("addresses") Set<String> addresses);

    int update(@Param("entity") BindRelationEntity user, @Param("oldBindAddress") String oldBindAddress);
}

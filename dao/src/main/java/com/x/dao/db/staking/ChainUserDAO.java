package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface ChainUserDAO {

    int save(@Param("entity") ChainUserEntity user);

    int saveBulk(@Param("entities") List<ChainUserEntity> users);

    List<ChainUserEntity> selectByChainAndAddresses(@Param("chainId") Long chainId, @Param("addresses") Set<String> addresses);

    List<ChainUserEntity> selectByChain(@Param("chainId") Long chainId, @Param("from") int from, @Param("size") int size);

}

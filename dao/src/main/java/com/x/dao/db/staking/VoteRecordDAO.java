package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.x.dao.po.staking.*;

@Mapper
public interface VoteRecordDAO {

    int save(@Param("entity") VoteRecordEntity record);

    int saveBulk(@Param("entities") List<VoteRecordEntity> records);

    List<VoteRecordEntity> selectByChainAndBlockAndVoteAt(@Param("chainId") Long chainId, @Param("endBlock") Long endBlock, @Param("beginTime") Long beginTime, @Param("endTime") Long endTime, @Param("from") int from, @Param("size") int size);

}

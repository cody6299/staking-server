package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.x.dao.po.staking.*;

@Mapper
public interface VoteRecordProcessDAO {

    int save(@Param("entity") VoteRecordProcessEntity record);

    VoteRecordProcessEntity selectByChain(@Param("chainId") Long chainId);

}

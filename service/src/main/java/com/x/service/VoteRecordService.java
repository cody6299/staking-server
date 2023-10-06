package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface VoteRecordService {

    public int save(VoteRecordEntity record);

    public int saveBulk(List<VoteRecordEntity> records);

    public List<VoteRecordEntity> getByChainAndProcessAndVoteAt(ChainEntity chain, Long endBlock, Long beginTime, Long endTime, int from, int size);

}

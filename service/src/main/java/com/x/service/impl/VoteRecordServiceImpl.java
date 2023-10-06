package com.x.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.db.staking.*;
import com.x.dao.po.staking.*;
import com.x.service.*;

@Slf4j
@Service
public class VoteRecordServiceImpl implements VoteRecordService {

    @Resource
    VoteRecordDAO voteRecordDAO;

    public int save(VoteRecordEntity record) {
        return voteRecordDAO.save(record);
    }

    public int saveBulk(List<VoteRecordEntity> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        return voteRecordDAO.saveBulk(records);
    }

    public List<VoteRecordEntity> getByChainAndProcessAndVoteAt(ChainEntity chain, Long endBlock, Long beginTime, Long endTime, int from, int size) {
        return voteRecordDAO.selectByChainAndBlockAndVoteAt(chain.getId(), endBlock, beginTime, endTime, from, size);
    }

}

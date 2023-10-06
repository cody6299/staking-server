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
public class VoteRecordProcessServiceImpl implements VoteRecordProcessService {

    @Resource
    VoteRecordProcessDAO voteRecordProcessDAO;

    public int save(VoteRecordProcessEntity record) {
        return voteRecordProcessDAO.save(record);
    }

    public VoteRecordProcessEntity getByChain(ChainEntity chain) {
        VoteRecordProcessEntity res = voteRecordProcessDAO.selectByChain(chain.getId());
        if (res == null) {
            res = new VoteRecordProcessEntity();
            res.setChainId(chain.getId());
            res.setBlockNum(0L);
        }
        return res;
    }

}

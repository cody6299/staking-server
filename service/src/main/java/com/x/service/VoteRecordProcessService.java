package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface VoteRecordProcessService {

    public int save(VoteRecordProcessEntity record);

    public VoteRecordProcessEntity getByChain(ChainEntity chain);

}

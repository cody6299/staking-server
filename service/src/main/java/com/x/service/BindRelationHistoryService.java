package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface BindRelationHistoryService {

    public int save(BindRelationHistoryEntity user);

    public List<BindRelationHistoryEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses);

}

package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface BindRelationService {

    public int save(BindRelationEntity user);

    public List<BindRelationEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses);

    public int update(BindRelationEntity user, String oldBindAddress);
}

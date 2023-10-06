package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface ChainUserService {

    public int save(ChainUserEntity user);

    public int saveBulk(List<ChainUserEntity> users);

    public List<ChainUserEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses);

    public List<ChainUserEntity> getByChain(ChainEntity chain, int from, int size);

}

package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface UserDailyPaymentService {

    public int save(UserDailyPaymentEntity user);

    public int saveBulk(List<UserDailyPaymentEntity> users);

    public List<UserDailyPaymentEntity> getByChainAndDateAndAddresses(ChainEntity chain, Date date, Set<String> addresses);
    
    public List<UserDailyPaymentEntity> getRemainedByChain(ChainEntity chain, int from, int size);

}

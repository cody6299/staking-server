package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface UserPaymentService {

    public int save(UserPaymentEntity user);

    public int saveBulk(List<UserPaymentEntity> users);

    public List<UserPaymentEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses);

}

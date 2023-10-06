package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface UserDailyRewardService {

    public int save(UserDailyRewardEntity user);

    public int saveBulk(List<UserDailyRewardEntity> users);

    public int updateSended(List<UserDailyRewardEntity> users);

    List<UserDailyRewardEntity> getUnsended(int from, int size);

    BigDecimal getTotalSendedByChanAndUserLessDate(ChainEntity chain, String chainAddress, Date date);
}

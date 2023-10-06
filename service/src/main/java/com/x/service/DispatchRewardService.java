package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface DispatchRewardService {

    public int save(DispatchRewardEntity user);

    public int saveBulk(List<DispatchRewardEntity> users);

    public int updateHash(List<DispatchRewardEntity> users);

    public List<DispatchRewardEntity> getByDate(Date date, int from, int size);

    public List<DispatchRewardEntity> getByAddressesAndDate(Date date, Set<String> addresses);

}

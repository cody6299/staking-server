package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface ChainService {

    public int save(ChainEntity chain);

    public List<ChainEntity> getAll();

    public List<ChainEntity> getByChains(List<String> chains);

    public List<ChainEntity> getByIds(List<Long> ids);

}

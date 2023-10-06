package com.x.service.impl;

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
public class ChainServiceImpl implements ChainService {
    
    @Resource
    ChainDAO chainDAO;

    public int save(ChainEntity chain) {
        return chainDAO.save(chain);
    }

    public List<ChainEntity> getAll() {
        return chainDAO.selectAll();
    }

    public List<ChainEntity> getByChains(List<String> chains) {
        return chainDAO.selectByChains(chains);
    }

    public List<ChainEntity> getByIds(List<Long> ids) {
        return chainDAO.selectByIds(ids);
    }

}

package com.x.service;

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
public class BindRelationHistoryServiceImpl implements BindRelationHistoryService {

    @Resource
    BindRelationHistoryDAO bindRelationHistoryDAO;

    public int save(BindRelationHistoryEntity user) {
        return bindRelationHistoryDAO.save(user);
    }

    public List<BindRelationHistoryEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return bindRelationHistoryDAO.selectByChainAndAddresses(chain.getId(), addresses);
    }
}

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
public class BindRelationServiceImpl implements BindRelationService {

    @Resource
    BindRelationDAO bindRelationDAO;

    public int save(BindRelationEntity user) {
        return bindRelationDAO.save(user);
    }

    public List<BindRelationEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return bindRelationDAO.selectByChainAndAddresses(chain.getId(), addresses);
    }

    public int update(BindRelationEntity user, String oldBindAddress) {
        return bindRelationDAO.update(user, oldBindAddress);
    }
}

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
public class ChainUserServiceImpl implements ChainUserService {

    @Resource
    ChainUserDAO chainUserDAO;

    public int save(ChainUserEntity user) {
        return chainUserDAO.save(user);
    }

    public int saveBulk(List<ChainUserEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return chainUserDAO.saveBulk(users);
    }

    public List<ChainUserEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return chainUserDAO.selectByChainAndAddresses(chain.getId(), addresses);
    }

    public List<ChainUserEntity> getByChain(ChainEntity chain, int from, int size) {
        return chainUserDAO.selectByChain(chain.getId(), from, size);
    }
}

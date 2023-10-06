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
public class UserDailyPaymentServiceImpl implements UserDailyPaymentService {

    @Resource
    UserDailyPaymentDAO userDailyPaymentDAO;

    public int save(UserDailyPaymentEntity user) {
        return userDailyPaymentDAO.save(user);
    }

    public int saveBulk(List<UserDailyPaymentEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return userDailyPaymentDAO.saveBulk(users);
    }

    public List<UserDailyPaymentEntity> getByChainAndDateAndAddresses(ChainEntity chain, Date date, Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return userDailyPaymentDAO.selectByChainAndDateAndAddresses(chain.getId(), date, addresses);
    }

    public List<UserDailyPaymentEntity> getRemainedByChain(ChainEntity chain, int from, int size) {
        return userDailyPaymentDAO.selectRemainedByChain(chain.getId(), from, size);
    }
}

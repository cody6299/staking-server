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
public class UserPaymentServiceImpl implements UserPaymentService {

    @Resource
    UserPaymentDAO userPaymentDAO;

    public int save(UserPaymentEntity user) {
        return userPaymentDAO.save(user);
    }

    public int saveBulk(List<UserPaymentEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return userPaymentDAO.saveBulk(users);
    }

    public List<UserPaymentEntity> getByChainAndAddresses(ChainEntity chain, Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return userPaymentDAO.selectByChainAndAddresses(chain.getId(), addresses);
    }
}

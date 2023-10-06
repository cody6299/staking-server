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
public class UserDailyRewardServiceImpl implements UserDailyRewardService {

    @Resource
    UserDailyRewardDAO userDailyRewardDAO;

    public int save(UserDailyRewardEntity user) {
        return userDailyRewardDAO.save(user);
    }

    public int saveBulk(List<UserDailyRewardEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return userDailyRewardDAO.saveBulk(users);
    }

    public int updateSended(List<UserDailyRewardEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return userDailyRewardDAO.updateSended(users);
    }

    public List<UserDailyRewardEntity> getUnsended(int from, int size) {
        return userDailyRewardDAO.selectUnsended(from, size);
    }

    public BigDecimal getTotalSendedByChanAndUserLessDate(ChainEntity chain, String chainAddress, Date date) {
        return userDailyRewardDAO.selectTotalSendedByChanAndUserLessDate(chain.getId(), chainAddress, date);
    }
}

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
public class DispatchRewardServiceImpl implements DispatchRewardService {

    @Resource
    DispatchRewardDAO dispatchRewardDAO;

    public int save(DispatchRewardEntity user) {
        return dispatchRewardDAO.save(user);
    }

    public int saveBulk(List<DispatchRewardEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return dispatchRewardDAO.saveBulk(users);
    }

    public int updateHash(List<DispatchRewardEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return dispatchRewardDAO.updateHash(users);
    }

    public List<DispatchRewardEntity> getByDate(Date date, int from, int size) {
        return dispatchRewardDAO.selectByDate(date, from, size);
    }

    public List<DispatchRewardEntity> getByAddressesAndDate(Date date, Set<String> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        } else {
            return dispatchRewardDAO.selectByAddressesAndDate(date, addresses);
        }
    }
}

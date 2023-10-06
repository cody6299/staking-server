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
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Resource
    PaymentInfoDAO paymentInfoDAO;

    public int save(PaymentInfoEntity user) {
        return paymentInfoDAO.save(user);
    }

    public PaymentInfoEntity getByDate(Date date) {
        return paymentInfoDAO.selectByDate(date);
    }

    public int reset(Date date, int oldStatus) {
        return paymentInfoDAO.reset(date, oldStatus);
    }

    public int update(PaymentInfoEntity paymentInfo) {
        return paymentInfoDAO.update(paymentInfo);
    }

    public int updateRootHash(Date date, String rootHash) {
        return paymentInfoDAO.updateRootHash(date, rootHash);
    }
    
    public int updateStatus(Date date, int oldStatus, int newStatus) {
        return paymentInfoDAO.updateStatus(date, oldStatus, newStatus);
    }

}

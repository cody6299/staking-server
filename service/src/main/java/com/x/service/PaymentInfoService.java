package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface PaymentInfoService {

    public int save(PaymentInfoEntity user);

    public PaymentInfoEntity getByDate(Date date);

    public int reset(Date date, int oldStatus);

    public int update(PaymentInfoEntity paymentInfo);

    int updateRootHash(Date date, String rootHash);

    int updateStatus(Date date, int oldStatus, int newStatus);

}

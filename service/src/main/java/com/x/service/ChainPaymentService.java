package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface ChainPaymentService {

    void doChainPayment(PaymentInfoEntity paymentInfo, ChainEntity chain);

}

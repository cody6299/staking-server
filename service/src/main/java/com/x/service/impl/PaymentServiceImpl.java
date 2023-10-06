package com.x.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import com.x.dao.po.staking.*;
import java.math.BigDecimal;
import com.x.service.*;
import java.util.*;
import java.*;

import com.x.common.constants.*;
import com.x.common.utils.*;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    ChainService chainService;
    @Resource
    ChainPaymentService chainPaymentService;
    @Resource
    DispatchPaymentService dispatchPaymentService;
    @Resource
    PaymentInfoService paymentInfoService;

    PaymentInfoEntity getCurrentPaymentInfo(Date date) {
        PaymentInfoEntity currentPaymentInfo = paymentInfoService.getByDate(date);
        if (currentPaymentInfo != null) {
            throw new RuntimeException("another payment is processing");
        }
        currentPaymentInfo = new PaymentInfoEntity();
        currentPaymentInfo.setDate(date);
        currentPaymentInfo.setStartSeconds(DateUtil.getBeginTime(date));
        currentPaymentInfo.setEndSeconds(DateUtil.getEndTime(date));
        currentPaymentInfo.setCurrentRecordId(0L);
        currentPaymentInfo.setDispatchReward(BigDecimal.ZERO);
        currentPaymentInfo.setRootHash(Constant.EMPTY_HASH);
        currentPaymentInfo.setStatus(Constant.PAYMENT_STATUS_PROCESS);
        int res = paymentInfoService.save(currentPaymentInfo);
        if (res != 1) {
            throw new RuntimeException("another payment is processing");
        }
        return currentPaymentInfo;
    }

    public void doPayment(Date date) {
        PaymentInfoEntity paymentInfo = null;
        try {
            paymentInfo = getCurrentPaymentInfo(date);
            List<ChainEntity> chains = chainService.getAll();
            for (int i = 0; i < chains.size(); i ++) {
                chainPaymentService.doChainPayment(paymentInfo, chains.get(i)); 
            }
            dispatchPaymentService.doPayment(paymentInfo);
            int res = paymentInfoService.updateStatus(date, Constant.PAYMENT_STATUS_PROCESS, Constant.PAYMENT_STATUS_SUCCESS);
            if (res != 1) {
                throw new RuntimeException("another payment is processing");
            }
            log.info("doPayment finish. date={} chainsNum={}", date, chains.size());
        } catch (Exception ex) {
            if (paymentInfo != null) {
                paymentInfoService.updateStatus(date, Constant.PAYMENT_STATUS_PROCESS, Constant.PAYMENT_STATUS_FAILED);
            }
            throw new RuntimeException(ex);
        }
    }

}

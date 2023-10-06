package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class UserDailyPaymentEntity {
    Long id;
    Long chainId;
    String chainAddress;
    Date date;
    BigDecimal totalReward;
    BigDecimal dailyReward;
    BigDecimal remainRelease;
    BigDecimal dailyRelease;
    Date createAt;
    Date updateAt;
}

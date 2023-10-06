package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class UserPaymentEntity {
    Long chainId;
    String chainAddress;
    BigDecimal totalReward;
    Date createAt;
    Date updateAt;
}

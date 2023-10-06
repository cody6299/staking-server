package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class ChainEntity {
    Long id;
    String chain;
    Integer decimals;
    BigDecimal rewardPerSeconds;
    BigDecimal totalShares;
    BigDecimal accPerShare;
    Long lastUpdate;
    Date createAt;
    Date updateAt;
}

package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class UserDailyRewardEntity {
    Long id;
    Long chainId;
    String chainAddress;
    Date date;
    BigDecimal reward;
    BigDecimal sended;
    String sendAddress;
    Date createAt;
    Date updateAt;
}

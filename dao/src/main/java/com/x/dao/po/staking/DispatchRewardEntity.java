package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class DispatchRewardEntity {
    String address;
    Date date;
    BigDecimal totalAmount;
    String hash;
    Date createAt;
    Date updateAt;
}

package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class UserEntity {
    Long chainId;
    String chainAddress;
    BigDecimal shares;
    BigDecimal reward;
    BigDecimal debt;
    Date createAt;
    Date updateAt;
}

package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class PriceEntity {
    Long id;
    String address;
    String token;
    Integer decimals;
    BigDecimal price;
    Date createAt;
    Date updateAt;
}

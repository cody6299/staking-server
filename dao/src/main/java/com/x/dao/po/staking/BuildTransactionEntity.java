package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class BuildTransactionEntity {
    Long id;
    String from;
    String to;
    BigDecimal value;
    String input;
    String inputDesc;
    String rawTransaction;
    Integer status;
    String hash;
    Date createAt;
    Date updateAt;
}

package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class PaymentInfoEntity {
    Date date;
    Long startSeconds;
    Long endSeconds;
    Long currentRecordId;
    BigDecimal dispatchReward;
    String rootHash;
    Integer status;
    Date createAt;
    Date updateAt;
}

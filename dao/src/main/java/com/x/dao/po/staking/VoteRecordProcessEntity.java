package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class VoteRecordProcessEntity {
    Long chainId;
    Long blockNum;
    Date createAt;
    Date updateAt;
}

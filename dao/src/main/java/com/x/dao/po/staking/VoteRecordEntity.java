package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class VoteRecordEntity {
    Long id;
    Long chainId;
    String chainAddress;
    BigDecimal voteNum;
    Long blockNum;
    String txHash;
    String voteTo;
    Long voteAt;
    Date createAt;
    Date updateAt;
}

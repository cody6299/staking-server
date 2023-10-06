package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class BindRelationHistoryEntity {
    Long id;
    Long chainId;
    String chainAddress;
    String bindAddress;
    String signData;
    Date createAt;
    Date updateAt;
}

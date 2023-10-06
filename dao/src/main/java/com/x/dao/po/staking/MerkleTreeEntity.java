package com.x.dao.po.staking;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class MerkleTreeEntity {
    Date date;
    String hash;
    String brotherHash;
    String parentHash;
    String address;
    Integer height;
    Date createAt;
    Date updateAt;
}

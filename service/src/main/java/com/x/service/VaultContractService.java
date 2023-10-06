package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;
import com.x.model.bean.*;

public interface VaultContractService {

    String updateRootHash(String hash, BigDecimal totalReward, Long updateAt);

}

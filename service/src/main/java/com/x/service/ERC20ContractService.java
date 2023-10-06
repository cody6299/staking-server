package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;
import com.x.model.bean.*;

public interface ERC20ContractService {

    public TokenInfoBean getTokenInfo(String address);

}

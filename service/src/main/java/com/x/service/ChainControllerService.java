package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;
import com.x.model.vo.*;

public interface ChainControllerService {

    public CallResult<ChainVO> getChains();
}

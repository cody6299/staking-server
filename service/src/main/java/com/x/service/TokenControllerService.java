package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;
import com.x.model.vo.*;

public interface TokenControllerService {

    public CallResult<ERC20InfoVO> getERC20Info(ERC20InfoParam param);
}

package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;
import com.x.model.vo.*;

public interface UserControllerService {

    public CallResult<UserProfitVO> getUserProfit(UserProfitParam param);

    public CallResult<BindAddressVO> bindAddress(BindAddressParam param);

    public CallResult<BindRelationVO> getBindRelation(BindRelationParam param);

    public CallResult<UserClaimVO> getUserClaim(UserClaimParam param);
}

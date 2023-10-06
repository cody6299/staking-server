package com.x.service.impl;

import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.common.constants.*;
import com.x.dao.db.staking.*;
import com.x.dao.po.staking.*;
import com.x.model.bean.*;
import com.x.model.vo.*;
import com.x.service.*;

@Slf4j
@Service
public class TokenControllerServiceImpl implements TokenControllerService {
    
    @Resource
    PriceService priceService;
    @Resource
    ERC20ContractService erc20ContractService;

    public CallResult<ERC20InfoVO> getERC20Info(ERC20InfoParam param) {
        ERC20InfoVO res = new ERC20InfoVO();
        if (param.getId() != null) {
            PriceEntity token = priceService.getById(param.getId());
            if (token != null) {
                param.setAddress(token.getAddress());
            }
            res.setPrice(token.getPrice());
        }
        if (param.getAddress() == null) {
            return CallResult.success(res);
        }

        TokenInfoBean tokenInfo = erc20ContractService.getTokenInfo(param.getAddress());
        res.setId(param.getId());
        res.setToken(tokenInfo.getSymbol());
        res.setTotalSupply(tokenInfo.getTotalSupply());
        if (res.getTotalSupply() != null && res.getPrice() != null) {
            res.setMarketcap(res.getTotalSupply().divide(BigDecimal.TEN.pow(tokenInfo.getDecimals())).multiply(res.getPrice()));
        }
        return CallResult.success(res);
    }
}

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
import com.x.model.vo.*;
import com.x.service.*;

@Slf4j
@Service
public class ChainControllerServiceImpl implements ChainControllerService {
    
    @Resource
    ChainService chainService;
    @Resource
    PriceService priceService;

    public CallResult<ChainVO> getChains() {
        ChainVO res = new ChainVO();
        List<PriceEntity> prices = priceService.getAll();
        Map<Long, PriceEntity> priceMap = prices.stream().collect(Collectors.toMap(PriceEntity::getId, price -> price));
        List<ChainEntity> chains = chainService.getAll();
        for (ChainEntity chain : chains) {
            if (chain.getId() == Constant.CHAIN_ID_ETH) {
                continue;
            }
            ChainVO.Chain vo = new ChainVO.Chain();
            vo.setId(chain.getId());
            vo.setChain(chain.getChain());
            vo.setTotalVote(chain.getTotalShares().divide(BigDecimal.TEN.pow(chain.getDecimals())));
            vo.setPrice(priceMap.get(chain.getId()).getPrice());
            vo.setTotalValue(vo.getPrice().multiply(vo.getTotalVote()));
            if (vo.getTotalValue().compareTo(BigDecimal.ZERO) > 0) {
                vo.setApr(
                    chain.getRewardPerSeconds()
                        .divide(BigDecimal.TEN.pow(priceMap.get(Constant.TOKEN_ID_FINGO).getDecimals()))
                        .multiply(new BigDecimal(3600 * 24 * 365)).divide(vo.getTotalValue(), 16, BigDecimal.ROUND_DOWN)
                );
            }
            res.getChains().add(vo);
        }
        return CallResult.success(res);
    }
}

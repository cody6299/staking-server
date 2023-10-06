package com.x.service.impl;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.db.staking.*;
import com.x.dao.po.staking.*;
import com.x.model.vo.*;
import com.x.service.*;

@Slf4j
@Service
public class PriceControllerServiceImpl implements PriceControllerService {
    
    @Resource
    PriceService priceService;

    public CallResult<PriceVO> getPrices() {
        PriceVO res = new PriceVO();
        List<PriceEntity> prices = priceService.getAll();
        for (PriceEntity price : prices) {
            PriceVO.Price vo = new PriceVO.Price();
            vo.setId(price.getId());
            vo.setToken(price.getToken());
            vo.setPrice(price.getPrice());
            res.getPrices().add(vo);
        }
        return CallResult.success(res);
    }
}

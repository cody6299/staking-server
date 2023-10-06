package com.x.service.impl;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.db.staking.*;
import com.x.dao.po.staking.*;
import com.x.service.*;

@Slf4j
@Service
public class PriceServiceImpl implements PriceService {
    
    @Resource
    PriceDAO priceDAO;

    public int save(PriceEntity price) {
        return priceDAO.save(price);
    }

    public int saveBulk(List<PriceEntity> prices) {
        return priceDAO.saveBulk(prices);
    }

    public List<PriceEntity> getAll() {
        return priceDAO.selectAll();
    }

    public PriceEntity getById(Long id) {
        return priceDAO.selectById(id);
    }

}

package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface PriceService {

    public int save(PriceEntity price);

    public int saveBulk(List<PriceEntity> prices);

    public List<PriceEntity> getAll();

    public PriceEntity getById(Long id);

}

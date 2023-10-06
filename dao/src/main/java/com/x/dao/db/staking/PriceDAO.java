package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.x.dao.po.staking.*;

@Mapper
public interface PriceDAO {

    int save(@Param("entity") PriceEntity price);

    int saveBulk(@Param("entities") List<PriceEntity> prices);

    List<PriceEntity> selectAll();

    PriceEntity selectById(@Param("id") Long id);

}

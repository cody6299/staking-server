package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.x.dao.po.staking.*;

@Mapper
public interface ChainDAO {

    int save(@Param("entity") ChainEntity chain);

    List<ChainEntity> selectAll();

    List<ChainEntity> selectByChains(@Param("chains") List<String> chains);

    List<ChainEntity> selectByIds(@Param("ids") List<Long> ids);

}

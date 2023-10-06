package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface PaymentInfoDAO {

    int save(@Param("entity") PaymentInfoEntity user);

    PaymentInfoEntity selectByDate(@Param("date") Date date);

    int reset(@Param("date") Date date, @Param("oldStatus") Integer oldStatus);

    int update(@Param("entity") PaymentInfoEntity paymentInfo);

    int updateRootHash(@Param("date") Date date, @Param("rootHash") String rootHash);

    int updateStatus(@Param("date") Date date, @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

}

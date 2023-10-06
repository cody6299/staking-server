package com.x.service;

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
public class BuildTransactionServiceImpl implements BuildTransactionService {

    @Resource
    BuildTransactionDAO buildTransactionDAO;

    public int save(BuildTransactionEntity user) {
        return buildTransactionDAO.save(user);
    }

}

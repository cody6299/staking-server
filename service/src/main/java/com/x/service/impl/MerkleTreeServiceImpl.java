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
public class MerkleTreeServiceImpl implements MerkleTreeService {

    @Resource
    MerkleTreeDAO merkleTreeDAO;

    public int save(MerkleTreeEntity user) {
        return merkleTreeDAO.save(user);
    }

    public int saveBulk(List<MerkleTreeEntity> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }
        return merkleTreeDAO.saveBulk(users);
    }

    public MerkleTreeEntity getByDateAndHash(Date date, String hash) {
        return merkleTreeDAO.selectByDateAndHash(date, hash);
    }
}

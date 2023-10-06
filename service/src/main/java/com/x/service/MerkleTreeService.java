package com.x.service;

import java.math.BigDecimal;
import java.util.*;
import java.*;

import com.x.dao.po.staking.*;

public interface MerkleTreeService {

    public int save(MerkleTreeEntity user);

    public int saveBulk(List<MerkleTreeEntity> users);

    public MerkleTreeEntity getByDateAndHash(Date date, String hash); 
}

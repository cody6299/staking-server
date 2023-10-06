package com.x.dao.db.staking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.*;

import com.x.dao.po.staking.*;

@Mapper
public interface MerkleTreeDAO {

    int save(@Param("entity") MerkleTreeEntity user);

    int saveBulk(@Param("entities") List<MerkleTreeEntity> users);

    MerkleTreeEntity selectByDateAndHash(@Param("date") Date date, @Param("hash") String hash);
}

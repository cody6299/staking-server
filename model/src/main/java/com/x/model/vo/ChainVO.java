package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class ChainVO {
    
    @Data
    public static class Chain {
        @JsonProperty(value = "id")
        Long id;
        @JsonProperty(value = "chain")
        String chain;
        @JsonProperty(value = "total_vote")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal totalVote;
        @JsonProperty(value = "price")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal price;
        @JsonProperty(value = "total_value")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal totalValue;
        @JsonProperty(value = "apr")
        BigDecimal apr;
    }
    @JsonProperty(value = "chains")
    List<Chain> chains = new ArrayList<>();

}

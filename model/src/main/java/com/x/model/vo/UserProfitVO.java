package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class UserProfitVO {
    
    @Data
    public static class Profit {
        @JsonProperty(value = "chain_id")
        Long chainId;
        @JsonProperty(value = "staked_amount")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal stakedAmount;
        @JsonProperty(value = "staked_value")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal stakedValue;
        @JsonProperty(value = "earned_token")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal earnedToken;
        @JsonProperty(value = "unlocked_token")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal unlockedToken;
        @JsonProperty(value = "locked_token")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal lockedToken;
    }
    @JsonProperty(value = "profits")
    List<Profit> profits = new ArrayList<>();

}

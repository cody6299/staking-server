package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class UserClaimVO {
    
    @Data
    public static class Claim {
        @JsonProperty(value = "address")
        String address;
        @JsonProperty(value = "amount")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal amount;
        @JsonProperty(value = "proofs")
        List<String> proofs = new ArrayList<>();
    }

    @Data
    public static class ChainUser {
        @JsonProperty(value = "chain_id")
        Long chainId;
        @JsonProperty(value = "chain_address")
        String chainAddress;
        @JsonProperty(value = "claims")
        List<Claim> claims = new ArrayList<>();
        
    }

    @JsonProperty(value = "chains")
    List<ChainUser> chains = new ArrayList<>();

}

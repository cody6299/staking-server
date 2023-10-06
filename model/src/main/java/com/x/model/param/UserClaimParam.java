package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class UserClaimParam {

    @Data
    public static class ChainUser {
        @JsonProperty(value = "chain_id")
        Long chainId;
        @JsonProperty(value = "chain_address")
        String chainAddress;
    }

    @JsonProperty(value = "users")
    List<ChainUser> users = new ArrayList<>();
    @JsonProperty(value = "date")
    Date date = new Date();
}

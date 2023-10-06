package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class BindRelationVO {

    @Data
    public static class History {
        @JsonProperty(value = "bind_address")
        String bindAddress;
        @JsonProperty(value = "create_at")
        Long createAt;
    }

    @Data
    public static class Relation {
        @JsonProperty(value = "chain_id")
        Long chainId;
        @JsonProperty(value = "chain_address")
        String chainAddress;
        @JsonProperty(value = "bind_address")
        String bindAddress;
        @JsonProperty(value = "history")
        List<History> bindHistory = new ArrayList<>();
    }
    @JsonProperty(value = "relations")
    List<Relation> relations = new ArrayList<>();
}

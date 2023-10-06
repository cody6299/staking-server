package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class ERC20InfoVO {

    @JsonProperty(value = "id")
    Long id;
    @JsonProperty(value = "token")
    String token;
    @JsonProperty(value = "price")
    @JsonSerialize(using = DecimalPrecision8Serialize.class)
    BigDecimal price;
    @JsonProperty(value = "total_supply")
    @JsonSerialize(using = DecimalPrecision8Serialize.class)
    BigDecimal totalSupply;
    @JsonProperty(value = "marketcap")
    @JsonSerialize(using = DecimalPrecision8Serialize.class)
    BigDecimal marketcap;

}

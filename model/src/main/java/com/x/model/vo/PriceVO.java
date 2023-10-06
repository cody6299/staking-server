package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class PriceVO {
    
    @Data
    public static class Price {
        @JsonProperty(value = "id")
        Long id;
        @JsonProperty(value = "token")
        String token;
        @JsonProperty(value = "price")
        @JsonSerialize(using = DecimalPrecision8Serialize.class)
        BigDecimal price;
    }
    @JsonProperty(value = "prices")
    List<Price> prices = new ArrayList<>();

}

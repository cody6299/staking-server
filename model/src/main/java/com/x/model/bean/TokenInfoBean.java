package com.x.model.bean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import java.util.*;
import java.math.*;

import com.x.model.formatter.*;

@Data
public class TokenInfoBean {
    String address;
    String name;
    String symbol;
    Integer decimals;
    BigDecimal totalSupply;
}

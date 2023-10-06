package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class ERC20InfoParam {

    @JsonProperty(value = "id")
    Long id;
    @JsonProperty(value = "address")
    @JsonDeserialize(using = AddressDeserialize.class)
    String address;

}

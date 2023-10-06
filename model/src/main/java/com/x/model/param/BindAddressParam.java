package com.x.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.math.*;
import lombok.Data;

import com.x.model.formatter.*;

@Data
public class BindAddressParam {

    @JsonProperty(value = "chain_id")
    Long chainId;
    @JsonProperty(value = "chain_address")
    String chainAddress;
    @JsonProperty(value = "bind_address")
    String bindAddress;
    @JsonProperty(value = "signature")
    String signature;
}

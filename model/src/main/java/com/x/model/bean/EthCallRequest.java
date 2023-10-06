package com.x.model.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import java.util.*;

import com.x.model.formatter.*;

@Data
public class EthCallRequest {
    @JsonSerialize(using = AddressSerialize.class)
    String from;
    @JsonSerialize(using = AddressSerialize.class)
    String to;
    @JsonSerialize(using = AddressSerialize.class)
    String data;
}

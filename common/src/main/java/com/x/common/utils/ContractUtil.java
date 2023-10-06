package com.x.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class ContractUtil {
    
    public static String sortToken(String token0, String token1) {
        BigInteger token0D = new BigInteger(token0, 16);
        BigInteger token1D = new BigInteger(token1, 16);
        return token0D.compareTo(token1D) < 0 ? token0 : token1;
    }

    public static String addressDeserialize(String address) {
        if (address != null && (address.startsWith("0x") || address.startsWith("0X"))) {
            address = address.substring(2).toLowerCase();
        } else if (address != null) {
            address = address.toLowerCase();
        }
        return address;
    }

    public static String addressSerialize(String address) {
        if (address != null && !address.startsWith("0x") && !address.startsWith("0X")) {
            address = "0x" + address;
        }
        return address;
    }

}

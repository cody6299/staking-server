package com.x.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.io.*;

public class JsonUtil {
    
    public static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> String toJson(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            return null;
        } 
    }

    public static <T> T toObject(String json, Class<T> cls) {
        try {
            return mapper.readValue(json, cls);
        } catch (IOException ex) {
            return null;
        }
    }

    public static <K,V> Map<K,V> toObjectMap(String json, Class<K> keyCls, Class<V> valueCls) {
        try {
            return mapper.readValue(json, new TypeReference<Map<K,V>>(){});
        } catch (IOException ex) {
            return null;
        }
    }

    
    public static Map<String, Object> toMap(String json) {
        return toObject(json, Map.class);
    }

    public static <T> Map<String, Object> toObjectMap(T obj) {
        return toMap(toJson(obj));
    }

}

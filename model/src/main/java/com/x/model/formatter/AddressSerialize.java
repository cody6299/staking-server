package com.x.model.formatter;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.util.*;

public class AddressSerialize extends JsonSerializer<String> {

    @Override
    public void serialize(String address, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (!address.startsWith("0x")) {
            address = String.format("0x%s", address);
        }
        jsonGenerator.writeString(address);
    }
}

package com.x.model.formatter;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.util.Date;

public class AddressDeserialize extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        String strValue = jsonParser.getValueAsString();
        if (strValue == null) {
            return null;
        }
        if (strValue.startsWith("0x")) {
            strValue = strValue.substring(2);
        }
        return strValue.toLowerCase();
	}
}

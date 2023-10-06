package com.x.model.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.Date;
import java.math.BigDecimal;

public class DecimalPrecision8Serialize extends JsonSerializer<BigDecimal> {
    /**
     * 把Decimal类型转换为8位精度,不足八位则补零
     * @param BigDecimal
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public void serialize(BigDecimal amount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (amount != null) {
            amount = amount.setScale(8, BigDecimal.ROUND_DOWN);
            jsonGenerator.writeString(amount.toPlainString());
        }
    }
}

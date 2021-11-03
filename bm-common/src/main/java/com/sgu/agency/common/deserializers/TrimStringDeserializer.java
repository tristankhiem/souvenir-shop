package com.sgu.agency.common.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TrimStringDeserializer extends StdDeserializer<String> {

    public TrimStringDeserializer() {
        this(null);
    }

    public TrimStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String tempString = jp.getValueAsString().trim();
        tempString = tempString.replaceAll("\\s{2,}"," ");
        return tempString;
    }
}

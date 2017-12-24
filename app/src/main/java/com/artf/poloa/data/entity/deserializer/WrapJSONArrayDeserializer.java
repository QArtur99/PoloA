package com.artf.poloa.data.entity.deserializer;

import com.artf.poloa.data.entity.WrapJSONArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class WrapJSONArrayDeserializer implements JsonDeserializer<WrapJSONArray> {

    @Override
    public WrapJSONArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new WrapJSONArray(json.getAsJsonArray());

    }
}

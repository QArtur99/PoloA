package com.artf.poloa.data.entity.deserializer;

import com.artf.poloa.data.entity.WrapJSONObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by ART_F on 2017-12-20.
 */

public class WrapJSONObjectDeserializer implements JsonDeserializer<WrapJSONObject> {

    @Override
    public WrapJSONObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new WrapJSONObject(json.getAsJsonObject());

    }
}
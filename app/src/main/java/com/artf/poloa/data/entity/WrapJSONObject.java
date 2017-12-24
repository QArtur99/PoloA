package com.artf.poloa.data.entity;

import com.artf.poloa.data.entity.deserializer.WrapJSONObjectDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;


@JsonAdapter(WrapJSONObjectDeserializer.class)
public class WrapJSONObject {

    public JsonObject jsonObject;

    public WrapJSONObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

}

package com.artf.poloa.data.entity;

import com.artf.poloa.data.entity.deserializer.WrapJSONArrayDeserializer;
import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;


@JsonAdapter(WrapJSONArrayDeserializer.class)
public class WrapJSONArray {

    public JsonArray jsonArray;
    public int periodTime;
    public String ccName;


    public WrapJSONArray(JsonArray jsonArrayString) {
        jsonArray = jsonArrayString;
    }
}

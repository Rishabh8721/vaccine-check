package com.floplabs.vaccinecheck.json;

import android.util.Log;

import com.floplabs.vaccinecheck.model.CowinCenterData;
import com.floplabs.vaccinecheck.model.CowinData;
import com.floplabs.vaccinecheck.model.CowinDistrict;
import com.floplabs.vaccinecheck.model.CowinState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConverter {

    private static final String TAG = "json_converter";

    public CowinData getCowinData(String json){
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        CowinData cowinData =  gson.fromJson(json, CowinData.class);
        return cowinData;
    }

    public CowinCenterData getCowinCenterData(String json){
        Log.i(TAG, "getCowinCenterData: " + json);
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        return gson.fromJson(json, CowinCenterData.class);
    }

    public CowinState getStates(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, CowinState.class);
    }

    public CowinDistrict getDistricts(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, CowinDistrict.class);
    }
}

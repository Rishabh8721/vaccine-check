package com.floplabs.vaccinecheck.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class NotifierChannelTypeConverter {
    @TypeConverter
    public static HashMap<Integer, String> stringToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Integer, String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String mapToString(HashMap<Integer, String> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Integer, String>>() {}.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public static List<String> stringToListString(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String listStringToString(List<String> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public static List<Integer> stringToListInteger(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String listIntegerToString(List<Integer> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.toJson(list, type);
    }
}

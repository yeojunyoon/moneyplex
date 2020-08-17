package com.kakaopay.moneyplex.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    private static final Gson gson = new GsonBuilder().create();

    public static String toString(Object o){
        return gson.toJson(o);
    }
}

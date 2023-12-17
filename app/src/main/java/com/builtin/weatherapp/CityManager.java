package com.builtin.weatherapp;
import android.content.Context;
import android.content.SharedPreferences;

public class CityManager {

    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_CITY_NAME = "cityName";
    private static final String DEFAULT_CITY_NAME = "Yushan";

    public static String getCityName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CITY_NAME, DEFAULT_CITY_NAME);
    }

    public static void setCityName(Context context, String cityName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CITY_NAME, cityName);
        editor.apply();
    }
}

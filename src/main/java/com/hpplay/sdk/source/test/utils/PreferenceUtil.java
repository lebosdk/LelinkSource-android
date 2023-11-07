package com.hpplay.sdk.source.test.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hpplay.sdk.source.test.DemoApplication;

public class PreferenceUtil {
    private SharedPreferences mPref;
    private static PreferenceUtil mInstance;

    private PreferenceUtil() {
        Context context = DemoApplication.getApplication().getApplicationContext();
        String name = context.getPackageName() + "_demo";
        mPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public synchronized static PreferenceUtil getInstance() {
        if (mInstance == null) {
            mInstance = new PreferenceUtil();
        }
        return mInstance;
    }

    public boolean get(String key, boolean defValue) {
        return mPref.getBoolean(key, defValue);
    }

    public int get(String key, int defValue) {
        return mPref.getInt(key, defValue);
    }

    public float get(String key, float defValue) {
        return mPref.getFloat(key, defValue);
    }

    public long get(String key, long defValue) {
        return mPref.getLong(key, defValue);
    }

    public String get(String key) {
        return get(key, null);
    }

    public String get(String key, String defValue) {
        return mPref.getString(key, defValue);
    }

    public void put(String key, boolean value) {
        mPref.edit().putBoolean(key, value).apply();
    }

    public void put(String key, int value) {
        mPref.edit().putInt(key, value).apply();
    }

    public void put(String key, float value) {
        mPref.edit().putFloat(key, value).apply();
    }

    public void put(String key, long value) {
        mPref.edit().putLong(key, value).apply();
    }

    public void put(String key, String value) {
        mPref.edit().putString(key, value).apply();
    }

}

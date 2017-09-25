package com.xinhuamm.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SharedPreferencesUtils {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private int mMode = Context.MODE_PRIVATE;

    public SharedPreferencesUtils(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, mMode);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferencesUtils(Context context, String name, int mode) {
        this.mSharedPreferences = context.getSharedPreferences(name, mode);
        this.mEditor = mSharedPreferences.edit();
    }

    public void add(Map<String, String> map) {
        Set<String> set = map.keySet();
        for (String key : set) {
            mEditor.putString(key, map.get(key));
        }
        mEditor.commit();
    }

    public void add(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void addLong(String key,long value){
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public Long getLong(String key,Long defaultVaule){
        if (mSharedPreferences != null) {
            return mSharedPreferences.getLong(key, defaultVaule);
        }
        return 0l;
    }

    public void addInt(String key,int value){
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public int getInt(String key,int defaultVaule){
        if (mSharedPreferences != null) {
            return mSharedPreferences.getInt(key, defaultVaule);
        }
        return 0;
    }



    public void addBoolean(String key,boolean value){
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }
    public boolean getBoolean(String key,boolean defaultVaule){
        if (mSharedPreferences != null) {
            return mSharedPreferences.getBoolean(key, defaultVaule);
        }
        return false;
    }

    public void deleteAll() throws Exception {
        mEditor.clear();
        mEditor.commit();
    }

    public void delete(String key){
        mEditor.remove(key);
        mEditor.commit();
    }

    public String get(String key){
        if (mSharedPreferences != null) {
            return mSharedPreferences.getString(key, "");
        }
        return "";
    }

//    public SharedPreferences.Editor getEditor() {
//        return mEditor;
//    }

}

package com.jacky.support.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author:Hzj
 * @date :2020/6/3
 * desc  ：Sharedpreference Utils
 * record：
 */
public class SpUtils {

    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor sEditor;
    private static final String DEFAULT_SP_NAME = "sp_config";

    public interface SpKey {
        String KEY_TOKEN = "token";
        String KEY_USER_INFO = "user_info";
    }

    public static void init(Context context) {
        init(context, DEFAULT_SP_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context, String name) {
        init(context, name, Context.MODE_PRIVATE);
    }

    public static void init(Context context, String name, int mode) {
        if (context == null) {
            throw new IllegalStateException("you must call SpUtils.init() first!!!");
        }
        sSharedPreferences = context.getSharedPreferences(name, mode);
        sEditor = sSharedPreferences.edit();
    }

    public static void addString(String key, String value) {
        sEditor.putString(key, value).apply();
    }

    public static void addLong(String key, long value) {
        sEditor.putLong(key, value).apply();
    }

    public static void addInt(String key, int value) {
        sEditor.putInt(key, value).apply();
    }

    public static void addBoolean(String key, boolean value) {
        sEditor.putBoolean(key, value).apply();
    }

    public static void addObject(String key, Serializable obj) {
        sEditor.putString(key, serialize(obj)).apply();
    }

    public static <T> T getObject(String key) {
        if (sSharedPreferences != null) {
            return deSerialization(getString(key));
        }
        return null;
    }

    public static Long getLong(String key, Long defaultValue) {
        if (sSharedPreferences != null) {
            return sSharedPreferences.getLong(key, defaultValue);
        }
        return 0L;
    }

    public static int getInt(String key, int defaultVaule) {
        if (sSharedPreferences != null) {
            return sSharedPreferences.getInt(key, defaultVaule);
        }
        return 0;
    }

    public static boolean getBoolean(String key, boolean defaultVaule) {
        if (sSharedPreferences != null) {
            return sSharedPreferences.getBoolean(key, defaultVaule);
        }
        return false;
    }

    public static String getString(String key) {
        if (sSharedPreferences != null) {
            return sSharedPreferences.getString(key, "");
        }
        return "";
    }


    /**
     * 序列化对象
     *
     * @param obj 必须实现Serializable
     */
    private static String serialize(Serializable obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
            return serStr;
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 反序列化对象
     */
    private static <A> A deSerialization(String str) {
        try {
            String redStr = java.net.URLDecoder.decode(str, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes(StandardCharsets.ISO_8859_1));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object readObject = objectInputStream.readObject();
            if (readObject != null) {
                A obj = (A) readObject;
                objectInputStream.close();
                byteArrayInputStream.close();
                return obj;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void deleteAll() {
        sEditor.clear().apply();
    }

    public static void delete(String key) {
        sEditor.remove(key).apply();
    }

    public static boolean contains(String key) {
        return sSharedPreferences.contains(key);
    }
}

package com.zzti.lsy.ninetingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zzti.lsy.ninetingapp.App;


/**
 * @author lsy
 * sp工具类
 */
public class SpUtils {
    private static final String NAME = "nineTing";
    public static final String USERNAME = "userName";//用户名
    public static final String LOGINSTATE = "loginState";//登录状态
    /**
     * 操作员类型  1生产管理员  2配件管理员 3设备管理员 4项目经理 5总经理 6机械师
     */
    public static final String OPTYPE = "opType";

    private static SpUtils instance = new SpUtils();
    private static SharedPreferences sp;//单例

    private SpUtils() {
    }

    public static SpUtils getInstance() {
        if (sp == null) {
            sp = App.get().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

    /**
     * 定义统一的保存数据的方法
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        }
        edit.commit();
    }

//    /**
//     * 保存字符串数据
//     *
//     * @param key
//     * @param value
//     */
//    public void putString(String key, String value) {
//        sp.edit().putString(key, value).commit();
//    }

    /**
     * 保存int数据
     */
//    public void putInt(String key, int value) {
//        sp.edit().putInt(key, value).commit();
//    }

    /**
     * 保存booLean数据
     */
//    public void putBoolean(String key, boolean value) {
//        sp.edit().putBoolean(key, value).commit();
//    }

    /**
     * 根据key读取对应的String数据
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 根据key读取对应的int数据
     */
    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    /**
     * 根据key读取对应的boolean数据
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 移除指定key所对应的数据
     */
    public void remove(String key) {
        sp.edit().remove(key).commit();
    }

    /**
     * 移除所有的数据
     */
    public void clear() {
        sp.edit().clear().commit();
    }

}

package com.zzti.lsy.ninetingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @autor：on lsy on 2017/9/14 10:45
 * @Email：lushuaiyuan@itsports.club
 * @describe：更新sharedpreference的工具类
 */
public class MigrationManager {
    private final static String KEY_PREFERENCES_VERSION = "key_preferences_version";
    private final static int PREFERENCES_VERSION = 11;

    public static void migrate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("nineTing", Context.MODE_PRIVATE);
        checkPreferences(preferences);
    }

    private static void checkPreferences(SharedPreferences thePreferences) {
        final double oldVersion = thePreferences.getInt(KEY_PREFERENCES_VERSION, 1);
        if (oldVersion < PREFERENCES_VERSION) {
            final SharedPreferences.Editor edit = thePreferences.edit();
            edit.clear();
            edit.putInt(KEY_PREFERENCES_VERSION, PREFERENCES_VERSION);
            edit.commit();
        }
    }
}

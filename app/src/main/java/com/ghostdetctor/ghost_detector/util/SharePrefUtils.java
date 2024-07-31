package com.ghostdetctor.ghost_detector.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePrefUtils {
    public static String email = "abcd@gmail.com";
    public static String subject = "Ghost Detector";
    private static SharedPreferences mSharePref;

    public static void init(Context context) {
        if (mSharePref == null) {
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(String key, String value) {
        return mSharePref.getString(key, value);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getInt(String key, int value) {
        return mSharePref.getInt(key, value);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static long getLong(String key, long value) {
        return mSharePref.getLong(key, value);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = mSharePref;
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, value);
        }
        return true;
    }

    public static void increaseCountFirstHelp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("dataAudio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("first", pre.getInt("first", 0) + 1);
        editor.apply();
    }

    public static int getCountOpenFirstHelp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("dataAudio", Context.MODE_PRIVATE);
        return pre.getInt("first", 0);
    }

    public static boolean isRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getBoolean("rated", false);
    }

    public static int getCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getInt("counts", 1);
    }

    public static void increaseCountOpenApp(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("counts", pre.getInt("counts", 0) + 1);
        editor.commit();
    }
    public static int getCountOpenChallenge(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getInt("counts_challenge", 1);
    }

    public static void increaseCountOpenChallenge(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("counts_challenge", pre.getInt("counts_challenge", 0) + 1);
        editor.commit();
    }
    public static int getCountOpenGhost(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getInt("counts_ghost", 1);
    }

    public static void increaseCountOpenGhost(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt("counts_ghost", pre.getInt("counts_ghost", 0) + 1);
        editor.commit();
    }

    public static void forceRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("rated", true);
        editor.commit();
    }
}

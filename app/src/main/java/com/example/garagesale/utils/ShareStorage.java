package com.example.garagesale.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareStorage {

    private static SharedPreferences getInstance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static void setUserId(Context context, String userId) {
        getInstance(context).edit().putString("userId", userId).commit();
    }

    public static String getUserId(Context context) {
        return getInstance(context).getString("userId", "");
    }

    public static void setUserName(Context context, String userName) {
        getInstance(context).edit().putString("username", userName).commit();
    }

    public static String getUsername(Context context) {
        return getInstance(context).getString("username", "");
    }
    public static void clean(Context context) {
         getInstance(context).edit().clear().commit();
    }


    public static void setEmail(Context context, String email) {
        getInstance(context).edit().putString("email", email).commit();
    }

    public static String getEmail(Context context) {
        return getInstance(context).getString("email", "");
    }

    public static void setLanguage(Context context, String language) {
        getInstance(context).edit().putString("lang", language).commit();
    }

    public static String getLanguage(Context context) {
        return getInstance(context).getString("lang", "");
    }
}

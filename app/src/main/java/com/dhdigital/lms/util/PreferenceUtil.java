package com.dhdigital.lms.util;

import android.content.Context;
import android.content.SharedPreferences;


import com.kelltontech.volley.persistence.SharedPrefsUtils;

import java.util.Map;

/**
 * Created by Kiran Bedre on 27/11/15.
 * DarkHorse BOA
 */
public class PreferenceUtil extends SharedPrefsUtils {
    private static final String HEADER_PREF = "pref_header";
    private static final String COMMON_PREF = "pref_commo";

    private static final String LAST_MASTER_REFRESH_TIMESTAMP = "last_master_refresh_timestamp";

    private static final String Set_Cookie = "Set-Cookie";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String Cookie = "Cookie";
    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final String GCM_DEVICE_TOKEN = "GCM_DEVICE_TOKEN";


    public static void setLastRefreshTimeForMasterData(Context pContext, long now) {
        SharedPreferences.Editor _editor = pContext.getSharedPreferences(COMMON_PREF, Context.MODE_PRIVATE).edit();
        _editor.putLong(LAST_MASTER_REFRESH_TIMESTAMP, now);
        _editor.apply();
    }

    public static long getLastRefreshTimeForMasterData(Context pContext) {
        SharedPreferences pref = pContext.getSharedPreferences(COMMON_PREF, Context.MODE_PRIVATE);
        return pref.getLong(LAST_MASTER_REFRESH_TIMESTAMP, -1);
    }


    public static void saveCookies(Context pContext, Map<String, String> headers) {
        SharedPreferences.Editor _editor = pContext.getSharedPreferences(HEADER_PREF, Context.MODE_PRIVATE).edit();
        for (Map.Entry entry : headers.entrySet()) {
            _editor.putString(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        _editor.apply();
    }

    public static Map getCookie(Context pContext) {
        SharedPreferences pref = pContext.getSharedPreferences(HEADER_PREF, Context.MODE_PRIVATE);
        return pref.getAll();
    }

    public static void clearCookies(Context pContext) {
        pContext.getSharedPreferences(HEADER_PREF, Context.MODE_PRIVATE).edit().clear().commit();
    }
    
    public static void setDeviceToken(Context pContext, String token) {
        SharedPrefsUtils.setSharedPrefString(pContext, COMMON_PREF, GCM_DEVICE_TOKEN, token);
    }

    public static String getDeviceToken(Context pContext) {
        return SharedPrefsUtils.getSharedPrefString(pContext, COMMON_PREF, GCM_DEVICE_TOKEN, null);
    }
}

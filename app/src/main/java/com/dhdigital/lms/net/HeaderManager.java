package com.dhdigital.lms.net;

import android.content.Context;
import android.util.Log;


import com.dhdigital.lms.util.AppUtil;
import com.dhdigital.lms.util.PreferenceUtil;
import com.kelltontech.volley.utils.DeviceInfoUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kiran Bedre on 6/11/15.
 * DarkHorse BOA
 */
public class HeaderManager {
    private static final String HEADER_AUTHORIZATION = "authorization";
    private static final String HEADER_DEVICE_TYPE = "device_type";
    private static final String HEADER_DEVICE_ID = "device_id";

    private static final String Set_Cookie = "Set-Cookie";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String Cookie = "Cookie";
    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";
    private static Map<String, String> HEAD_AUTH = new HashMap<String, String>();


    /**
     * Creating LoginHeaders as combination of Username and password concatenated using ':'
     * @param context
     * @param username
     * @param password
     * @return map of Loginheaders
     */
    public static final Map<String, String> prepareLoginHeaders(Context context, String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(HEADER_AUTHORIZATION, "Basic " + AppUtil.convertBase64String(username + ":" + password));
        HEAD_AUTH.put(HEADER_AUTHORIZATION, "Basic " + AppUtil.convertBase64String(username + ":" + password));
        putDeviceInfo(context, map);
        return map;
    }






    /**
     *
     * @param context
     * @return
     */
    public static final Map<String, String> prepareServicePolicyCheckHeader(Context context) {
        Map<String, String> toReturn = new HashMap<String, String>();
        putDeviceInfo(context, toReturn);
        putCookie(context, toReturn);
        return toReturn;
    }

    public static final Map<String, String> prepareMasterDataHeaders(Context context) {
        Map<String, String> toReturn = new HashMap<String, String>();
        putDeviceInfo(context, toReturn);
        putCookie(context, toReturn);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------------------------

    public static final void saveCookies(Context context, Map<String, String> headers) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        for (Map.Entry entry : headers.entrySet()) {
            if (Set_Cookie.equals(String.valueOf(entry.getKey()))) {
                String[] split = entry.getValue().toString().split(";");
                for (String item : split) {
                    if (item.contains(XSRF_TOKEN)) {
                        cookieMap.put(XSRF_TOKEN, item.split("=")[1]);
                    }
                }
            }
            if (JSESSIONID.equals(String.valueOf(entry.getKey()))) {
                cookieMap.put(JSESSIONID, String.valueOf(entry.getValue()).trim());
            }
        }

        for (Map.Entry entry : HEAD_AUTH.entrySet()) {
            cookieMap.put(HEADER_AUTHORIZATION,String.valueOf(entry.getValue()));
        }

        PreferenceUtil.saveCookies(context, cookieMap);
    }

    private static final Map<String, String> putCookie(Context context, Map<String, String> toReturn) {
        Map cookieMap = PreferenceUtil.getCookie(context);
        StringBuilder builder = new StringBuilder();
        builder.append(JSESSIONID).append("=").append(cookieMap.get(JSESSIONID)).append(';');
        toReturn.put(Cookie, builder.toString());
        return toReturn;
    }

    private static final void putDeviceInfo(Context context, Map<String, String> headers) {
        headers.put(HEADER_DEVICE_TYPE, "android");
        String uniqueId = DeviceInfoUtils.getUniqueId(context);
        headers.put(HEADER_DEVICE_ID, PreferenceUtil.getDeviceToken(context));
        Log.d("TAG","Device Token : "+PreferenceUtil.getDeviceToken(context));
    }

    public static final boolean isCookieAvailable(Context context) {
        Map cookieMap = PreferenceUtil.getCookie(context);
        if(cookieMap==null || cookieMap.size()==0) return false;
        return true;
    }

}

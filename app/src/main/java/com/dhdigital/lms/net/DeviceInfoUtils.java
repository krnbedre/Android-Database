/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.dhdigital.lms.net;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.kelltontech.volley.utils.Installation;
import com.kelltontech.volley.utils.StringUtils;


/**
 * class for device info specific methods
 *
 * @author sachin.gupta
 */
public class DeviceInfoUtils {

    /**
     * Used to retreive the IMEI device id
     *
     * @param pContext
     * @return device IMEI id
     */
    public static String getUniqueId(Context pContext) {
        String deviceImei = null;
        if (pContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            TelephonyManager telephonyManager = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
            deviceImei = telephonyManager.getDeviceId();
        }
        if (StringUtils.isNullOrEmpty(deviceImei)) {
            return Installation.id(pContext);
        } else {
            return deviceImei;
        }
    }

    /**
     * API used to retreive the Device model
     *
     * @return
     */
    public static String getModelDesc() {
        String modelDesc = Build.MANUFACTURER;
        modelDesc += " " + Build.MODEL;
        return modelDesc;
    }
}

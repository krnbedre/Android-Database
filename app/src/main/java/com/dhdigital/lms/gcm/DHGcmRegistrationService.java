package com.dhdigital.lms.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.dhdigital.lms.R;
import com.dhdigital.lms.util.PreferenceUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


/**
 * Created by Shiv on 29/3/16.
 */
public class DHGcmRegistrationService extends IntentService {
    private static final String TAG = DHGcmRegistrationService.class.getSimpleName();

    public DHGcmRegistrationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            // Initially this call goes out to the network to retrieve the token, subsequent calls are local.
            InstanceID instanceID = InstanceID.getInstance(this);

            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

            PreferenceUtil.setDeviceToken(this, token);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }

    }


}

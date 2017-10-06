package com.dhdigital.lms.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Shiv on 29/3/16.
 */
public class DHInstanceIDListenerService extends InstanceIDListenerService {
    private final String TAG = "DarkHorseInstanceIDServiceListener";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, DHGcmRegistrationService.class);
        startService(intent);
    }
}

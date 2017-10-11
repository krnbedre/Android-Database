package com.dhdigital.lms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.dhdigital.lms.R;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.util.AppConstants;
import com.kelltontech.volley.ui.activity.BaseActivity;

/**
 * Created by Darkhorse on 9/27/2017.
 */

public class SplashScreenActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen_layout);

        switchUserNavigation();
    }

    private void switchUserNavigation() {

        final Intent loginScreen  = new Intent(this,LoginActivity.class);
        if (isUserLoggedIn()) {
            loginScreen.putExtra("NAVIGATION","LOGGED_IN");
        } else {
            loginScreen.putExtra("NAVIGATION","LOGGED_OUT");
        }

        Handler timer = new Handler();
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(loginScreen);
                finish();
            }
        }, AppConstants.TIMEOUT_SPLASH_SCREEN);





    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }


    /**
     * API to get the status of User logged status cookie
     * @return
     */
    private boolean isUserLoggedIn() {
        if (HeaderManager.isCookieAvailable(this)) {
            return true;
        }
        return false;
    }


}

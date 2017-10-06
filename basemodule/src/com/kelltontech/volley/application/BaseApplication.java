/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.kelltontech.volley.application;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.kelltontech.BuildConfig;
import com.kelltontech.volley.database.GenericDB;


import java.util.Timer;
import java.util.TimerTask;

/**
 * This class holds some application-global instances.
 */
public abstract class BaseApplication extends Application {
    private static final String LOG_TAG = "BaseApplication";
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;
    public boolean mAppInBackground;
    private GenericDB mGenericDB;
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initialize();
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "onCreate()");
        }
    }

    /**
     * @return the mIsAppInBackground
     */
    public boolean isAppInBackground() {
        return mAppInBackground;
    }

    /**
     * @param isAppInBackground value to set for mIsAppInBackground
     */
    public void setAppInBackground(boolean isAppInBackground) {
        mAppInBackground = isAppInBackground;
        onAppStateSwitched(isAppInBackground);
    }

    /**
     * @param isAppInBackground
     * @note subclass can override this method for this callback
     */
    protected void onAppStateSwitched(boolean isAppInBackground) {
        // nothing to do here in base application class
    }

    /**
     * should be called from onResume of each activity of application
     */
    public void onActivityResumed() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }
        setAppInBackground(false);
    }

    /**
     * should be called from onPause of each activity of app
     */
    public void onActivityPaused() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                setAppInBackground(true);
                if (BuildConfig.DEBUG) {
                    Log.i(LOG_TAG, "None of our activity is in foreground.");
                }
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask, MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    /**
     * Get the BaseDbHelper instance.
     *
     * @return mGenericDB
     */
    public GenericDB getGenericDB() {
        String dbName = null;
        int dbVersion = -1;
        try {
            ApplicationInfo ai = getApplicationMetaData();
            Bundle bundle = ai.metaData;
            dbName = bundle.getString("DB_NAME");
            dbVersion = bundle.getInt("DB_VERSION");
        } catch (NameNotFoundException e) {
            Log.e(LOG_TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        if (dbName == null || dbVersion <= 0) {
            throw new IllegalStateException("DB name or version is not configured. Please check <meta-data> in manifest file");
        }
        Log.d(LOG_TAG, "DB Name: " + dbName + " DB Version: " + dbVersion);
        if (mGenericDB == null) {
            mGenericDB = new GenericDB(this, dbName, null, dbVersion);
        }
        return mGenericDB;
    }

    public ApplicationInfo getApplicationMetaData()
            throws NameNotFoundException {
        ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
        return ai;
    }

    protected abstract void initialize();
}
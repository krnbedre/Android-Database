/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.kelltontech.volley.ui.activity;

import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.kelltontech.BuildConfig;
import com.kelltontech.volley.application.BaseApplication;
import com.kelltontech.volley.ui.IScreen;
import com.kelltontech.volley.utils.KeypadUtils;


/**
 * This class is used as base-class for application-base-activity.
 */
public abstract class BaseActivity extends AppCompatActivity implements IScreen {
    private String LOG_TAG = getClass().getSimpleName();
    /**
     * Subclass should over-ride this method to update the UI with response,
     * this base class promises to call this method from UI thread.
     *
     * @param serviceResponse
     */

    // ////////////////////////////// show and hide ProgressDialog

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "onResume()");
        }

        Application application = this.getApplication();
        if (application instanceof BaseApplication) {
            BaseApplication baseApplication = (BaseApplication) application;
            if (baseApplication.isAppInBackground()) {
                onAppResumeFromBackground();
            }
            baseApplication.onActivityResumed();
        }
    }

    /**
     * This callback will be called after onResume if application is being
     * resumed from background. <br/>
     * <p/>
     * Subclasses can override this method to get this callback.
     */
    protected void onAppResumeFromBackground() {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "onAppResumeFromBackground()");
        }
    }

    /**
     * This method should be called to force app assume itself not in
     * background.
     */
    public final void setAppNotInBackground() {
        Application application = this.getApplication();
        if (application instanceof BaseApplication) {
            BaseApplication baseApplication = (BaseApplication) application;
            baseApplication.setAppInBackground(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "onPause()");
        }

        Application application = this.getApplication();
        if (application instanceof BaseApplication) {
            BaseApplication baseApplication = (BaseApplication) application;
            baseApplication.onActivityPaused();
        }
    }

//	/**
//	 * @param serviceResponse
//	 */
//	@Override
//	public final void handleUiUpdate(final boolean status,final int action, final Object serviceResponse){
//		if (isFinishing()) {
//			return;
//		}
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				updateUiDelegate(status,action,serviceResponse);
//			}
//		});
//	}
//
//	/**
//	 * Users of this method should call this method only from UI thread.
//	 */
//	public final void updateUiDelegate(final boolean status,final int action, final Object serviceResponse) {
//		if (BuildConfig.DEBUG) {
//			updateUi(status,action,serviceResponse);
//		} else {
//			try {
//				updateUi(status,action,serviceResponse);
//			} catch (Exception e) {
//				Log.e(LOG_TAG, "updateUi()", e);
//			}
//		}
//	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "onNewIntent()");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows a simple native progress dialog<br/>
     * Subclass can override below two methods for custom dialogs- <br/>
     * 1. showProgressDialog <br/>
     * 2. removeProgressDialog
     *
     * @param bodyText
     */
    public void showProgressDialog(String bodyText) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BaseActivity.this);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true; //
                    }
                    return false;
                }
            });
        }

        mProgressDialog.setMessage(bodyText);

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Removes the simple native progress dialog shown via showProgressDialog <br/>
     * Subclass can override below two methods for custom dialogs- <br/>
     * 1. showProgressDialog <br/>
     * 2. removeProgressDialog
     */
    public void removeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void setProgressDialog(ProgressDialog dialog) {
        this.mProgressDialog = dialog;
    }
    // ////////////////////////////// show and hide key-board

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
       View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                KeypadUtils.hideSoftKeypad(this);
            }
        }
        return ret;
    }

}
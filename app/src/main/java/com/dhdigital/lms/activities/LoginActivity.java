package com.dhdigital.lms.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.dhdigital.lms.R;
import com.dhdigital.lms.db.MasterDataTable;
import com.dhdigital.lms.gcm.MyFirebaseMessagingService;
import com.dhdigital.lms.gcm.model.NotificationAction;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.Users;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.AppUtil;
import com.dhdigital.lms.util.PreferenceUtil;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;

import java.lang.reflect.Type;
import java.util.Map;

import static com.dhdigital.lms.util.AppConstants.EXTRA_NOTIFICATION_ACTION;
import static com.dhdigital.lms.util.AppConstants.LOGGED_IN;
import static com.dhdigital.lms.util.AppConstants.LOGGED_OUT;
import static com.dhdigital.lms.util.AppConstants.NAVIGATION;
import static com.dhdigital.lms.util.AppConstants.NOTIFICATION;

/**
* This class is used for displaying user with Login page
 * */
public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext = LoginActivity.this;
    private EditText mEditTextPassword, mEditTextUsername,mEditURL;
    private ImageView mMainLogo;
    private LinearLayout mParentLayout;
    private LinearLayout mContainer;
    private TextInputLayout mUserNameWrapper,mPasswordWrapper;
    private Button mButtonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWidgets();
        registerGCMServices();
        switchNavigation();

    }

    private void switchNavigation() {

        String loggedInStatus = getIntent().getStringExtra(NAVIGATION);
        if (null != loggedInStatus) {
            switch (loggedInStatus) {
                case LOGGED_IN:
                    executeLoginFromCookie();
                    break;
                case LOGGED_OUT:
                    break;
                case NOTIFICATION:
                    if (isUserLoggedIn()) {
                        NotificationAction notificationAction = getIntent().getParcelableExtra(EXTRA_NOTIFICATION_ACTION);
                        Intent leaveDetails = new Intent(this, LeaveDetailsActivity.class);
                        leaveDetails.putExtra(NAVIGATION, NOTIFICATION);
                        leaveDetails.putExtra(EXTRA_NOTIFICATION_ACTION, notificationAction);
                        startActivity(leaveDetails);
                    }
                    break;
            }
        }
    }

    /**
     * Initialize widgets items and timings
     */
    private void initWidgets(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_page);
        mEditTextPassword = ((EditText) findViewById(R.id.editText_password));
        mEditTextUsername = ((EditText) findViewById(R.id.editText_username));
        mMainLogo = (ImageView) findViewById(R.id.splash_logo) ;
        mParentLayout = (LinearLayout) findViewById(R.id.parent_layout);
        mContainer = (LinearLayout) findViewById(R.id.container_layout) ;
        mButtonLogin = (Button) findViewById(R.id.change_pwd_button);
        mUserNameWrapper = (TextInputLayout) findViewById(R.id.input_username);
        mPasswordWrapper = (TextInputLayout) findViewById(R.id.input_password_text);
        mEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mButtonLogin.setOnClickListener(this);
        mEditTextUsername.setOnFocusChangeListener(this);
        mEditTextPassword.setOnFocusChangeListener(this);
        mContainer = (LinearLayout) findViewById(R.id.container);

        mEditTextPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG,"onKeyEvent");
                    mButtonLogin.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void registerGCMServices() {
        Intent gcmRegistrationIntent = new Intent(mContext, MyFirebaseMessagingService.class);
        startService(gcmRegistrationIntent);
    }


    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        removeProgressDialog();
        Log.i("TAG-1", "@UMESH updateUi, serviceResponse: " + serviceResponse);
        switch (actionID) {
            case NetworkEvents.VALIDATE_USER:
                if (status && serviceResponse instanceof Users) {
                    Users loggedInUser = (Users) serviceResponse;
                    loggedInUser.getEmployee();
                    Log.d("DATA","details ::"+loggedInUser.getUsername());
                    GlobalData.gLoggedInUser = loggedInUser;
                    MasterDataTable.getInstance(mContext).insertUserInfo(loggedInUser.getUsername(),loggedInUser.getTenant().getTenantName(),0);
                    Intent intent = new Intent(mContext, LandingPageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (serviceResponse == null || AppConstants.ERROR_MSG_UNAUTHORIZED.equals(serviceResponse.toString())) {
                        AppUtil.showSnackBar(findViewById(R.id.change_pwd_button), getString(R.string.login_error_invalid_username_or_password), Color.parseColor("#A52A2A"));
                        mEditTextPassword.getText().clear();
                        mEditTextUsername.getText().clear();
                        mContainer.setVisibility(View.VISIBLE);

                    } else {
                        AppUtil.showSnackBar(findViewById(R.id.change_pwd_button), serviceResponse.toString(), Color.parseColor("#A52A2A"));
                        mContainer.setVisibility(View.VISIBLE);

                    }
                }
                break;

        }
    }

    @Override
    public void onEvent(int eventId, Object eventData) {
    }

    @Override
    public void getData(int actionID) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_pwd_button:
                onLoginBtnClicked(view);
                break;
        }
    }


    private void onLoginBtnClicked(View view) {

        final String username = mEditTextUsername.getText().toString().trim();
        final String password = mEditTextPassword.getText().toString().trim();

        if (!validateLogin()) {
            return;
        }
        if (!AppUtil.isNetworkConnected(mContext)) {
            AppUtil.showSnackBar(view, AppConstants.ERROR_MSG_N0_CONNECTION, getResources().getColor(R.color.error_text_color));
            return;
        }
        executeLoginAPI(view, username, password);
    }

    private boolean validateLogin() {
        return validateUserName() && validatePassword();
    }

    private boolean validateUserName(){
        String username = mEditTextUsername.getText().toString();
        boolean toReturn = true;
        if (username.length() == 0) {
            mUserNameWrapper.setErrorEnabled(true);
            mUserNameWrapper.setError(getString(R.string.login_error_enter_username));
            mEditTextUsername.requestFocus();

            toReturn = false;
        } else if (!AppUtil.isValidEmail(username)) {
            mUserNameWrapper.setErrorEnabled(true);
            mUserNameWrapper.setError(getString(R.string.login_error_enter_valid_username));
            mEditTextUsername.requestFocus();

            toReturn = false;
        }
        else{
            mUserNameWrapper.setErrorEnabled(false);
        }
        return toReturn;
    }

    private boolean validatePassword(){
        String password = mEditTextPassword.getText().toString();
        boolean toReturn = true;
        if (password.length() == 0) {
            mPasswordWrapper.setErrorEnabled(true);
            mPasswordWrapper.setError(getString(R.string.login_error_enter_password));

            toReturn = false;
        } else if (password.length() < 3) {
            mPasswordWrapper.setErrorEnabled(true);
            mPasswordWrapper.setError(getString(R.string.login_error_enter_valid_password));

            toReturn = false;
        }
        else{
            mPasswordWrapper.setErrorEnabled(false);
        }
        return toReturn;
    }




    /**
     * API used to provide Login Functionality
     * @param view
     * @param username
     * @param password
     */
    private void executeLoginAPI(final View view, final String username, final String password) {
        showProgressDialog(AppConstants.PROGRESS_DIALOG_LOGGING_IN);
        Log.d(TAG, "@UMESH executeLoginAPI...");
        Type userType = new TypeToken<Users>() {
        }.getType();

        RequestManager.addRequest(new GsonObjectRequest<Users>(APIUrls.LOGIN_URL, null, userType, new VolleyErrorListener(LoginActivity.this, LoginActivity.this, NetworkEvents.VALIDATE_USER)) {

            @Override
            public void deliverResponse(Users response, Map<String, String> responseHeaders) {
                HeaderManager.saveCookies(LoginActivity.this, responseHeaders);
                updateUi(true, NetworkEvents.VALIDATE_USER, response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return HeaderManager.prepareLoginHeaders(mContext, username, password);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
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







    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.editText_username:
                if (b) {
                    Log.d("TAG","on Foucs Change of username"+b);
                    if(!validateUserName())
                        mUserNameWrapper.setErrorEnabled(false);
                }
                break;
            case R.id.editText_password:
                if (b) mPasswordWrapper.setErrorEnabled(false);
                break;
        }
    }




    /**
     * API used to provide Login Functionality from Cookies
     *
     */
    private void executeLoginFromCookie() {
        showProgressDialog(AppConstants.PROGRESS_DIALOG_LOGGING_IN);
        Log.d(TAG, "@UMESH executeLoginAPI...");
        Type userType = new TypeToken<Users>() {
        }.getType();

        mContainer.setVisibility(View.GONE);

        RequestManager.addRequest(new GsonObjectRequest<Users>(APIUrls.LOGIN_URL, PreferenceUtil.getCookie(this), null, userType, new VolleyErrorListener(LoginActivity.this, LoginActivity.this, NetworkEvents.VALIDATE_USER)) {

            @Override
            public void deliverResponse(Users response, Map<String, String> responseHeaders) {
                HeaderManager.saveCookies(LoginActivity.this, responseHeaders);
                updateUi(true, NetworkEvents.VALIDATE_USER, response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return PreferenceUtil.getCookie(LoginActivity.this);
            }


        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }


}

package com.dhdigital.lms.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.ChangePassword;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.AppUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by admin on 16/10/17.
 */

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {


    private TextInputLayout mCurrentPwdLayout, mConfirmPwdLayout;
    private EditText mCurrentPwdText, mNewPwdText, mConfirmPwdText;
    private Button mChangePwdButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd_layout);
        initLayout();
        initToolBar();
    }


    protected void initLayout() {

        mCurrentPwdLayout = (TextInputLayout) findViewById(R.id.input_old_password);
        mConfirmPwdLayout = (TextInputLayout) findViewById(R.id.input_new_password_again);
        mChangePwdButton = (Button) findViewById(R.id.change_pwd_button);
        mChangePwdButton.setOnClickListener(this);
        mCurrentPwdText = (EditText) findViewById(R.id.editText_old_password);
        mCurrentPwdText.addTextChangedListener(new PasswordTextWatcher(mCurrentPwdText));
        mNewPwdText = (EditText) findViewById(R.id.editText_new_password);
        mNewPwdText.addTextChangedListener(new PasswordTextWatcher(mNewPwdText));
        mConfirmPwdText = (EditText) findViewById(R.id.editText_new_password_again);
        mConfirmPwdText.addTextChangedListener(new PasswordTextWatcher(mConfirmPwdText));

    }


    private void initToolBar() {

        toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(getString(R.string.change_pwd));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    private boolean validateConfirmPassword() {
        if (mNewPwdText.getText().toString().equals(mConfirmPwdText.getText().toString())) {
            mConfirmPwdLayout.setErrorEnabled(false);
            return true;
        } else {
            mConfirmPwdLayout.setError(getString(R.string.error_msg_pwd_not_match));
            return false;
        }
    }

    private boolean validateChangePassword() {
        if (mCurrentPwdText.getText().toString().equals(mConfirmPwdText.getText().toString())) {
            mCurrentPwdLayout.setError(getString(R.string.error_password_same));
            return false;
        } else {
            mCurrentPwdLayout.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        if (status && serviceResponse != null) {
            switch (actionID) {
                case NetworkEvents.CHANGE_PWD:
                    AppUtil.showSnackBar(mChangePwdButton, getString(R.string.succes_pwd_changed), Color.parseColor("#3CB371"));
                    finish();
                    break;
            }
        } else {
            AppUtil.showSnackBar(mChangePwdButton, getString(R.string.error_msg_pwd_incorrect), Color.parseColor("#A52A2A"));
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
                if (validateChangePassword() && validateConfirmPassword()) {
                    ChangePassword password = new ChangePassword();
                    password.setOldpass(mCurrentPwdText.getText().toString());
                    password.setNewpass(mConfirmPwdText.getText().toString());
                    changeAccountPassword(NetworkEvents.CHANGE_PWD, password);

                }
                break;
        }
    }


    /**
     * API used to change Account Password-
     *
     * @param event
     * @param password
     */

    public void changeAccountPassword(final int event, ChangePassword password) {
        Type listType = new TypeToken<Boolean>() {
        }.getType();
        String url = APIUrls.CHANGE_PASSWORD;
        String requestPayload = new Gson().toJson(password);
        RequestManager.addRequest(new GsonObjectRequest<Boolean>(url, HeaderManager.prepareMasterDataHeaders(this), requestPayload, listType, new VolleyErrorListener(this, this, event)) {
            @Override
            public void deliverResponse(Boolean response, Map<String, String> responseHeaders) {
                updateUi(true, NetworkEvents.CHANGE_PWD, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }


    private class PasswordTextWatcher implements TextWatcher {

        private View view;

        private PasswordTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_new_password_again:
                    validateConfirmPassword();
                    break;
                case R.id.editText_new_password:
                    validateChangePassword();
                    break;
                case R.id.editText_old_password:
                    validateChangePassword();
                    break;

            }
        }
    }
}

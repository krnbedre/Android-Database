package com.dhdigital.lms.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.Leave;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.AppUtil;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;
import com.kelltontech.volley.utils.DateTimeUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by admin on 06/10/17.
 */

public class LeaveDetailsActivity extends BaseActivity {


    private TextView mLeaveRefId,mLeaveStatus,mFromDateText,mToDateText,mTotalDaystext,mLeaveReasontext,mManagerText;
    private EditText mReasonDescriptionText;
    private Button mCancelButton,mLeaveBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_details_page_layout);
        initToolBar();
        initWidgets();
        populateLeaveData();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView ivMyInbox = (ImageView) toolbar.findViewById(R.id.txv_summary);
        ivMyInbox.setVisibility(View.GONE);

        tvTitle.setText("Leave Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initWidgets() {

        mLeaveRefId = (TextView) findViewById(R.id.leave_reference_txt);
        mLeaveStatus = (TextView) findViewById(R.id.leave_status_txt);
        mFromDateText = (TextView) findViewById(R.id.from_date_txt);
        mToDateText = (TextView) findViewById(R.id.to_date_txt);
        mTotalDaystext = (TextView) findViewById(R.id.no_of_days);
        mLeaveReasontext = (TextView) findViewById(R.id.leave_reason_txt);
        mManagerText = (TextView) findViewById(R.id.manager_name_txt);
        mLeaveBalance = (Button) findViewById(R.id.view_leave_bal_btn);
        mReasonDescriptionText = (EditText) findViewById(R.id.manager_comments_txt);
        mCancelButton = (Button) findViewById(R.id.button_save);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerCancelLeave();
            }
        });
    }

    private void populateLeaveData() {

        if (GlobalData.gLeaveModal != null) {

            LeaveModal leaveModal = GlobalData.gLeaveModal;
            mLeaveRefId.setText(null != leaveModal.getId() ? leaveModal.getId() : "-");
            mLeaveStatus.setText(null != leaveModal.getStatus() ? leaveModal.getStatus().getName() : "-");
            mFromDateText.setText(leaveModal.getStartDate() != 0 ? DateTimeUtils.getFormattedDate(leaveModal.getStartDate(), DateTimeUtils.Format.DD_Mmm_YYYY)  : "-");
            mToDateText.setText(leaveModal.getStartDate() != 0 ? DateTimeUtils.getFormattedDate(leaveModal.getStartDate(), DateTimeUtils.Format.DD_Mmm_YYYY)  : "-");
            mTotalDaystext.setText(""+leaveModal.getCount()+"\nDAYS");
            mLeaveReasontext.setText(null != leaveModal.getLeaveReason() ? leaveModal.getLeaveReason().getName() : "-");
            mManagerText.setText(null != leaveModal.getApprover() ? leaveModal.getApprover().getCompleteName() : "-");

            switch(leaveModal.getStatus().getName().toUpperCase()) {

                case "APPROVED":
                    mCancelButton.setVisibility(View.GONE);
                    mLeaveStatus.setTextColor(getColor(R.color.greenBulb));
                    break;
                case "TAKEN":
                    mCancelButton.setVisibility(View.GONE);
                    mLeaveStatus.setTextColor(getColor(android.R.color.holo_blue_light));
                    break;
                case "CANCELLED":
                    mCancelButton.setVisibility(View.GONE);
                    mLeaveStatus.setTextColor(getColor(R.color.common_grey_2));
                    break;
                case "REJECTED":
                    mCancelButton.setVisibility(View.GONE);
                    mLeaveStatus.setTextColor(getColor(R.color.error_text_color));
                    break;
                case "APPROVAL PENDING":
                    mCancelButton.setVisibility(View.VISIBLE);
                    mLeaveStatus.setTextColor(getColor(android.R.color.holo_blue_light));
                    break;

            }
        }

    }

    private void triggerCancelLeave() {

        Type type = new TypeToken<Object>() {
        }.getType();

        String URL = APIUrls.CANCEL_LEAVE+"?leaveId="+GlobalData.gLeaveModal.getId();
        Log.d("TAG",URL);
        RequestManager.addRequest(new GsonObjectRequest<Object>(URL,null,type,new VolleyErrorListener(this,this, NetworkEvents.CANCEL_LEAVE_REQUEST)) {

            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {

                updateUi(true,NetworkEvents.CANCEL_LEAVE_REQUEST,response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        switch (actionID) {
            case NetworkEvents.CANCEL_LEAVE_REQUEST:
                AppUtil.showSnackBar(findViewById(R.id.button_save), "Leave Cancelled Successfully", Color.parseColor("#259259"));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }
}

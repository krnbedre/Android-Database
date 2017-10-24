package com.dhdigital.lms.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhdigital.lms.R;
import com.dhdigital.lms.glide.HeaderLoader;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.Files;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.modal.TaskActionRequest;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.AppUtil;
import com.dhdigital.lms.util.CircleTransform;
import com.dhdigital.lms.util.PreferenceUtil;
import com.dhdigital.lms.widgets.TaskActionDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;
import com.kelltontech.volley.utils.DateTimeUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 06/10/17.
 */

public class LeaveDetailsActivity extends BaseActivity implements View.OnClickListener {


    private TextView mRequestorName, mUserIDTextView, mLeaveRefId, mLeaveStatus, mFromDateText, mToDateText, mTotalDaystext, mLeaveReasontext, mManagerText;
    private EditText mReasonDescriptionText;
    private Button mCancelButton, mLeaveBalance, mApproveBtn, mRejectBtn;
    private ImageView mReqImage;
    private String NAVIGATION_PAGE = AppConstants.REQUESTOR;
    private LinearLayout mApproverActionContainer;
    private LeaveModal leaveModal;
    private TaskActionRequest taskActionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_details_page_layout);
        NAVIGATION_PAGE = getIntent().getStringExtra(AppConstants.NAVIGATION);
        initToolBar();
        initWidgets();
        populateLeaveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NAVIGATION_PAGE = getIntent().getStringExtra(AppConstants.NAVIGATION);
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
        mRequestorName = (TextView) findViewById(R.id.requestor_name);
        mReqImage = (ImageView) findViewById(R.id.user_icon);
        mUserIDTextView = (TextView) findViewById(R.id.requestor_id);
        mApproveBtn = (Button) findViewById(R.id.approve_button);
        mRejectBtn = (Button) findViewById(R.id.reject_button);
        mApproveBtn.setOnClickListener(this);
        mRejectBtn.setOnClickListener(this);
        mLeaveBalance.setOnClickListener(this);

        mApproverActionContainer = (LinearLayout) findViewById(R.id.approver_actions);
        if (NAVIGATION_PAGE.equalsIgnoreCase(AppConstants.APPROVER)) {
            mCancelButton.setVisibility(View.GONE);
            mApproverActionContainer.setVisibility(View.VISIBLE);
        } else {
            mApproverActionContainer.setVisibility(View.GONE);
            mCancelButton.setVisibility(View.VISIBLE);
        }
        mCancelButton.setOnClickListener(this);
    }

    private void populateLeaveData() {

        if (GlobalData.gLeaveModal != null) {

            leaveModal = GlobalData.gLeaveModal;
            mLeaveRefId.setText(null != leaveModal.getId() ? leaveModal.getId() : "-");
            mLeaveStatus.setText(null != leaveModal.getStatus() ? leaveModal.getStatus().getName() : "-");
            mFromDateText.setText(leaveModal.getStartDate() != 0 ? DateTimeUtils.getFormattedDate(leaveModal.getStartDate(), DateTimeUtils.Format.DD_Mmm_YYYY)  : "-");
            mToDateText.setText(leaveModal.getStartDate() != 0 ? DateTimeUtils.getFormattedDate(leaveModal.getStartDate(), DateTimeUtils.Format.DD_Mmm_YYYY)  : "-");
            mTotalDaystext.setText(""+leaveModal.getCount()+"\nDAYS");
            mLeaveReasontext.setText(null != leaveModal.getLeaveReason() ? leaveModal.getLeaveReason().getName() : "-");
            mManagerText.setText(null != leaveModal.getApprover() ? leaveModal.getApprover().getCompleteName() : "-");

            loadUserProf(leaveModal.getRequestedBy().getEmployee());
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
                    if (NAVIGATION_PAGE.equalsIgnoreCase(AppConstants.APPROVER)) {
                        mCancelButton.setVisibility(View.GONE);
                    } else {
                        mCancelButton.setVisibility(View.VISIBLE);
                    }
                    mLeaveStatus.setTextColor(getColor(android.R.color.holo_blue_light));
                    break;

            }
        }

    }


    private void loadUserProf(Employee employee) {


        Files fileUpload = employee.getFileUpload();
        mRequestorName.setText(employee.getCompleteName());
        mUserIDTextView.setText("Employee Id: " + String.valueOf(employee.getId()));
        if (null != fileUpload) {

            String URL = APIUrls.FILE_DOWNLOAD + "?fileName=" + fileUpload.getFileName() + "&filePath=" + fileUpload.getPathURI();
            Log.d("IMAGE", "URL: " + URL);
            //AppUtil.loadThumbNailImage(context,URL ,holder.userIcon);


            Glide.with(this)
                    .load(HeaderLoader.getUrlWithHeaders(URL, this))
                    .asBitmap()
                    .transform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.user_icon_disp)
                    .into(mReqImage);


        }


    }

    private void triggerCancelLeave(TaskActionRequest taskActionRequest) {

        Type type = new TypeToken<Object>() {
        }.getType();

        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String, Object> postPayload = new HashMap<>();
        postPayload.put("leaveId", GlobalData.gLeaveModal.getId());
        postPayload.put("comments", taskActionRequest.getComments());
        String payload = gson.toJson(postPayload);

        Log.d("TAG", "Payload: " + payload);
        String URL = APIUrls.CANCEL_LEAVE;//+"?leaveId="+GlobalData.gLeaveModal.getId();
        Log.d("TAG",URL);
        RequestManager.addRequest(new GsonObjectRequest<Object>(URL, HeaderManager.prepareMasterDataHeaders(this), payload, type, new VolleyErrorListener(this, this, NetworkEvents.CANCEL_LEAVE_REQUEST)) {

            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {

                updateUi(true,NetworkEvents.CANCEL_LEAVE_REQUEST,response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        removeProgressDialog();
        switch (actionID) {
            case NetworkEvents.CANCEL_LEAVE_REQUEST:
                AppUtil.showSnackBar(findViewById(R.id.button_save), "Leave Cancelled Successfully", Color.parseColor("#259259"));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        finish();
                    }
                }, 1000);
                break;
            case NetworkEvents.APPROVE_TASK:
                if (status) {
                    if (serviceResponse instanceof String) {
                        String result = (String) serviceResponse;
                        if (result.equalsIgnoreCase("APR001")) {
                            AppUtil.showSnackBar(findViewById(R.id.manager_name_txt), "Leave Approved Successfully", Color.parseColor("#259259"));
                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    finish();
                                }
                            }, 1000);
                            break;

                        }
                    }
                }
                break;
            case NetworkEvents.REJECT_TAKS:
                if (status) {
                    if (serviceResponse instanceof String) {
                        String result = (String) serviceResponse;
                        if (result.equalsIgnoreCase("APR002")) {
                            AppUtil.showSnackBar(findViewById(R.id.manager_name_txt), "Leave Rejected Successfully", Color.parseColor("#259259"));
                            final Handler handler3 = new Handler();
                            handler3.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    finish();
                                }
                            }, 1000);
                            break;

                        }
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

        int id = view.getId();
        switch (id) {
            case R.id.approve_button:
                onApproveClick(leaveModal);
                break;
            case R.id.reject_button:
                onRejectClick(leaveModal);
                break;
            case R.id.button_save:

                displayConfirmationBeforeCancel();
                break;
            case R.id.view_leave_bal_btn:
                Intent leaveBal = new Intent(LeaveDetailsActivity.this, MyLeaveBalanceActivity.class);
                startActivity(leaveBal);
                break;
        }
    }


    private void onApproveClick(LeaveModal view) {
        taskActionRequest = new TaskActionRequest();
        String title = null;
        taskActionRequest.setId(view.getId());
        taskActionRequest.setDelegateToEmpNo(null);
        taskActionRequest.setDelegateTo(null);
        TaskActionDialogBuilder builder = new TaskActionDialogBuilder(this, false, false, false, null); // providing comment not mandatory for "Approve"
        builder.build("APPROVE LEAVE", "APPROVE", new TaskActionDialogBuilder.OnPositiveOptListener() {
            @Override
            public void onPositionOpt(Object object, String comment) {
                taskActionRequest.setComments(comment);
                executeApproveTaskActionService(taskActionRequest);
            }
        }).show();
    }

    private void onRejectClick(LeaveModal view) {

        taskActionRequest = new TaskActionRequest();

        taskActionRequest.setId(view.getId());
        taskActionRequest.setDelegateToEmpNo(null);
        taskActionRequest.setDelegateTo(null);
        //taskactionRejectRequest.setTravelRequest(trObj);

        TaskActionDialogBuilder builder = new TaskActionDialogBuilder(this, true, false, false, null); // providing comment in "Reject" is mandatory
        builder.build("REJECT LEAVE", "REJECT", new TaskActionDialogBuilder.OnPositiveOptListener() {
            @Override
            public void onPositionOpt(Object object, String comment) {
                taskActionRequest.setComments(comment);
                // taskActionRequest.setRejectReason((MasterData) object);
                executeRejectTaskActionService(taskActionRequest);

            }
        }).show();
    }

    private void onCancelBtnClick() {

        taskActionRequest = new TaskActionRequest();
        taskActionRequest.setDelegateToEmpNo(null);
        taskActionRequest.setDelegateTo(null);
        //taskactionRejectRequest.setTravelRequest(trObj);

        TaskActionDialogBuilder builder = new TaskActionDialogBuilder(this, true, false, false, null); // providing comment in "Reject" is mandatory
        builder.build("CANCEL LEAVE", "CANCEL", new TaskActionDialogBuilder.OnPositiveOptListener() {
            @Override
            public void onPositionOpt(Object object, String comment) {
                taskActionRequest.setComments(comment);
                // taskActionRequest.setRejectReason((MasterData) object);
                triggerCancelLeave(taskActionRequest);

            }
        }).show();
    }


    public void executeApproveTaskActionService(TaskActionRequest leaveModal) {

        showProgressDialog(getString(R.string.loading));
        Type listType = new TypeToken<Object>() {
        }.getType();

        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String, Object> payload = new HashMap<>();
        payload.put("leaveId", leaveModal.getId());
        payload.put("comments", leaveModal.getComments());
        String requestPayload = gson.toJson(payload);
        Log.d("TAG", "Payload : " + requestPayload);


        RequestManager.addRequest(new GsonObjectRequest<Object>(APIUrls.APPROVER_TASK_URL, HeaderManager.prepareMasterDataHeaders(LeaveDetailsActivity.this), requestPayload, listType, new VolleyErrorListener(LeaveDetailsActivity.this, LeaveDetailsActivity.this, NetworkEvents.APPROVE_TASK)) {
            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {

                PreferenceUtil.saveCookies(LeaveDetailsActivity.this, responseHeaders);
                Log.d("TAG", "" + response);
                updateUi(true, NetworkEvents.APPROVE_TASK, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }


    public void executeRejectTaskActionService(TaskActionRequest leaveModal) {
        Type listType = new TypeToken<Object>() {
        }.getType();

        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String, Object> payload = new HashMap<>();
        payload.put("leaveId", leaveModal.getId());
        payload.put("comments", leaveModal.getComments());
        String requestPayload = gson.toJson(payload);
        Log.d("TAG", "Payload : " + requestPayload);

        RequestManager.addRequest(new GsonObjectRequest<Object>(APIUrls.REJECT_TASK_URL, HeaderManager.prepareMasterDataHeaders(LeaveDetailsActivity.this), requestPayload, listType, new VolleyErrorListener(LeaveDetailsActivity.this, LeaveDetailsActivity.this, NetworkEvents.REJECT_TAKS)) {
            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {
                PreferenceUtil.saveCookies(LeaveDetailsActivity.this, responseHeaders);
                Log.d("TAG", "" + response);
                updateUi(true, NetworkEvents.REJECT_TAKS, response);

            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }


    private void displayConfirmationBeforeCancel() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("Confirm");
        dialog.setMessage("Are you sure you want to cancel this leave?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
                onCancelBtnClick();
            }
        })
                .setNegativeButton("Dismiss ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }
}

package com.dhdigital.lms.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.adapters.MyLeavesAdapter;
import com.dhdigital.lms.fragments.FilterDialog;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.modal.MasterData;
import com.dhdigital.lms.modal.MyleavesResponse;
import com.dhdigital.lms.modal.TaskActionRequest;
import com.dhdigital.lms.modal.TaskFilterParams;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.AppUtil;
import com.dhdigital.lms.util.PreferenceUtil;
import com.dhdigital.lms.widgets.TaskActionDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 09/10/17.
 */

public class ApproverTasksActivity extends BaseActivity implements ListItemClickListener, FilterDialog.OnFilterAppliedListener {

    String mTaskIdApprove, mTaskIdReject, mReferenceNo;
    private boolean isRefreshingList = false;
    private int mTRPageIndex = 0;
    private int mECPageIndex = 0;
    private int totalElements;
    private ListView mListView;
    private MyLeavesAdapter mLeavesAdapter;
    private List<LeaveModal> mLeavesList = new ArrayList<LeaveModal>();
    private TextView emptyView;
    private TaskActionRequest taskActionRequest;

    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<MasterData> teamList = new ArrayList<>();
    private String mTeamId, mEmployeeId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_leaves_layout);
        initToolBar();
        mListView = (ListView) findViewById(R.id.list);
        emptyView = (TextView) findViewById(R.id.empty);
        mLeavesAdapter = new MyLeavesAdapter(this,mLeavesList, NetworkEvents.GET_MY_TAKS);
        mListView.setAdapter(mLeavesAdapter);
        emptyView.setText(getString(R.string.no_tasks));
        getEmployeesUnderLoggedInUser();

        executeMyLeavesAPI(NetworkEvents.GET_MY_TAKS,true);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {


            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (mListView.getCount() < totalElements) {

                        executeMyLeavesAPI(NetworkEvents.GET_MY_TAKS,false);
                    }
                }

            }
        });



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView ivMyInbox = (ImageView) toolbar.findViewById(R.id.txv_summary);
        ivMyInbox.setVisibility(View.GONE);

        tvTitle.setText("My Tasks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        removeProgressDialog();
        switch (actionID) {
            case NetworkEvents.GET_MY_TAKS:
                if (status) {
                    if (serviceResponse instanceof MyleavesResponse) {
                        MyleavesResponse response = (MyleavesResponse) serviceResponse;
                        if (response.getContent() != null) {
                            //mListTravelRequest.clear();
                            if (response.getContent().size() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                            } else {
                                totalElements = response.getTotalElements();
                                mTRPageIndex++;
                                mLeavesList.addAll(response.getContent());
                                mLeavesAdapter.notifyDataSetChanged();
                                emptyView.setVisibility(View.GONE);
                            }

                        }
                    }
                }
                isRefreshingList = false;
                break;
            case NetworkEvents.GET_MY_FILTERED_TASKS:
                if (status) {
                    if (serviceResponse instanceof MyleavesResponse) {
                        MyleavesResponse response = (MyleavesResponse) serviceResponse;
                        if (response.getContent() != null) {
                            mLeavesList.clear();
                            totalElements = response.getTotalElements();
                            mTRPageIndex++;
                            mLeavesList.addAll(response.getContent());
                            mLeavesAdapter.notifyDataSetChanged();
                            emptyView.setVisibility(View.GONE);

                        }
                    }
                }
                isRefreshingList = false;
                break;

            case NetworkEvents.APPROVE_TASK:
                if(status) {
                    if (serviceResponse instanceof String) {
                        String result = (String) serviceResponse;
                        if(result.equalsIgnoreCase("APR001")) {
                            AppUtil.showSnackBar(findViewById(R.id.empty), "Leave Approved Successfully", Color.parseColor("#259259"));
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    mLeavesList.clear();
                                    executeMyLeavesAPI(NetworkEvents.GET_MY_TAKS,true);
                                }
                            }, 1000);
                            break;

                        }
                    }
                }
                break;
            case NetworkEvents.REJECT_TAKS:
                if(status) {
                    if (serviceResponse instanceof String) {
                        String result = (String) serviceResponse;
                        if(result.equalsIgnoreCase("APR002")) {
                            AppUtil.showSnackBar(findViewById(R.id.empty), "Leave Rejected Successfully", Color.parseColor("#259259"));
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    mLeavesList.clear();
                                    executeMyLeavesAPI(NetworkEvents.GET_MY_TAKS,true);
                                }
                            }, 1000);
                            break;

                        }
                    }
                }
                break;
            case NetworkEvents.GET_EMPLOYEES:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    //mDashBoardData = (DashBoardModal) serviceResponse;

                    employeeList = (ArrayList<Employee>) serviceResponse;

                    //updateLeaveBalanceCount();
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


    private void executeMyLeavesAPI(final int event,final boolean loadStart) {
        if (isRefreshingList) return;

        isRefreshingList = true;
        if(loadStart) {
            mTRPageIndex = 0;
        }
        if (mTRPageIndex == 0) // showing the progress dialog only first time when Activity starts
            showProgressDialog(getString(R.string.loading));
        Type listType = new TypeToken<MyleavesResponse>() {
        }.getType();
//http://192.168.10.37:8080/lms/approve?leaveId=44&comments="spring TuTiTu"

        TaskFilterParams params = new TaskFilterParams();
        Gson gson = new GsonBuilder().create();
        params.setTeamId(mTeamId);
        params.setEmpId(mEmployeeId);
        String requestPayload = gson.toJson(params);


        Log.d("PAYLOAD",requestPayload);

        String url = APIUrls.APPROVER_TASKS + "?p=" + mTRPageIndex + "&size=30";


        RequestManager.addRequest(new GsonObjectRequest<MyleavesResponse>(url, HeaderManager.prepareMasterDataHeaders(ApproverTasksActivity.this), requestPayload, listType, new VolleyErrorListener(ApproverTasksActivity.this, ApproverTasksActivity.this, event)) {
            @Override
            public void deliverResponse(MyleavesResponse response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }

    @Override
    public void listItemSelected(View childView, int position) {

        Log.d("TAG","Selected Item"+position);
        Object itemAtPosition = mLeavesList.get(position);
        if (itemAtPosition instanceof LeaveModal) {
            LeaveModal leaveModal = (LeaveModal) mLeavesList.get(position);
            GlobalData.gLeaveModal = leaveModal;
            Intent intent = new Intent(getApplicationContext(), LeaveDetailsActivity.class);
            intent.putExtra(AppConstants.NAVIGATION, AppConstants.APPROVER);
            startActivity(intent);
        }

    }

    @Override
    public void listApproveBtnSelected(int position) {

        onApproveClick(mLeavesList.get(position));
    }

    @Override
    public void listRejectBtnSelected(int position) {

        onRejectClick(mLeavesList.get(position));
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
    public void executeApproveTaskActionService(TaskActionRequest leaveModal) {

        showProgressDialog(getString(R.string.loading));
        Type listType = new TypeToken<Object>() {
        }.getType();

        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String,Object> payload = new HashMap<>();
        payload.put("leaveId",leaveModal.getId());
        payload.put("comments",leaveModal.getComments());
        String requestPayload = gson.toJson(payload);
        Log.d("TAG", "Payload : " + requestPayload);



        RequestManager.addRequest(new GsonObjectRequest<Object>(APIUrls.APPROVER_TASK_URL, HeaderManager.prepareMasterDataHeaders(ApproverTasksActivity.this), requestPayload, listType, new VolleyErrorListener(ApproverTasksActivity.this, ApproverTasksActivity.this, NetworkEvents.APPROVE_TASK)) {
            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {

                PreferenceUtil.saveCookies(ApproverTasksActivity.this, responseHeaders);
                Log.d("TAG",""+response);
                updateUi(true, NetworkEvents.APPROVE_TASK, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }


    public void executeRejectTaskActionService(TaskActionRequest leaveModal) {
        Type listType = new TypeToken<Object>() {
        }.getType();

        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String,Object> payload = new HashMap<>();
        payload.put("leaveId",leaveModal.getId());
        payload.put("comments",leaveModal.getComments());
        String requestPayload = gson.toJson(payload);
        Log.d("TAG", "Payload : " + requestPayload);

        RequestManager.addRequest(new GsonObjectRequest<Object>(APIUrls.REJECT_TASK_URL, HeaderManager.prepareMasterDataHeaders(ApproverTasksActivity.this), requestPayload, listType, new VolleyErrorListener(ApproverTasksActivity.this, ApproverTasksActivity.this, NetworkEvents.REJECT_TAKS)) {
            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {
                PreferenceUtil.saveCookies(ApproverTasksActivity.this, responseHeaders);
                Log.d("TAG",""+response);
                updateUi(true, NetworkEvents.REJECT_TAKS, response);

            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                onFilterClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onFilterClicked() {
        FilterDialog dialog = new FilterDialog(this, this, this);
        dialog.show();
    }


    @Override
    public void onFilterApplied(String userId, String teamId) {

        mTeamId = teamId;
        mEmployeeId = userId;
        executeMyLeavesAPI(NetworkEvents.GET_MY_FILTERED_TASKS, true);
    }

    private void getEmployeesUnderLoggedInUser() {

        showProgressDialog(getString(R.string.loading));
        Type type = new TypeToken<ArrayList<Employee>>() {
        }.getType();


        Log.d("URL", APIUrls.GET_EMPLOYEES);

        RequestManager.addRequest(new GsonObjectRequest<List>(APIUrls.GET_EMPLOYEES, null, type, new VolleyErrorListener(this, this, NetworkEvents.GET_EMPLOYEES)) {

            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true, NetworkEvents.GET_EMPLOYEES, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }
}

package com.dhdigital.lms.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.db.MasterDataTable;
import com.dhdigital.lms.fragments.PieChartFragment;
import com.dhdigital.lms.modal.DashBoardModal;
import com.dhdigital.lms.modal.LeaveEntitlement;
import com.dhdigital.lms.modal.LeaveType;
import com.dhdigital.lms.modal.MonthWiseLeave;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
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
 * Created by admin on 16/10/17.
 */

public class MyLeaveBalanceActivity extends BaseActivity implements View.OnClickListener {


    private Context mContext;
    private AppCompatSpinner leaveTypeSpinner, employeeSpinner;
    private LeaveType mSelectedLeaveType = null;
    private TextView mleaveTypeText, mLeaveBalanceText;
    private DashBoardModal mDashBoardData;
    private FrameLayout mChartContainer;
    private List<LeaveType> leaveTypeList = new ArrayList<LeaveType>();
    private ArrayList<MonthWiseLeave> monthWiseLeaveList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(AppConstants.APP_TAG, "-onCreate-" + getLocalClassName());
        setContentView(R.layout.my_leaves_bal_layout);
        overridePendingTransition(R.anim.do_not_move_anime, R.anim.do_not_move_anime);
        mContext = getApplicationContext();
        mChartContainer = (FrameLayout) findViewById(R.id.charts_container);
        leaveTypeSpinner = (AppCompatSpinner) findViewById(R.id.leave_type_spn);
        employeeSpinner = (AppCompatSpinner) findViewById(R.id.employee_spn);
        mLeaveBalanceText = (TextView) findViewById(R.id.leave_bal_txt);
        buildToolBar();
        loadWidgets();
    }

    private void loadWidgets() {
        instantiateLeaveTypeSpinner();
    }

    /**
     * API used to initiate the Tool bar items
     */
    private void buildToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(getString(R.string.leaveBalance));
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void instantiateLeaveTypeSpinner() {

        leaveTypeList = MasterDataTable.getInstance(mContext).getAllByleaveType();
        List<String> adapterlist = new ArrayList<String>();
        for (int i = 0; i < leaveTypeList.size(); i++) {
            adapterlist.add(i, leaveTypeList.get(i).getName());
        }
        mSelectedLeaveType = leaveTypeList.get(0);

        getLeavebalance();
        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, adapterlist);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        leaveTypeSpinner.setAdapter(adapter);
        leaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //mleaveTypeText.setText(leaveTypeList.get(i).getName());
                mSelectedLeaveType = leaveTypeList.get(i);
                requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()), null);
                updateLeaveBalanceCount();
                getLeavebalance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    private void updateLeaveBalanceCount() {


        //float entitledLeaves = (float) mDashBoardData.getLeaveEntitlement().get(mSelectedLeaveType.getId());
        //float leavebalance = (float) mDashBoardData.getLeaveBalance().get(mSelectedLeaveType.getId());

        LeaveEntitlement entitlement = MasterDataTable.getInstance(mContext).getLeaveEntitlement(mSelectedLeaveType.getId());
        mLeaveBalanceText.setText(mSelectedLeaveType.getBalance() % 1 == 0 ? mSelectedLeaveType.getBalance().intValue() + "/" + entitlement.getCount() + " Days" : mSelectedLeaveType.getBalance() + "/" + entitlement.getCount() + " Days");
        // mLeaveBalanceText.setText(mSelectedLeaveType.getBalance() + "/" + (float) entitlement.getCount());
    }


    public void getLeavebalance() {

        Type type = new TypeToken<Map>() {
        }.getType();

        RequestManager.addRequest(new GsonObjectRequest<Object>(APIUrls.LEAVE_BAL_URL, null, type, new VolleyErrorListener(this, this, NetworkEvents.MY_LEAVES_BALANCE)) {

            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {

                updateUi(true, NetworkEvents.MY_LEAVES_BALANCE, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }


    public void requestDashBoardData(String leaveTypeId, String userId) {

        String URL = APIUrls.USER_DASHBOARD;
        if (leaveTypeId == null) {
            if (userId == null) {

            } else {
                URL = URL + "?userId=" + userId;
            }
        } else {
            URL = URL + "?leaveTypeId=" + leaveTypeId;
            if (userId == null) {
            } else {
                URL = URL + "&userId=" + userId;
            }
        }

        Type type = new TypeToken<ArrayList<MonthWiseLeave>>() {
        }.getType();


        Log.d("URL", URL);

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, null, type, new VolleyErrorListener(this, this, NetworkEvents.LEAVE_DASHBOARD)) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true, NetworkEvents.LEAVE_DASHBOARD, response);

            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        switch (actionID) {
            case NetworkEvents.LEAVE_DASHBOARD:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    //mDashBoardData = (DashBoardModal) serviceResponse;
                    monthWiseLeaveList = (ArrayList<MonthWiseLeave>) serviceResponse;
                    loadChart();
                    updateLeaveBalanceCount();
                }
                break;
            case NetworkEvents.MY_LEAVES_BALANCE:
                if (status && serviceResponse instanceof Map) {
                    Map<String, Double> responseMap = new HashMap();
                    responseMap = (Map<String, Double>) serviceResponse;
                    if (null != responseMap) {

                        mSelectedLeaveType.setBalance(
                                responseMap.containsKey(mSelectedLeaveType.getCode()) ?
                                        responseMap.get(mSelectedLeaveType.getCode()) :
                                        0);
                        updateLeaveBalanceCount();
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

    private void loadChart() {

        if (null != mChartContainer) {
            PieChartFragment fragment = new PieChartFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("MONTHWISE_LEAVES", monthWiseLeaveList);
            bundle.putString(AppConstants.NAVIGATION, AppConstants.CUMULATIVE_LEAVE_CHART);
            fragment.setArguments(bundle);
            if (getSupportFragmentManager().getFragments().size() >= 1) {
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.charts_container, fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.charts_container, fragment).commit();
            }

        }
    }
}



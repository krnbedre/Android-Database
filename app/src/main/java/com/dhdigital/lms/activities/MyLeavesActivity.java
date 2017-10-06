package com.dhdigital.lms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.adapters.MyLeavesAdapter;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.Leave;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.modal.MyleavesResponse;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.google.gson.reflect.TypeToken;

import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 04/10/17.
 */

public class MyLeavesActivity extends BaseActivity {


    private boolean isRefreshingList = false;
    private int mTRPageIndex = 0;
    private int mECPageIndex = 0;
    private int totalElements;

    private ListView mListView;

    private MyLeavesAdapter mLeavesAdapter;
    private List<LeaveModal> mLeavesList = new ArrayList<LeaveModal>();
    private TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_leaves_layout);
        initToolBar();
        mListView = (ListView) findViewById(R.id.list);
        emptyView = (TextView) findViewById(R.id.empty);
        mLeavesAdapter = new MyLeavesAdapter(this,mLeavesList, NetworkEvents.MY_LEAVES_REQUEST);
        mListView.setAdapter(mLeavesAdapter);
        emptyView.setText("No Leaves Requests found");


        executeMyLeavesAPI(NetworkEvents.MY_LEAVES_REQUEST);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {


            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (mListView.getCount() < totalElements) {

                        executeMyLeavesAPI(NetworkEvents.MY_LEAVES_REQUEST);
                        }
                    }

            }
        });



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);
                if (itemAtPosition instanceof LeaveModal) {
                    LeaveModal leaveModal = (LeaveModal) parent.getItemAtPosition(position);
                    GlobalData.gLeaveModal = leaveModal;
                    Intent intent = new Intent(getApplicationContext(), LeaveDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView ivMyInbox = (ImageView) toolbar.findViewById(R.id.txv_summary);
        ivMyInbox.setVisibility(View.GONE);

        tvTitle.setText("My Leaves");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        removeProgressDialog();
        switch (actionID) {
            case NetworkEvents.MY_LEAVES_REQUEST:
                if (status) {
                    if (serviceResponse instanceof MyleavesResponse) {
                        MyleavesResponse response = (MyleavesResponse) serviceResponse;
                        if (response.getContent() != null) {
                            //mListTravelRequest.clear();
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
        }
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }


    private void executeMyLeavesAPI(final int event) {
        if (isRefreshingList) return;

        isRefreshingList = true;
        if (mTRPageIndex == 0) // showing the progress dialog only first time when Activity starts
            showProgressDialog("Loading");
        Type listType = new TypeToken<MyleavesResponse>() {
        }.getType();

        String url = APIUrls.MY_LEAVES + "?p=" + mTRPageIndex + "&size=10";


        RequestManager.addRequest(new GsonObjectRequest<MyleavesResponse>(url, HeaderManager.prepareMasterDataHeaders(MyLeavesActivity.this), null, listType, new VolleyErrorListener(MyLeavesActivity.this, MyLeavesActivity.this, event)) {
            @Override
            public void deliverResponse(MyleavesResponse response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }
}

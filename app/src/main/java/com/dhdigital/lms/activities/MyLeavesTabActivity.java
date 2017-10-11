package com.dhdigital.lms.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.fragments.ListFragment;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.modal.MasterData;
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
 * Created by admin on 06/10/17.
 */

public class MyLeavesTabActivity extends BaseActivity {


    private static ViewPager viewPager;
    private static TabLayout tabLayout;

    private ArrayList<LeaveModal> mAllLeaves = new ArrayList<LeaveModal>();
    private ArrayList<LeaveModal> mScheduledLeaves = new ArrayList<LeaveModal>();
    private ArrayList<LeaveModal> mPastLeaves = new ArrayList<LeaveModal>();


    private boolean isRefreshingList = false;
    private int mTRPageIndex = 0;
    private int mECPageIndex = 0;
    private int totalElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_leaves_tab_layout);
        initToolBar();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager

        //Implementing tab selected listener over tablayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                switch (tab.getPosition()) {
                    case 0:
                        Log.d("Count",""+mScheduledLeaves.size());
                        Log.e("TAG","TAB1");
                        break;
                    case 1:
                        Log.d("Count",""+mPastLeaves.size());
                        Log.e("TAG","TAB2");
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        setupViewPager(viewPager);
        executeMyLeavesAPI(NetworkEvents.MY_LEAVES_REQUEST,true);
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


    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ListFragment("SCHEDULED",mScheduledLeaves), "SCHEDULED");
        adapter.addFrag(new ListFragment("PAST",mPastLeaves), "PAST");
        viewPager.setAdapter(adapter);
    }


    //View Pager fragments setting adapter class
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
        private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        //adding fragments and title method
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
                            if (mAllLeaves.size() > 0) {
                                mAllLeaves.clear();
                            }
                            if(mScheduledLeaves.size() > 0 ) {
                                mScheduledLeaves.clear();
                            }
                            if(mPastLeaves.size() > 0) {
                                mPastLeaves.clear();
                            }
                            mAllLeaves.addAll(response.getContent());
                            categorizeList();
                            setupViewPager(viewPager);
                        }
                    }
                }
                isRefreshingList = false;
                break;
        }
    }



    private void categorizeList() {

        for ( int i=0 ;i<mAllLeaves.size();i++) {

            LeaveModal leaveModalItem = mAllLeaves.get(i);
            if (null != leaveModalItem) {
                MasterData status = leaveModalItem.getStatus();



                switch (status.getName().toUpperCase()) {

                    case "APPROVED":
                        mScheduledLeaves.add(leaveModalItem);
                        break;
                    case "TAKEN":
                        mPastLeaves.add(leaveModalItem);
                        break;
                    case "CANCELLED":
                        mPastLeaves.add(leaveModalItem);
                        break;
                    case "REJECTED":
                        mPastLeaves.add(leaveModalItem);
                        break;
                    case "APPROVAL PENDING":
                        mScheduledLeaves.add(leaveModalItem);
                        break;

                }
            }
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
            showProgressDialog("Loading");
        Type listType = new TypeToken<MyleavesResponse>() {
        }.getType();



        String url = APIUrls.MY_LEAVES + "?p=" + mTRPageIndex + "&ps=10";


        RequestManager.addRequest(new GsonObjectRequest<MyleavesResponse>(url, HeaderManager.prepareMasterDataHeaders(MyLeavesTabActivity.this), null, listType, new VolleyErrorListener(MyLeavesTabActivity.this, MyLeavesTabActivity.this, event)) {
            @Override
            public void deliverResponse(MyleavesResponse response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }
}

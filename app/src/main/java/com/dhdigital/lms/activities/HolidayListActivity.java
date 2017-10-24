package com.dhdigital.lms.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.adapters.HolidayListRecyclerAdapter;
import com.dhdigital.lms.db.MasterDataTable;
import com.dhdigital.lms.modal.Holiday;
import com.kelltontech.volley.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 13/10/17.
 */

public class HolidayListActivity extends BaseActivity {


    private List<Holiday> listHolidayList = new ArrayList<Holiday>();
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView mScreenTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holiday_list_layout);
        initializeWidgets();
    }

    private void initializeWidgets() {
        setRecyclerView();
        initToolBar();

    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        toolbar.setVisibility(View.VISIBLE);

        mScreenTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        mScreenTitle.setText("Holiday List");

        ImageView ivMyInbox = (ImageView) toolbar.findViewById(R.id.txv_summary);
        ivMyInbox.setVisibility(View.GONE);
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


    //Setting recycler view
    private void setRecyclerView() {

        listHolidayList = MasterDataTable.getInstance(this).getAllHolidaysList();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(this));//Linear Items

        HolidayListRecyclerAdapter adapter = new HolidayListRecyclerAdapter(this, listHolidayList);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }
}

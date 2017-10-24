package com.dhdigital.lms.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhdigital.lms.R;
import com.dhdigital.lms.db.MasterDataTable;
import com.dhdigital.lms.modal.LeaveType;
import com.dhdigital.lms.modal.MasterData;
import com.kelltontech.volley.ui.activity.BaseActivity;
import com.kelltontech.volley.utils.DateTimeUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.dhdigital.lms.util.AppConstants.END_DATE_FILTER;
import static com.dhdigital.lms.util.AppConstants.LEAVE_TYPE_FILTER;
import static com.dhdigital.lms.util.AppConstants.MY_LEAVES_FILTER_INTENT;
import static com.dhdigital.lms.util.AppConstants.START_DATE_FILTER;
import static com.dhdigital.lms.util.AppConstants.STATUS_FILTER;

/**
 * Created by admin on 12/10/17.
 */

public class MyLeavesFilterDialog extends BaseActivity implements View.OnClickListener {

    private Context mContext = MyLeavesFilterDialog.this;
    private List<LeaveType> leaveTypeList = new ArrayList<>();
    private ArrayList<MasterData> teamList = new ArrayList<>();
    private AppCompatSpinner leaveTypeSpinner, statusSpinner;
    private TextView mFromDateText, mToDateText;
    private String mSelectedLeavetype = null;
    private String mSelectedStatus = null;
    private long mSelectedStartDate = 0;
    private long mSelectedEndDate = 0;
    private Calendar currentCalendar = Calendar.getInstance();
    private Calendar startDateCalendar = currentCalendar;
    private Calendar beginDateCalendar = currentCalendar;
    private Calendar endDateCalendar = currentCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.my_leaves_filter_dialog);
        findViewById(R.id.button_apply).setOnClickListener(this);
        mFromDateText = (TextView) findViewById(R.id.from_date_txt);
        mFromDateText.setOnClickListener(this);
        mToDateText = (TextView) findViewById(R.id.to_date_txt);
        mToDateText.setOnClickListener(this);
        instantiateLeaveTypeSpinner();
        instantiateStatusSpinner();

        //initToolBar();


    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView ivMyInbox = (ImageView) toolbar.findViewById(R.id.txv_summary);
        ivMyInbox.setVisibility(View.GONE);

        tvTitle.setText("Filter");
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void instantiateLeaveTypeSpinner() {


        leaveTypeList = MasterDataTable.getInstance(mContext).getAllByleaveType();
        leaveTypeSpinner = (AppCompatSpinner) findViewById(R.id.leaveType_spn);
        List<String> adapterlist = new ArrayList<String>();

        for (int i = 0; i < leaveTypeList.size(); i++) {
            adapterlist.add(i, leaveTypeList.get(i).getName());

        }
        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        ArrayAdapter emp_adapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, adapterlist);
        emp_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        leaveTypeSpinner.setAdapter(emp_adapter);
        leaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                mSelectedLeavetype = String.valueOf(leaveTypeList.get(position).getId());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    private void instantiateStatusSpinner() {


        statusSpinner = (AppCompatSpinner) findViewById(R.id.status_spn);
        final List<String> adapterlist = new ArrayList<String>();
        adapterlist.add(0, "Approved");
        adapterlist.add(1, "Rejected");
        adapterlist.add(2, "Cancelled");
        adapterlist.add(3, "Pending");


        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        ArrayAdapter team_adapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, adapterlist);
        team_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        statusSpinner.setAdapter(team_adapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                switch (adapterlist.get(position)) {
                    case "Approved":
                        mSelectedStatus = "APR001";
                        break;
                    case "Rejected":
                        mSelectedStatus = "APR002";
                        break;
                    case "Cancelled":
                        mSelectedStatus = "REQ009";
                        break;
                    case "Pending":
                        mSelectedStatus = "APR000";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.textView_clear:
                onClearBtnClicked();
                break;
            case R.id.button_apply:
                onApplySearchBtnClicked();
                break;
            case R.id.from_date_txt:
                showStartDateCalendar();
                break;
            case R.id.to_date_txt:
                showEndDateCalendar();
                break;

        }

    }

    private void onClearBtnClicked() {
        mSelectedLeavetype = null;
        mSelectedStatus = null;
        mSelectedStartDate = 0;
        mSelectedEndDate = 0;
        mFromDateText.setText("");
        mToDateText.setText("");
        instantiateLeaveTypeSpinner();
        instantiateStatusSpinner();
    }


    public void showStartDateCalendar() {


        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int selectedYear, int selectedMonth, int selectedDay) {
                        Toast.makeText(mContext, "Selected Date: " + selectedDay + "/" + selectedMonth + 1 + "/" + selectedYear, Toast.LENGTH_SHORT).show();
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(selectedYear, selectedMonth, selectedDay, 00, 00, 00);
                        mSelectedStartDate = selectedCal.getTimeInMillis();
                        startDateCalendar = selectedCal;
                        mFromDateText.setText(DateTimeUtils.getFormattedDate(mSelectedStartDate, DateTimeUtils.Format.DD_Mmm_YYYY));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor("#323232");
        Calendar minDateCal = Calendar.getInstance();
        minDateCal.set(minDateCal.get(Calendar.YEAR), 00, 01);

        Calendar maxDateCal = Calendar.getInstance();
        maxDateCal.set(minDateCal.get(Calendar.YEAR), 11, 31);


        if (mSelectedEndDate != 0) {
            maxDateCal.setTimeInMillis(mSelectedEndDate);
        }


        dpd.setMinDate(minDateCal);
        dpd.setMaxDate(maxDateCal);

        //if(dpd != null) dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    public void showEndDateCalendar() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int selectedYear, int selectedMonth, int selectedDay) {

                        Toast.makeText(mContext, "Selected Date: " + selectedDay + "/" + selectedMonth + 1 + "/" + selectedYear, Toast.LENGTH_SHORT).show();
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(selectedYear, selectedMonth, selectedDay);
                        mSelectedEndDate = selectedCal.getTimeInMillis();

                        endDateCalendar = selectedCal;
                        mToDateText.setText(DateTimeUtils.getFormattedDate(mSelectedEndDate, DateTimeUtils.Format.DD_Mmm_YYYY));
                        //calculateNoOfDays();
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor("#323232");
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        Calendar minDateCal = Calendar.getInstance();
        minDateCal.set(minDateCal.get(Calendar.YEAR), 00, 01);

        Calendar maxDateCal = Calendar.getInstance();
        maxDateCal.set(minDateCal.get(Calendar.YEAR), 11, 31);

        dpd.setMinDate(minDateCal);
        if (mSelectedStartDate != 0) {
            minDateCal.setTimeInMillis(mSelectedStartDate);
            dpd.setMinDate(minDateCal);
            if (minDateCal.after(maxDateCal)) {
                Log.d("LOG", "True");
            }
        }

        if (minDateCal.after(maxDateCal)) {
            Log.d("LOG", "True");
            dpd.setMaxDate(minDateCal);
        } else {
            dpd.setMaxDate(maxDateCal);
        }

        dpd.show(getFragmentManager(), "Datepickerdialog");


    }


    private void onApplySearchBtnClicked() {

        Intent data = new Intent();
        data.putExtra(STATUS_FILTER, mSelectedStatus);
        data.putExtra(LEAVE_TYPE_FILTER, mSelectedLeavetype);
        data.putExtra(START_DATE_FILTER, mSelectedStartDate);
        data.putExtra(END_DATE_FILTER, mSelectedEndDate);
        setResult(MY_LEAVES_FILTER_INTENT, data);
        finish();

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

package com.dhdigital.lms.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dhdigital.lms.R;
import com.dhdigital.lms.adapters.CustomAlertAdapter;
import com.dhdigital.lms.db.MasterDataTable;
import com.dhdigital.lms.modal.FailResponse;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.Holiday;
import com.dhdigital.lms.modal.Leave;
import com.dhdigital.lms.modal.LeaveEntitlement;
import com.dhdigital.lms.modal.LeaveType;
import com.dhdigital.lms.modal.MasterData;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.AppUtil;
import com.dhdigital.lms.util.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;
import com.kelltontech.volley.utils.DateTimeUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 28/09/17.
 */

public class NewLeaveRequestActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {


    private Toolbar toolbar;
    private Button mSubmitBtn;
    private TextView mScreenTitle;

    private Context mContext = NewLeaveRequestActivity.this;
    private TextView mleaveTypeText, mLeaveBalanceText,mFromDateText,mToDateText,mTotalDaystext,mLeaveReasontext;
    private EditText mReasonDescriptionText;
    private TextInputLayout mReasonTextTil;
    private LinearLayout mFromDateContainer,mToDateContainer,mLeaveReasonContainer,mAppliedLeavesDaysContainer;

    private AppCompatSpinner leaveTypeSpinner;


    private AlertDialog myalertDialog = null;
    private ListView listView;
    private int mWidgetItem = 0;
    private List<LeaveEntitlement> leaveEntitlementList = new ArrayList<LeaveEntitlement>();
    private List<LeaveType> leaveTypeList = new ArrayList<LeaveType>();
    private List<MasterData> leaveResonlist = new ArrayList<MasterData>();
    private List<MasterData> teamList = new ArrayList<MasterData>();
    private List<Holiday> listHolidayList = new ArrayList<Holiday>();
    private ArrayList<String> sorted_list = new ArrayList<String>();
    private String[] string_array;


    private LeaveType mSelectedLeaveType = null;
    private MasterData mSelectedLeaveReason = null;
    private long mSelectedStartDate = 0;
    private long mSelectedEndDate = 0;

    private Calendar currentCalendar = Calendar.getInstance();
    private Calendar startDateCalendar = currentCalendar;
    private Calendar beginDateCalendar = currentCalendar;
    private Calendar endDateCalendar = currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_leave_request_layout);
        initToolBar();
        getLeavebalance();
        initWidgets();

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        toolbar.setVisibility(View.VISIBLE);

        mScreenTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        mScreenTitle.setText("Apply Leave");

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

    private void initWidgets() {

        //mleaveTypeText = (TextView) findViewById(R.id.leave_type_txt);
        mLeaveBalanceText = (TextView) findViewById(R.id.leave_bal_txt);
        mFromDateText  = (TextView) findViewById(R.id.from_date_txt);
        mToDateText = (TextView) findViewById(R.id.to_date_txt);
        mTotalDaystext  = (TextView) findViewById(R.id.total_days_txt);
        mReasonDescriptionText  = (EditText) findViewById(R.id.editText_description);
        mLeaveReasontext = (TextView) findViewById(R.id.leave_reason_txt);
        leaveTypeSpinner =  (AppCompatSpinner) findViewById(R.id.leave_type_spn);
        mFromDateContainer = (LinearLayout) findViewById(R.id.from_date_container);
        mToDateContainer  = (LinearLayout) findViewById(R.id.to_date_container);
        mLeaveReasonContainer =  (LinearLayout) findViewById(R.id.leave_reason_container);
        mAppliedLeavesDaysContainer  =  (LinearLayout) findViewById(R.id.applied_leaves_day);
        mReasonTextTil = (TextInputLayout) findViewById(R.id.til_desc);
        mSubmitBtn = (Button) findViewById(R.id.button_save);
        mSubmitBtn.setOnClickListener(this);
        //mleaveTypeText.setOnClickListener(this);
        mLeaveReasontext.setOnClickListener(this);
        mToDateText.setOnClickListener(this);
        mFromDateText.setOnClickListener(this);



        /*mTilLeaveType = (TextInputLayout) findViewById(R.id.til_leave_type);
        mTilLeavebal = (TextInputLayout) findViewById(R.id.til_leave_balance);
        mTilFromDate = (TextInputLayout) findViewById(R.id.til_from_date);
        mTilToDate = (TextInputLayout) findViewById(R.id.til_to_date);
        mTilTotalDays = (TextInputLayout) findViewById(R.id.til_total_days);
        mTilLeaveReason = (TextInputLayout) findViewById(R.id.til_total_days);*/

        mSelectedStartDate = currentCalendar.getTimeInMillis();
        mSelectedEndDate = currentCalendar.getTimeInMillis();


        leaveTypeList = MasterDataTable.getInstance(mContext).getAllByleaveType();


        List<String> adapterlist = new ArrayList<String>();
        for (int i=0;i<leaveTypeList.size();i++) {
            adapterlist.add(i,leaveTypeList.get(i).getName());
        }
        mSelectedLeaveType = leaveTypeList.get(0);
        getLeavebalance();

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, adapterlist);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        leaveTypeSpinner.setAdapter(adapter);
        leaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //mleaveTypeText.setText(leaveTypeList.get(i).getName());
                mSelectedLeaveType = leaveTypeList.get(i);
                getLeavebalance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    private void updateLeaveBalanceCount() {


        LeaveEntitlement entitlement = MasterDataTable.getInstance(mContext).getLeaveEntitlement(mSelectedLeaveType.getId());

        mLeaveBalanceText.setText(mSelectedLeaveType.getBalance() + "/" + entitlement.getCount() + " Days");
    }


    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        removeProgressDialog();
        switch (actionID) {
            case NetworkEvents.SUBMIT_LEAVE_REQUEST:
                if (status && serviceResponse instanceof Leave) {

                    AppUtil.showSnackBar(findViewById(R.id.button_save), "Leave Applied Successfully", Color.parseColor("#259259"));
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            finish();
                        }
                    }, 1000);

                } else {
                    handleErrorMsgs(serviceResponse);
                }
                break;
            case NetworkEvents.MY_LEAVES_BALANCE:
                if (status && serviceResponse instanceof Map) {
                    Map<String,Double>  responseMap = new HashMap();
                    responseMap = (Map<String, Double>) serviceResponse;
                    if (null != responseMap) {

                        mSelectedLeaveType.setBalance(
                                responseMap.containsKey(mSelectedLeaveType.getCode()) ?
                                responseMap.get(mSelectedLeaveType.getCode()) :
                                        0);
                        updateLeaveBalanceCount();
                    }
                } else {
                    handleErrorMsgs(serviceResponse);
                }
                break;
            case NetworkEvents.CALCULATE_NO_DAYS:
                if (status && serviceResponse instanceof List<?>) {
                    List<String> noOfDays = (List<String>) serviceResponse;
                    updateAppliedLeaveDays(noOfDays.size());
                } else {
                    handleErrorMsgs(serviceResponse);

                }
        }
    }


    private void handleErrorMsgs(Object serviceResponse) {

        if (serviceResponse instanceof FailResponse) {
            FailResponse res = (FailResponse) serviceResponse;
            AppUtil.showSnackBar(findViewById(R.id.button_save), res.getMessage(), Color.parseColor("#A52A2A"));
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

            case R.id.leave_bal_txt:
                break;
            case R.id.from_date_txt:
                    showStartDateCalendar();
                break;
            case R.id.to_date_txt:
                if (validateFromDate()) {
                   showEndDateCalendar();
                }
                break;
            case R.id.leave_reason_txt:
                mWidgetItem = 2;
                showAlertDialog(R.string.leaveReason,false);
                break;
            case R.id.total_days_txt:
                break;
            case R.id.editText_description:
                mReasonDescriptionText.setBackground(getResources().getDrawable(R.drawable.card_style, null));
                break;
            case R.id.button_save:
                if (validateForm()) {
                    submitLeaveRequest();
                } else {
                    AppUtil.showSnackBar(findViewById(R.id.button_save), "Fill in the highlighted fields", Color.parseColor("#A52A2A"));
                }
                break;
        }
    }











    public MasterData getSelectedItemFromDialog(String editText) {
        //List<MasterData> allCostCenter = MasterDataTable.getInstance(mContext).getAllByType(MasterData.TYPE.CostCentre);
        int position = 0;
        for (int iterator = 0; iterator < leaveResonlist.size(); iterator++) {
            Log.d("TAG", "list selection :: " + editText + "masterData :: " + leaveResonlist.get(iterator).toString() );
            if (editText.toString().equals(leaveResonlist.get(iterator).getName().toString())) {
                Log.d("TAG", "Equals :: " + leaveResonlist.get(iterator));
                position = iterator;
            }
        }
        Log.d("TAG", "final return :: " + leaveResonlist.get(position));
        return leaveResonlist.get(position);
    }



    public void showStartDateCalendar() {


        if (mSelectedEndDate != 0) {
            mSelectedEndDate = 0;

        }
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int selectedYear, int selectedMonth, int selectedDay) {
                        Toast.makeText(mContext,"Selected Date: "+selectedDay+"/"+selectedMonth+1+"/"+selectedYear,Toast.LENGTH_SHORT).show();
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(selectedYear,selectedMonth,selectedDay,00,00,00);
                        mSelectedStartDate = selectedCal.getTimeInMillis();
                        startDateCalendar = selectedCal;
                        beginDateCalendar = selectedCal;
                        mSelectedEndDate = 0;
                        mToDateText.setText("");
                        mFromDateContainer.setBackground(getResources().getDrawable(R.drawable.card_style,null));
                        mFromDateText.setText(DateTimeUtils.getFormattedDate(mSelectedStartDate, DateTimeUtils.Format.DD_Mmmm_YYYY));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor("#323232");
        Calendar minDateCal = Calendar.getInstance();
        minDateCal.set(minDateCal.get(Calendar.YEAR),00,01);

        Calendar maxDateCal = Calendar.getInstance();
        maxDateCal.set(minDateCal.get(Calendar.YEAR),11,31);


        if (mSelectedLeaveType !=null) {
            LeaveEntitlement entitlement = MasterDataTable.getInstance(mContext).getLeaveEntitlement(mSelectedLeaveType.getId());
            minDateCal.setTimeInMillis(entitlement.getStartDate());
            maxDateCal.setTimeInMillis(entitlement.getEndDate());
        }

        if (mSelectedEndDate != 0) {
            maxDateCal.setTimeInMillis(mSelectedEndDate);
        }


        dpd.setMinDate(minDateCal);
        dpd.setMaxDate(maxDateCal);
        disableWeekendsinCalendar(dpd,minDateCal);
        disableHolidaysinCalendar(dpd);

        //if(dpd != null) dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }



    public void disableWeekendsinCalendar(DatePickerDialog datePickerDialog,Calendar minDateCal) {
        //Disable sundays and Saturdays
        Calendar sunday = minDateCal;
        Calendar saturday = minDateCal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String a = sdf.format(new Date(minDateCal.getTimeInMillis()));
        Log.d("START WEEKEND DATE", "TimeStamp: " + minDateCal.getTimeInMillis() + "Date: " + a);
        List<Calendar> weekends = new ArrayList<>();
        int weeks = 53;

        for (int i = 0; i < (weeks * 7) ; i = i + 7) {

            sunday = Calendar.getInstance();
            sunday.set(minDateCal.get(Calendar.YEAR), minDateCal.get(Calendar.MONTH), minDateCal.get(Calendar.DAY_OF_MONTH));
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            saturday = Calendar.getInstance();
            saturday.set(minDateCal.get(Calendar.YEAR), minDateCal.get(Calendar.MONTH), minDateCal.get(Calendar.DAY_OF_MONTH));
            saturday.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - saturday.get(Calendar.DAY_OF_WEEK) + i));
            weekends.add(saturday);
            weekends.add(sunday);
        }
        Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
        datePickerDialog.setDisabledDays(disabledDays);
        printDates(disabledDays);

    }


    private void printDates(Calendar[] disabledDays) {

        for (int i = 0; i < disabledDays.length; i++) {
            long holidayDate = disabledDays[i].getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String a = sdf.format(new Date(holidayDate));
            Log.d("WEEKENDS", "TimeStamp: " + holidayDate + "Date: " + a);
        }

    }


    public void disableHolidaysinCalendar(DatePickerDialog datePickerDialog) {

        listHolidayList = MasterDataTable.getInstance(mContext).getAllHolidaysList();
        for (int i=0 ; i < listHolidayList.size() ; i++) {


            long holidayDate = listHolidayList.get(i).getDate();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String a = sdf.format(new Date(holidayDate));
            Log.d("HOLIDAYS","TimeStamp: "+holidayDate + "Date: "+a);
            java.util.Date date = null;
            try {
                date = sdf.parse(a);

                ;
                System.out.println( dateToCalendar(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Calendar> dates = new ArrayList<>();
            dates.add( dateToCalendar(date));
            Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
            datePickerDialog.setDisabledDays(disabledDays1);
            datePickerDialog.setHighlightedDays(disabledDays1);



        }
    }


    public void showEndDateCalendar() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int selectedYear, int selectedMonth, int selectedDay) {

                        Toast.makeText(mContext,"Selected Date: "+selectedDay+"/"+selectedMonth+1+"/"+selectedYear,Toast.LENGTH_SHORT).show();
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(selectedYear,selectedMonth,selectedDay,00,00,00);
                        mSelectedEndDate = selectedCal.getTimeInMillis();

                        endDateCalendar = selectedCal;
                        mToDateContainer.setBackground(getResources().getDrawable(R.drawable.card_style,null));
                        mToDateText.setText(DateTimeUtils.getFormattedDate(mSelectedEndDate, DateTimeUtils.Format.DD_Mmmm_YYYY));
                        //calculateNoOfDays();
                        getAppliedLeaveDays(mSelectedStartDate,mSelectedEndDate);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor("#323232");
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        Calendar minDateCal = Calendar.getInstance();
        minDateCal.set(minDateCal.get(Calendar.YEAR),00,01);

        Calendar maxDateCal = Calendar.getInstance();
        maxDateCal.set(minDateCal.get(Calendar.YEAR),11,31);

        dpd.setMinDate(minDateCal);
        if (mSelectedStartDate != 0) {
            minDateCal.setTimeInMillis(mSelectedStartDate);
            dpd.setMinDate(minDateCal);
            if (minDateCal.after(maxDateCal)) {
                Log.d("LOG","True");
            }
        }

        if (minDateCal.after(maxDateCal)) {
            Log.d("LOG","True");
            dpd.setMaxDate(minDateCal);
        } else {
            dpd.setMaxDate(maxDateCal);
        }


        //
        disableWeekendsinCalendar(dpd,minDateCal);
        disableHolidaysinCalendar(dpd);
        //if(dpd != null) dpd.setOnDateSetListener();
        dpd.show(getFragmentManager(), "Datepickerdialog");



    }



    /*private void calculateNoOfDays() {

        //mAppliedLeavesDaysContainer.setVisibility(View.VISIBLE);

        Calendar startingDate = beginDateCalendar;

        List<String> holidaysList = new ArrayList<>();
        List<String> leaveAppliedDays = new ArrayList<>();

        Calendar holidayMaster = Calendar.getInstance();


        for (int i=0 ;i < listHolidayList.size() ;i++){

            Calendar holiday = Calendar.getInstance();
            holiday.setTimeInMillis(listHolidayList.get(i).getDate());
            holidaysList.add(DateTimeUtils.getFormattedDate(holiday, DateTimeUtils.Format.DD_MM_YYYY));
            Log.d("TAG","Holiday Dates :"+DateTimeUtils.getFormattedDate(holiday, DateTimeUtils.Format.DD_MM_YYYY));

        }

        Log.d("TAG","Start Dates :"+DateTimeUtils.getFormattedDate(startDateCalendar, DateTimeUtils.Format.DD_MM_YYYY));

        //Log.d("TAG","Start Dates :"+DateTimeUtils.getFormattedDate(startingDate, DateTimeUtils.Format.DD_MM_YYYY));
        Log.d("TAG","End Dates :"+DateTimeUtils.getFormattedDate(endDateCalendar, DateTimeUtils.Format.DD_MM_YYYY));


        while(startingDate.getTimeInMillis() <= endDateCalendar.getTimeInMillis()) {

            Calendar holiday = Calendar.getInstance();
            holiday.setTimeInMillis(startingDate.getTimeInMillis());
            if (!(holiday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || holiday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY )) {

                for (int i = 0; i < listHolidayList.size(); i++) {
                    Calendar holidayDate = Calendar.getInstance();
                    holidayDate.setTimeInMillis(listHolidayList.get(i).getDate());
                    String date1 = DateTimeUtils.getFormattedDate(holidayDate, DateTimeUtils.Format.DD_MM_YYYY);
                    String date2 = DateTimeUtils.getFormattedDate(startingDate, DateTimeUtils.Format.DD_MM_YYYY);

                    if (date1.equalsIgnoreCase(date2)) {

                        Log.d("HOLIDAY", date1);
                        Log.d("COMP HOLIDAY", date2);
                        Log.d("FILTERED", "Removing Holiday :" + DateTimeUtils.getFormattedDate(startingDate, DateTimeUtils.Format.DD_MM_YYYY));

                    } else {
                        if (!leaveAppliedDays.contains(DateTimeUtils.getFormattedDate(startingDate, DateTimeUtils.Format.DD_MM_YYYY))) {
                            leaveAppliedDays.add(DateTimeUtils.getFormattedDate(startingDate, DateTimeUtils.Format.DD_MM_YYYY));
                            Log.d("ADDED", "Dates :" + DateTimeUtils.getFormattedDate(startingDate, DateTimeUtils.Format.DD_MM_YYYY));
                        }


                    }
                    //  Log.d("TAG", "Dates :" + DateTimeUtils.getFormattedDate(startingDate, DateTimeUtils.Format.DD_MM_YYYY));
                }
            }
            startingDate.add(Calendar.DATE,1);

        }
        *//*if (leaveAppliedDays.size() == 1) {
            mTotalDaystext.setText("1 DAY");
        } else {
            mTotalDaystext.setText(leaveAppliedDays.size()+" DAYS");
        }*//*


        Log.d("APPLIED LEAVES DAYS :", ""+leaveAppliedDays.size());
        Log.d("TAG","Start Dates :"+DateTimeUtils.getFormattedDate(startDateCalendar, DateTimeUtils.Format.DD_MM_YYYY));


    }*/


    public void showAlertDialog(int type, boolean searchBox) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.MyDialogTheme));
        final EditText editText = new EditText(mContext);

        listView = new ListView(mContext);
        switch (type) {
            case R.string.leaveType:
                leaveTypeList = MasterDataTable.getInstance(mContext).getAllByleaveType();
                string_array = new String[leaveTypeList.size()];
                for (int i = 0; i < leaveTypeList.size(); i++) {
                    string_array[i] = leaveTypeList.get(i).getName();
                }
                break;
            case R.string.leaveEntitlement:
                leaveEntitlementList = MasterDataTable.getInstance(mContext).getAllByLeaveEntitlementType();
                string_array = new String[leaveEntitlementList.size()];
                for (int i = 0; i < leaveEntitlementList.size(); i++) {
                    string_array[i] = leaveEntitlementList.get(i).getName();
                }
                break;
            case R.string.leaveReason:
                leaveResonlist = MasterDataTable.getInstance(mContext).getAllByLeaveReason();
                string_array = new String[leaveResonlist.size()];
                for (int i = 0; i < leaveResonlist.size(); i++) {
                    string_array[i] = leaveResonlist.get(i).getName();
                }
                break;

        }
        builderSingle.setTitle(type);


        sorted_list = new ArrayList<String>(Arrays.asList(string_array));
        Collections.sort(sorted_list);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(15, 10, 15, 0);
        editText.setBackground(getResources().getDrawable(R.drawable.rectangle));
        editText.setTextColor(Color.parseColor("#414042"));
        if (searchBox) {
            layout.addView(editText);
        }
        layout.addView(listView);
        builderSingle.setView(layout);
        CustomAlertAdapter arrayAdapter = new CustomAlertAdapter(NewLeaveRequestActivity.this, sorted_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                final int textlength = editText.getText().length();
                sorted_list.clear();
                for (int i = 0; i < string_array.length; i++) {
                    if (textlength <= string_array[i].length()) {
                        if (string_array[i].toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                            sorted_list.add(string_array[i]);
                        }
                    }
                }
                listView.setAdapter(new CustomAlertAdapter(NewLeaveRequestActivity.this, sorted_list));

            }

        });
        myalertDialog = builderSingle.create();
        Window window = myalertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(wlp.MATCH_PARENT, wlp.WRAP_CONTENT);

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        myalertDialog.show();
    }


    public ArrayList<String> getListinPopUp() {
        return sorted_list;
    }


    public ListView getListView() {
        return listView;
    }

    public AlertDialog getAlertDialog() {
        return myalertDialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getAlertDialog().dismiss();
        String selectedItem =null;
        switch(mWidgetItem){
            case 1:
                selectedItem = getListinPopUp().get(position);
               // mleaveTypeText.setText(selectedItem);
                mSelectedLeaveType = getSelectedLeaveTypeFromDialog(selectedItem);
                mLeaveBalanceText.setText("12");
                break;
            case 2:
                mLeaveReasonContainer.setBackground(getResources().getDrawable(R.drawable.card_style,null));
                selectedItem = getListinPopUp().get(position);
                mLeaveReasontext.setText(selectedItem);
                mSelectedLeaveReason = getSelectedItemFromDialog(selectedItem) ;
                if (mSelectedLeaveReason.getName().equalsIgnoreCase("others")) {
                    mReasonTextTil.setVisibility(View.VISIBLE);
                } else {
                    mReasonTextTil.setVisibility(View.GONE);
                }
                break;

        }

    }




    public LeaveType getSelectedLeaveTypeFromDialog(String leaveTypeValue){
        //List<MasterData> allCostCenter = MasterDataTable.getInstance(getActivity()).getAllByType(MasterData.TYPE.CostCentre);
        int position = 0;
        for(int iterator=0;iterator < leaveTypeList.size();iterator++ ){
            Log.d("TAG","list selection :: "+leaveTypeValue + "masterData :: "+leaveTypeList.get(iterator).toString());
            if(leaveTypeValue.equals(leaveTypeList.get(iterator).toString())){
                Log.d("TAG","Equals :: "+leaveTypeList.get(iterator) );
                position = iterator;
            }
        }
        Log.d("TAG","final return :: "+leaveTypeList.get(position));
        return leaveTypeList.get(position);
    }



    /** API used for submit action */
    public void submitLeaveRequest() {

        showProgressDialog("Submitting your leave...");
        Type type = new TypeToken<Leave>() {
        }.getType();

        long userid = GlobalData.gLoggedInUser.getEmployee().getId();
        int reasonId = mSelectedLeaveReason.getId();
        String leaveType = mSelectedLeaveType.getLeaveType();


        Gson gson = new GsonBuilder().serializeNulls().create();
        Map<String,Object> postPayload = new HashMap<>();
        postPayload.put("userId",userid);
        postPayload.put("startDate",mSelectedStartDate);
        postPayload.put("endDate",mSelectedEndDate);
        postPayload.put("reasonId", reasonId);
        postPayload.put("leaveTypeName", leaveType);
        postPayload.put("comments",mReasonDescriptionText.getText().toString());

        String payload = gson.toJson(postPayload);

        String URL = APIUrls.SUBMIT_LEAVE_REQ_URL;

        Log.d("URL",URL);

        RequestManager.addRequest(new GsonObjectRequest<Leave>(URL,HeaderManager.prepareMasterDataHeaders(this),payload,type,new VolleyErrorListener(this,this,NetworkEvents.SUBMIT_LEAVE_REQUEST)) {

            @Override
            public void deliverResponse(Leave response, Map<String, String> responseHeaders) {

                PreferenceUtil.saveCookies(NewLeaveRequestActivity.this, responseHeaders);
                updateUi(true,NetworkEvents.SUBMIT_LEAVE_REQUEST,response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        },AppConstants.REQUEST_TIMEOUT_AVG);
    }



    public void getLeavebalance() {

        Type type = new TypeToken<Map>() {
        }.getType();

        RequestManager.addRequest(new GsonObjectRequest<Object>(APIUrls.LEAVE_BAL_URL,null,type,new VolleyErrorListener(this,this,NetworkEvents.MY_LEAVES_BALANCE)) {

            @Override
            public void deliverResponse(Object response, Map<String, String> responseHeaders) {

                updateUi(true,NetworkEvents.MY_LEAVES_BALANCE,response);
            }

        },AppConstants.REQUEST_TIMEOUT_AVG);
    }

    public void getAppliedLeaveDays(long startDate,long endDate) {

        showProgressDialog("Calculating number of days");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        Log.d("TAG","START DATE : "+startDate);
        Log.d("TAG","END DATE :"+endDate);

        Log.d("TAG","START DATE WITH TIME: "+startDate*1000);
        Log.d("TAG","END DATE WITH TIME:"+endDate*1000);


        RequestManager.addRequest(new GsonObjectRequest<List>(APIUrls.CALCULATE_LEAVE_DAYS + "?startDate=" + startDate + "&endDate=" + endDate, null, type, new VolleyErrorListener(this, this, NetworkEvents.CALCULATE_NO_DAYS)) {

            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true,NetworkEvents.CALCULATE_NO_DAYS,response);
            }

        },AppConstants.REQUEST_TIMEOUT_AVG);
    }


    private void updateAppliedLeaveDays(int noOfDays) {

        mAppliedLeavesDaysContainer.setVisibility(View.VISIBLE);
        if (noOfDays == 1) {
            mTotalDaystext.setText(String.valueOf(noOfDays) + " DAY");
        } else if (noOfDays == 0) {
            mTotalDaystext.setText(String.valueOf(noOfDays) + " DAYS");
        } else {
            mTotalDaystext.setText(String.valueOf(noOfDays) + " DAYS");
        }

    }


    private boolean validateForm() {

        boolean isFromDateValid = validateFromDate();
        boolean isToDateValid = validateToDate();
        boolean isValidLeaveReason = validateLeaveReason();
        boolean isValidReasonComment = validateReasonComment();
        return isFromDateValid  && isToDateValid && isValidLeaveReason && isValidReasonComment;
    }

    private boolean validateFromDate() {

        if (mFromDateText.getText().toString().length() > 0) {
            mFromDateContainer.setBackground(getResources().getDrawable(R.drawable.card_style,null));
            return true;
        } else {
            mFromDateContainer.setBackground(getResources().getDrawable(R.drawable.card_style_error,null));
            return false;
        }
    }


    private boolean validateToDate() {

        if (mToDateText.getText().toString().length() > 0) {
            mToDateContainer.setBackground(getResources().getDrawable(R.drawable.card_style,null));
            return true;
        } else {
            mToDateContainer.setBackground(getResources().getDrawable(R.drawable.card_style_error,null));
            return false;
        }
    }



    private boolean validateLeaveReason() {

        if (mLeaveReasontext.getText().toString().length() > 0) {
            mLeaveReasonContainer.setBackground(getResources().getDrawable(R.drawable.card_style,null));
            return true;
        } else {
            mLeaveReasonContainer.setBackground(getResources().getDrawable(R.drawable.card_style_error,null));
            return false;
        }
    }



    private boolean validateReasonComment() {

        if (mSelectedLeaveReason!= null && mSelectedLeaveReason.getName().equalsIgnoreCase("others")) {

            if (mReasonDescriptionText.getText().toString().length() > 0) {
                mReasonDescriptionText.setBackground(getResources().getDrawable(R.drawable.card_style, null));
                return true;
            } else {
                mReasonDescriptionText.setBackground(getResources().getDrawable(R.drawable.card_style_error, null));
                return false;
            }
        }
        else  {
            mReasonDescriptionText.setBackground(getResources().getDrawable(R.drawable.card_style, null));
            return true;
        }
    }




}




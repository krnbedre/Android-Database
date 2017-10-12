package com.dhdigital.lms.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhdigital.lms.R;
import com.dhdigital.lms.adapters.CustomExpandableListAdapter;
import com.dhdigital.lms.adapters.ExpandableListDataPump;
import com.dhdigital.lms.db.MasterDataTable;
import com.dhdigital.lms.fragments.PieChartFragment;
import com.dhdigital.lms.glide.HeaderLoader;
import com.dhdigital.lms.modal.DashBoardModal;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.Files;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.Holiday;
import com.dhdigital.lms.modal.LeaveEntitlement;
import com.dhdigital.lms.modal.LeaveType;
import com.dhdigital.lms.modal.MasterData;
import com.dhdigital.lms.modal.MonthWiseLeave;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.AppUtil;
import com.dhdigital.lms.util.CircleTransform;
import com.dhdigital.lms.util.PreferenceUtil;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.activity.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.dhdigital.lms.adapters.CustomExpandableListAdapter.LOG_OUT;
import static com.dhdigital.lms.adapters.CustomExpandableListAdapter.MY_LEAVES;
import static com.dhdigital.lms.adapters.CustomExpandableListAdapter.NEW_LEAVE_REQUEST;
import static com.dhdigital.lms.adapters.CustomExpandableListAdapter.PERSONAL_TASK;

/**
 * Created by Kiran Bedre on 6/15/2016.
 */
public class LandingPageActivity extends BaseActivity implements View.OnClickListener{

    private static Map<Integer, MasterData.TYPE> masterDataTypeMap = new HashMap<Integer, MasterData.TYPE>();
    private static boolean masterDataFetchStatus = false;

    static {
        masterDataTypeMap.put(NetworkEvents.GET_MASTER_DATA_LEAVE_ENTITLEMENT, MasterData.TYPE.LeaveEntitlement);
        masterDataTypeMap.put(NetworkEvents.GET_MASTER_DATA_LEAVE_TYPE, MasterData.TYPE.LeaveType);
        masterDataTypeMap.put(NetworkEvents.GET_MASTER_DATA_LEAVE_REASON, MasterData.TYPE.LeaveReason);
        masterDataTypeMap.put(NetworkEvents.GET_MASTER_DATA_TEAM, MasterData.TYPE.Team);
        masterDataTypeMap.put(NetworkEvents.GET_MASTER_DATA_HOLIDAY, MasterData.TYPE.Holiday);
        masterDataTypeMap.put(NetworkEvents.GET_MASTER_DATA_REJECT_REASON, MasterData.TYPE.rejectReason);


    }

    private final int MASTER_DATA_TYPE_COUNT = masterDataTypeMap.size();       // This is how many requests are there to fetch MasterData
    private final int MY_TR_EC_DETAIL = 0x656;
    private DrawerLayout mDrawerLayout;
    private ExpandableListView expandableListView;
    private LinkedHashMap<String, List<String>> expandableListDetail;
    private ArrayList<String> expandableListTitle = new ArrayList<String>();
    private CustomExpandableListAdapter expandableListAdapter;
    private int lastExpandedPosition = -1;
    private String mLastLoggedInUser = null;

    private TextView userName, mUserIDTextView;
    private String loggedInUserName;
    private ImageView mUserIcon;
    private Context mContext;
    private int numberOfResMasterDataReq = 0;
    private FrameLayout mChartContainer;
    private ArrayList<MonthWiseLeave> monthWiseLeaveList = new ArrayList<>();
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private List<LeaveType> leaveTypeList = new ArrayList<LeaveType>();

    private AppCompatSpinner leaveTypeSpinner, employeeSpinner;
    private LeaveType mSelectedLeaveType = null;
    private TextView mleaveTypeText, mLeaveBalanceText;
    private DashBoardModal mDashBoardData;


    public static int getThemeAccentColor (final Context context) {

        final TypedValue value = new TypedValue();
        context.getTheme ().resolveAttribute (R.attr.colorAccent, value, true);
        Log.d(AppConstants.APP_TAG,"Color Accent : "+value.data);
        context.getTheme ().resolveAttribute (R.attr.colorPrimary, value, true);
        Log.d(AppConstants.APP_TAG,"colorPrimary : "+value.data);
        context.getTheme ().resolveAttribute (R.attr.colorPrimaryDark, value, true);
        Log.d(AppConstants.APP_TAG,"CcolorPrimaryDark : "+value.data);

        return value.data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(AppConstants.APP_TAG,"-onCreate-"+getLocalClassName());
        setContentView(R.layout.landing_page_layout);
        overridePendingTransition(R.anim.do_not_move_anime,R.anim.do_not_move_anime);
        mContext = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.baselayout);
        mChartContainer = (FrameLayout) findViewById(R.id.charts_container);
        leaveTypeSpinner = (AppCompatSpinner) findViewById(R.id.leave_type_spn);
        employeeSpinner = (AppCompatSpinner) findViewById(R.id.employee_spn);
        mLeaveBalanceText = (TextView) findViewById(R.id.leave_bal_txt);


        ViewTreeObserver viewTreeObserver = mDrawerLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mDrawerLayout.closeDrawers();
                    //circularRipple(mDrawerLayout);
                    mDrawerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        buildToolBar();
        loadWidgets();
        buildDrawerItems();
        getThemeAccentColor(this);

        //if (MasterDataTable.getInstance(mContext).getRefreshStatus() == 0)
        refreshMasterData();


    }


    private void loadWidgets(){

        ImageView imageView = (ImageView) findViewById(R.id.userDispIcon);

        Files fileUpload = GlobalData.gLoggedInUser.getEmployee().getFileUpload();
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
                    .into(imageView);


        }


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


    private void instantiateEmployeeSpinner() {


        List<String> emp_adapterlist = new ArrayList<String>();
        final List<String> employeeIds = new ArrayList<String>();
        emp_adapterlist.add(0, "Self");
        employeeIds.add(0, String.valueOf(GlobalData.gLoggedInUser.getEmployee().getId()));
        for (int i = 0; i < employeeList.size(); i++) {
            emp_adapterlist.add(i + 1, employeeList.get(i).getCompleteName());
            employeeIds.add(i + 1, String.valueOf(employeeList.get(i).getId()));
        }
        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        ArrayAdapter emp_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, emp_adapterlist);
        emp_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        employeeSpinner.setAdapter(emp_adapter);
        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    findViewById(R.id.leave_bal_container).setVisibility(View.INVISIBLE);
                } else {
                    findViewById(R.id.leave_bal_container).setVisibility(View.VISIBLE);
                }
                requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()), employeeIds.get(position));
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

        mLeaveBalanceText.setText(mSelectedLeaveType.getBalance() + "/" + (float) entitlement.getCount());
    }




    private void loadChart() {

        if (null != mChartContainer) {

            PieChartFragment fragment = new PieChartFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("MONTHWISE_LEAVES",monthWiseLeaveList);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.charts_container, fragment).commit();
        }
    }




    /** This API is used for building the drawer Username and enabling listeners*/
    private void buildDrawerItems(){
        userName = (TextView) findViewById(R.id.textView_username);
        mUserIcon = (ImageView) findViewById(R.id.userDispIcon);
        mUserIDTextView = (TextView) findViewById(R.id.textView_userId);


        Log.d("TAG", "User details USER::" );
        if (GlobalData.gLoggedInUser != null && GlobalData.gLoggedInUser.getEmployee() != null) {
            loggedInUserName = GlobalData.gLoggedInUser.getEmployee().getFirstName() + " " + GlobalData.gLoggedInUser.getEmployee().getLastName();
            Log.d("TAG", "User details USER::" + loggedInUserName);
            userName.setText(loggedInUserName);
            mUserIDTextView.setText("Employee Id: " + String.valueOf(GlobalData.gLoggedInUser.getEmployee().getId()));
        }
        buildDraweList();
    }

    private void buildDraweList() {

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setDivider(getDrawable(android.R.drawable.divider_horizontal_dim_dark));
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                switch (groupPosition) {
                    case NEW_LEAVE_REQUEST:
                        View title = findViewById(R.id.expandedListItem);
                        Intent newLeaveReq = new Intent(mContext, NewLeaveRequestActivity.class);
                        startActivity(newLeaveReq);
                        break;
                    case MY_LEAVES:
                        Intent myLeavesAct = new Intent(mContext, MyLeavesTabActivity.class);
                        startActivity(myLeavesAct);
                        break;
                    case PERSONAL_TASK:
                        Intent myTasks = new Intent(mContext, ApproverTasksActivity.class);
                        startActivity(myTasks);
                        break;
                    case LOG_OUT:
                        onLogoutBtnClicked();
                        break;
                }

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    //AnimUtil.collapseItem(expandableListView.getChildAt(lastExpandedPosition),expandableListView.getChildAt(groupPosition));
                    expandableListView.collapseGroup(lastExpandedPosition);
                }

                mDrawerLayout.closeDrawers();


                lastExpandedPosition = groupPosition;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View childView,
                                        int groupPosition, int childPosition, long id) {

                switch (expandableListDetail
                        .get(expandableListTitle
                                .get(groupPosition))
                        .get(childPosition)){



                }

                return false;
            }
        });

       mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View userTitle = findViewById(R.id.textView_username);
                View userIcon = findViewById(R.id.userDispIcon);
                View banner = findViewById(R.id.imageView_bg_splash);

            }
        });
    }

    /** API used for LogOut action for LogOut Option*/
    public void onLogoutBtnClicked() {


        mDrawerLayout.closeDrawers();
        AppUtil.showSnackBar(findViewById(R.id.baselayout), "Logged Out Successfully", Color.parseColor("#259259"));
        AppUtil.goToLoginScreen(mContext);
        LandingPageActivity.this.finish();

        /*Type type = new TypeToken<String>() {
        }.getType();
        RequestManager.addRequest(new GsonObjectRequest<String>(APIUrls.LOGOUT_URL, PreferenceUtil.getCookie(this), null, type, new VolleyErrorListener(this, this, NetworkEvents.INVALIDATE_USER)) {
            @Override
            public void deliverResponse(String response, Map<String, String> responseHeaders) {
                //PreferenceUtil.clearCookies(LandingPageActivity.this);
                //updateUi(true, NetworkEvents.INVALIDATE_USER, response);

            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);*/

    }

    /** API used to initiate the Tool bar items*/
    private void buildToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(getString(R.string.title_activity_landing_page));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.baselayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    /**
     * API to Update UI with respect to LIVE actions like Inbox msgs
     * @param status
     * @param actionID
     * @param serviceResponse
     */
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        Log.i("TAG", "@UMESH updateUi, serviceResponse: " + serviceResponse);
        switch(actionID){

            case NetworkEvents.INVALIDATE_USER: // on Logout response
                if (status && serviceResponse instanceof String) {
                    String response = serviceResponse.toString();
                    if ("SUCCESS".equals(response)) {
                        AppUtil.goToLoginScreen(mContext);
                        this.finish();
                    } else {
                        PreferenceUtil.clearCookies(this);
                        AppUtil.goToLoginScreen(mContext);
                        this.finish();
                    }
                }
                break;

            case NetworkEvents.GET_MASTER_DATA_HOLIDAY:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    MasterDataTable.getInstance(mContext).insertHolidayList((List<Holiday>) serviceResponse, MasterData.TYPE.Holiday);
                    numberOfResMasterDataReq++;
                }
                break;
            case NetworkEvents.GET_MASTER_DATA_LEAVE_ENTITLEMENT:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    MasterDataTable.getInstance(mContext).insertLeaveEntitlement((List<LeaveEntitlement>) serviceResponse, MasterData.TYPE.LeaveEntitlement);
                    numberOfResMasterDataReq++;
                }
                break;
            case NetworkEvents.GET_MASTER_DATA_LEAVE_REASON:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    MasterDataTable.getInstance(mContext).insertLeaveReasonData((List<MasterData>) serviceResponse, MasterData.TYPE.LeaveReason);
                    numberOfResMasterDataReq++;
                }
                break;
            case NetworkEvents.GET_MASTER_DATA_LEAVE_TYPE:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    MasterDataTable.getInstance(mContext).insertLeaveTypes((List<LeaveType>) serviceResponse, MasterData.TYPE.LeaveType);
                    numberOfResMasterDataReq++;
                }
                break;
            case NetworkEvents.GET_MASTER_DATA_TEAM:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    MasterDataTable.getInstance(mContext).insertTeamData((List<MasterData>) serviceResponse, MasterData.TYPE.Team);
                    numberOfResMasterDataReq++;
                }
                break;
            case NetworkEvents.GET_MASTER_DATA_REJECT_REASON:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    MasterDataTable.getInstance(mContext).insertRejectReason((List<MasterData>) serviceResponse, MasterData.TYPE.Team);
                    numberOfResMasterDataReq++;
                }
                break;

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
            case NetworkEvents.GET_EMPLOYEES:
                removeProgressDialog();
                if (status && serviceResponse instanceof List<?>) {
                    //mDashBoardData = (DashBoardModal) serviceResponse;
                    employeeList = (ArrayList<Employee>) serviceResponse;
                    instantiateEmployeeSpinner();
                    //updateLeaveBalanceCount();
                }
                break;
            default:
                removeProgressDialog();


        }
    }

    @Override
    public void onEvent(int eventId, Object eventData) {
    }

    @Override
    public void getData(int actionID) {
    }

    /**
     * API used to launch other activities
     * @param className
     * @param key
     * @param value
     */
    private void launchActivity(Class className, String key, String value) {
        Intent intent = new Intent(this, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(key, value);
        this.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(AppConstants.APP_TAG,"-onStart-"+getLocalClassName());


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(AppConstants.APP_TAG,"-onResume-"+getLocalClassName());

        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        instantiateLeaveTypeSpinner();
        getEmployeesUnderLoggedInUser();
        //showProgressDialog("Setting up...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(AppConstants.APP_TAG,"-onPause-"+getLocalClassName());

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(AppConstants.APP_TAG,"-onStop-"+getLocalClassName());
    }





    /**
     * It fetches the Master data from server and store in local DB, so that it can be displayed at verious places over the application.
     */
    private void refreshMasterData() {
        numberOfResMasterDataReq = 0;
        masterDataFetchStatus = true;

        //if(!isMasterDataRefreshed()) {
            showProgressDialog("Setting up...");
            for (Map.Entry<Integer, MasterData.TYPE> pair : masterDataTypeMap.entrySet()) {
                Log.d("Trex", "Value :: " + pair.getValue().toString() + " pair :: " + pair.getKey().toString());
                if(pair.getKey().equals(NetworkEvents.GET_MASTER_DATA_LEAVE_ENTITLEMENT)) {
                    getLeaveEntitlementMasterData(pair.getValue().toString(), pair.getKey());
                } else if (pair.getKey().equals(NetworkEvents.GET_MASTER_DATA_LEAVE_REASON)) {
                    getLeaveReasonMasterData(pair.getValue().toString(), pair.getKey());
                }
                else if (pair.getKey().equals(NetworkEvents.GET_MASTER_DATA_TEAM)) {
                    getTeamMasterData(pair.getValue().toString(), pair.getKey());
                }
                else if (pair.getKey().equals(NetworkEvents.GET_MASTER_DATA_LEAVE_TYPE)) {
                    getLeaveTypeMasterData(pair.getValue().toString(), pair.getKey());
                }
                else if (pair.getKey().equals(NetworkEvents.GET_MASTER_DATA_HOLIDAY)) {

                    getHolidayMasterData(pair.getValue().toString(), pair.getKey());
                } else if (pair.getKey().equals(NetworkEvents.GET_MASTER_DATA_REJECT_REASON)) {
                    getRejectReason(pair.getValue().toString(), pair.getKey());
                }
            }
       //}
    }

    private void getLeaveEntitlementMasterData(String name,final  int event) {
        Type listType = new TypeToken<ArrayList<LeaveEntitlement>>() {
        }.getType();
        showProgressDialog("Setting up...");
        RequestManager.addRequest(new GsonObjectRequest<List>(
                APIUrls.MASTER_DATA_URL + name,
                HeaderManager.prepareMasterDataHeaders(LandingPageActivity.this),
                null,
                listType,
                new VolleyErrorListener(LandingPageActivity.this, LandingPageActivity.this, event)
        ) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }


    private void getLeaveReasonMasterData(String name,final  int event) {
        Type listType = new TypeToken<ArrayList<MasterData>>() {
        }.getType();
        showProgressDialog("Setting up...");


        RequestManager.addRequest(new GsonObjectRequest<List>(
                APIUrls.MASTER_DATA_URL + name,
                HeaderManager.prepareMasterDataHeaders(LandingPageActivity.this),
                null,
                listType,
                new VolleyErrorListener(LandingPageActivity.this, LandingPageActivity.this, event)
        ) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }


    private void getLeaveTypeMasterData(String name,final  int event) {
        Type listType = new TypeToken<ArrayList<LeaveType>>() {
        }.getType();
        showProgressDialog("Setting up...");


        RequestManager.addRequest(new GsonObjectRequest<List>(
                APIUrls.MASTER_DATA_URL + name,
                HeaderManager.prepareMasterDataHeaders(LandingPageActivity.this),
                null,
                listType,
                new VolleyErrorListener(LandingPageActivity.this, LandingPageActivity.this, event)
        ) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }


    private void getTeamMasterData(String name,final  int event) {
        Type listType = new TypeToken<ArrayList<MasterData>>() {
        }.getType();
        showProgressDialog("Setting up...");


        RequestManager.addRequest(new GsonObjectRequest<List>(
                APIUrls.MASTER_DATA_URL + name,
                HeaderManager.prepareMasterDataHeaders(LandingPageActivity.this),
                null,
                listType,
                new VolleyErrorListener(LandingPageActivity.this, LandingPageActivity.this, event)
        ) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }

    private void getRejectReason(String name,final  int event) {
        Type listType = new TypeToken<ArrayList<MasterData>>() {
        }.getType();
        showProgressDialog("Setting up...");


        RequestManager.addRequest(new GsonObjectRequest<List>(
                APIUrls.MASTER_DATA_URL + name,
                HeaderManager.prepareMasterDataHeaders(LandingPageActivity.this),
                null,
                listType,
                new VolleyErrorListener(LandingPageActivity.this, LandingPageActivity.this, event)
        ) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }

  /* private void executeGetProjectMasterData(String name,final  int event) {
        showProgressDialog("Setting up...");
        RequestManager.addRequest(new GsonObjectRequest<List>(
                APIUrls.MASTER_DATA_PROJECT_URL + name,
                HeaderManager.prepareMasterDataHeaders(LandingPageActivity.this),
                null,
                listType,
                new VolleyErrorListener(LandingPageActivity.this, LandingPageActivity.this, event)
        ) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }*/

    /**
     * To check if the master data is refreshed today or not
     *
     * @return true if refreshed today
     */
    private boolean isMasterDataRefreshed() {
        long lastRefTime = PreferenceUtil.getLastRefreshTimeForMasterData(mContext);
        return DateUtils.isToday(lastRefTime);
    }

    /**
     * API used to get the MasterData through Request
     * @param name
     * @param event
     */

    private void getHolidayMasterData(String name, final int event) {
        Type listType = new TypeToken<ArrayList<Holiday>>() {
        }.getType();
        showProgressDialog("Setting up...");
        RequestManager.addRequest(new GsonObjectRequest<List>(
                APIUrls.MASTER_DATA_URL + name,
                HeaderManager.prepareMasterDataHeaders(LandingPageActivity.this),
                null,
                listType,
                new VolleyErrorListener(LandingPageActivity.this, LandingPageActivity.this, event)
                                                    ) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {
                updateUi(true, event, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

        }

    }



    public void requestDashBoardData(String leaveTypeId,String userId) {

        String URL = APIUrls.USER_DASHBOARD;
        if (leaveTypeId == null) {
         if (userId == null) {

         }else {
             URL = URL + "?userId="+userId;
         }
        } else {
            URL = URL + "?leaveTypeId="+leaveTypeId;
            if (userId == null) {
            }else {
                URL = URL + "&userId="+userId;
            }
        }

        Type type = new TypeToken<ArrayList<MonthWiseLeave>>() {
        }.getType();


        Log.d("URL", URL);

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, null, type, new VolleyErrorListener(this, this, NetworkEvents.LEAVE_DASHBOARD)) {
            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true,NetworkEvents.LEAVE_DASHBOARD,response);

            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
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

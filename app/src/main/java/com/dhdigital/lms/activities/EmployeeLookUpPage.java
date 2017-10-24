package com.dhdigital.lms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.adapters.ListAdapter;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.MasterData;
import com.dhdigital.lms.modal.UserRole;
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
 * Created by admin on 18/10/17.
 */

public class EmployeeLookUpPage extends BaseActivity {

    ListAdapter listAdapter;
    private AppCompatSpinner mTeamSpinner;
    private ListView mEmployeeListView;
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<MasterData> teamList = new ArrayList<>();
    private String mSelectedTeamId = null;
    private String mSelectedEmpId = null;
    private ArrayAdapter team_adapter;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_look_up_page);
        mTeamSpinner = (AppCompatSpinner) findViewById(R.id.employee_spn);
        mEmployeeListView = (ListView) findViewById(R.id.list_view);
        listAdapter = new ListAdapter(this, employeeList);
        mEmployeeListView.setAdapter(listAdapter);
        initToolBar();
        getMyTeams();
        instantiateSearchListener();
        mEmployeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent data = new Intent();
                data.putExtra(AppConstants.EMP_FILTER, String.valueOf(employeeList.get(position).getId()));
                data.putExtra(AppConstants.EMP_NAME_FILTER, employeeList.get(position).getCompleteName());
                setResult(AppConstants.EMPLOYEE_FILTER_INTENT, data);
                finish();
            }
        });
    }

    private void instantiateSearchListener() {
        searchField = (EditText) findViewById(R.id.search_box);
        searchField.setHint("Search Employee");
        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.getFilter().filter(s.toString());
                ArrayList<Employee> adapter = new ArrayList<>();
                for (Employee employee : employeeList) {
                    if (employee.getFirstName().toLowerCase().contains(s.toString().toLowerCase())) {
                        adapter.add(employee);
                    }
                }
                listAdapter = new ListAdapter(EmployeeLookUpPage.this, adapter);
                mEmployeeListView.setAdapter(listAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void instantiateTeamSpinner() {

        Employee employee = GlobalData.gLoggedInUser.getEmployee();
        if (null != employee) {
            List<String> team_adapterlist = new ArrayList<String>();

            team_adapterlist.clear();
            if (GlobalData.gLoggedInUser.getUserRoles() != null) {

                for (UserRole role : GlobalData.gLoggedInUser.getUserRoles()
                        ) {
                    if (role.getAuthority().equalsIgnoreCase("SENIOR_MANAGEMENT")) {
                        MasterData data = new MasterData();
                        data.setName("Reporting to me");
                        teamList.add(data);
                    }
                }
            }

            for (int i = 0; i < teamList.size(); i++) {
                team_adapterlist.add(i, teamList.get(i).getName());

            }

            //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
            team_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, team_adapterlist);
            team_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
            mTeamSpinner.setAdapter(team_adapter);
            mTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    if (position == teamList.size()) {
                        getEmployeesUnderMe();
                    } else {

                        mSelectedTeamId = String.valueOf(teamList.get(position).getId());
                        getEmployeesUnderteam();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });
        }
    }


    private void getMyTeams() {

        Type type = new TypeToken<ArrayList<MasterData>>() {
        }.getType();

        String URL = APIUrls.GET_TEAMS;

        Log.d("URL", URL);

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, HeaderManager.prepareMasterDataHeaders(this), null, type, new VolleyErrorListener(this, this, NetworkEvents.GET_TEAMS)) {

            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true, NetworkEvents.GET_TEAMS, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }

    private void getEmployeesUnderteam() {

        Type type = new TypeToken<ArrayList<Employee>>() {
        }.getType();

        String URL = APIUrls.GET_EMPLOYEES_FOR_TEAM + "?teamId=" + mSelectedTeamId;

        Log.d("URL", URL);

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, HeaderManager.prepareMasterDataHeaders(this), null, type, new VolleyErrorListener(this, this, NetworkEvents.GET_EMPLOYEES)) {

            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true, NetworkEvents.GET_EMPLOYEES, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }


    private void getEmployeesUnderMe() {

        Type type = new TypeToken<ArrayList<Employee>>() {
        }.getType();

        String URL = APIUrls.GET_EMPLOYEES;

        Log.d("URL", URL);

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, HeaderManager.prepareMasterDataHeaders(this), null, type, new VolleyErrorListener(this, this, NetworkEvents.GET_EMPLOYEES)) {

            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true, NetworkEvents.GET_EMPLOYEES, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }


    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        switch (actionID) {
            case NetworkEvents.GET_EMPLOYEES:
                if (status && serviceResponse instanceof List<?>) {
                    if (employeeList.size() > 0) {
                        employeeList.clear();
                    }
                    employeeList.clear();
                    ArrayList<Employee> employeesList = (ArrayList<Employee>) serviceResponse;
                    employeeList.addAll(employeesList);
                    listAdapter.notifyDataSetChanged();
                }
                break;
            case NetworkEvents.GET_TEAMS:
                if (status && serviceResponse instanceof List<?>) {

                    if (teamList.size() > 0) {
                        teamList.clear();
                    }
                    teamList = (ArrayList<MasterData>) serviceResponse;
                    instantiateTeamSpinner();
                }
        }

    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView ivMyInbox = (ImageView) toolbar.findViewById(R.id.txv_summary);
        ivMyInbox.setVisibility(View.GONE);

        tvTitle.setText("Select Employee");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }
}

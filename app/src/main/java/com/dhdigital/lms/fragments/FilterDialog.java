package com.dhdigital.lms.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.MasterData;
import com.dhdigital.lms.modal.UserRole;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.dhdigital.lms.util.AppConstants;
import com.google.gson.reflect.TypeToken;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.kelltontech.volley.ui.IScreen;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 12/10/17.
 */

public class FilterDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnFilterAppliedListener mOnFilterAppliedListener;
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private ArrayList<MasterData> teamList = new ArrayList<>();
    private AppCompatSpinner teamSpinner, employeeSpinner;
    private String mSelectedTeamId = null;
    private String mSelectedEmpId = null;
    private IScreen mIScreenBinder;
    private ArrayAdapter team_adapter, emp_adapter;


    public FilterDialog(Context context, IScreen iscreen, OnFilterAppliedListener listener) {
        super(context, R.style.filter_floating_screen_dialog);
        this.mContext = context;
        this.teamList = teamList;
        this.mIScreenBinder = iscreen;
        this.mOnFilterAppliedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.filter_dialog_layout);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //instantiateEmployeeSpinner();
        employeeSpinner = (AppCompatSpinner) findViewById(R.id.employee_spn);
        teamSpinner = (AppCompatSpinner) findViewById(R.id.team_spn);
        getMyTeams();

        employeeSpinner.setVisibility(View.VISIBLE);
        findViewById(R.id.textView_clear).setOnClickListener(this);
        findViewById(R.id.button_apply).setOnClickListener(this);
    }

    private void instantiateEmployeeSpinner() {


        List<String> emp_adapterlist = new ArrayList<String>();


        final List<String> employeeIds = new ArrayList<String>();
        emp_adapterlist.add(0, "-");
        employeeIds.add(0, "_");
        for (int i = 0; i < employeeList.size(); i++) {
            emp_adapterlist.add(i + 1, employeeList.get(i).getCompleteName());
            employeeIds.add(i + 1, String.valueOf(employeeList.get(i).getId()));
        }
        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        emp_adapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, emp_adapterlist);
        emp_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        employeeSpinner.setAdapter(emp_adapter);
        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 0) {

                } else {
                    mSelectedEmpId = employeeIds.get(position);
                }
                // mSelectedTeamId = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    private void instantiateTeamSpinner() {

        Employee employee = GlobalData.gLoggedInUser.getEmployee();
        if (null != employee) {
            List<String> team_adapterlist = new ArrayList<String>();

            if (GlobalData.gLoggedInUser.getUserRoles() != null) {

                for (UserRole role : GlobalData.gLoggedInUser.getUserRoles()
                        ) {
                    if (role.getAuthority().equalsIgnoreCase("SENIOR_MANAGEMENT")) {
                        MasterData data = new MasterData();
                        data.setName("My Team");
                        teamList.add(data);
                    }
                }
            }

            for (int i = 0; i < teamList.size(); i++) {
                team_adapterlist.add(i, teamList.get(i).getName());

            }

            //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
            team_adapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, team_adapterlist);
            team_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
            teamSpinner.setAdapter(team_adapter);
            teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        }

    }

    private void onClearBtnClicked() {
        mSelectedEmpId = null;
        mSelectedTeamId = null;
        //instantiateEmployeeSpinner();
        instantiateTeamSpinner();
        dismiss();
    }

    private void onApplySearchBtnClicked() {
        if (mOnFilterAppliedListener != null) {

            mOnFilterAppliedListener.onFilterApplied(mSelectedEmpId, mSelectedTeamId);
        }
        dismiss();
    }

    private void getMyTeams() {

        Type type = new TypeToken<ArrayList<MasterData>>() {
        }.getType();

        String URL = APIUrls.GET_TEAMS;

        Log.d("URL", URL);

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, null, type, new VolleyErrorListener(mIScreenBinder, mContext, NetworkEvents.GET_TEAMS)) {

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

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, null, type, new VolleyErrorListener(mIScreenBinder, mContext, NetworkEvents.GET_EMPLOYEES)) {

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

        RequestManager.addRequest(new GsonObjectRequest<List>(URL, null, type, new VolleyErrorListener(mIScreenBinder, mContext, NetworkEvents.GET_EMPLOYEES)) {

            @Override
            public void deliverResponse(List response, Map<String, String> responseHeaders) {

                updateUi(true, NetworkEvents.GET_EMPLOYEES, response);
            }

        }, AppConstants.REQUEST_TIMEOUT_AVG);

    }

    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        switch (actionID) {
            case NetworkEvents.GET_EMPLOYEES:
                if (status && serviceResponse instanceof List<?>) {
                    if (employeeList.size() > 0) {
                        employeeList.clear();
                    }
                    employeeList = (ArrayList<Employee>) serviceResponse;
                    employeeSpinner.setVisibility(View.VISIBLE);
                    instantiateEmployeeSpinner();
                    emp_adapter.notifyDataSetChanged();
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


    public interface OnFilterAppliedListener {
        public void onFilterApplied(String userId, String teamId);
    }

}

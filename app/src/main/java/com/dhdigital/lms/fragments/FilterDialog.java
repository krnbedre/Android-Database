package com.dhdigital.lms.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.MasterData;

import java.util.ArrayList;
import java.util.List;

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


    public FilterDialog(Context context, ArrayList<Employee> employeesList, ArrayList<MasterData> teamList, OnFilterAppliedListener listener) {
        super(context, R.style.floating_screen_dialog);
        this.mContext = context;
        this.employeeList = employeesList;
        this.teamList = teamList;
        this.mOnFilterAppliedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.filter_dialog_layout);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        instantiateEmployeeSpinner();
        instantiateTeamSpinner();
        findViewById(R.id.textView_clear).setOnClickListener(this);
        findViewById(R.id.button_apply).setOnClickListener(this);
    }

    private void instantiateEmployeeSpinner() {


        employeeSpinner = (AppCompatSpinner) findViewById(R.id.employee_spn);
        List<String> emp_adapterlist = new ArrayList<String>();
        final List<String> employeeIds = new ArrayList<String>();
        emp_adapterlist.add(0, "Self");
        employeeIds.add(0, String.valueOf(GlobalData.gLoggedInUser.getEmployee().getId()));
        for (int i = 0; i < employeeList.size(); i++) {
            emp_adapterlist.add(i + 1, employeeList.get(i).getCompleteName());
            employeeIds.add(i + 1, String.valueOf(employeeList.get(i).getId()));
        }
        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        ArrayAdapter emp_adapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, emp_adapterlist);
        emp_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        employeeSpinner.setAdapter(emp_adapter);
        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                mSelectedEmpId = employeeIds.get(position);
                mSelectedTeamId = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    private void instantiateTeamSpinner() {


        teamSpinner = (AppCompatSpinner) findViewById(R.id.team_spn);
        List<String> team_adapterlist = new ArrayList<String>();


        for (int i = 0; i < teamList.size(); i++) {
            team_adapterlist.add(i, teamList.get(i).getName());

        }
        //requestDashBoardData(String.valueOf(mSelectedLeaveType.getId()),null);
        ArrayAdapter team_adapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, team_adapterlist);
        team_adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        teamSpinner.setAdapter(team_adapter);
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                mSelectedTeamId = String.valueOf(teamList.get(position).getId());
                mSelectedEmpId = null;
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

        }

    }

    private void onClearBtnClicked() {
        mSelectedEmpId = null;
        mSelectedTeamId = null;
        instantiateEmployeeSpinner();
        instantiateTeamSpinner();
    }

    private void onApplySearchBtnClicked() {
        if (mOnFilterAppliedListener != null) {

            mOnFilterAppliedListener.onFilterApplied(mSelectedEmpId, mSelectedTeamId);
        }
        dismiss();
    }


    public interface OnFilterAppliedListener {
        public void onFilterApplied(String userId, String teamId);
    }
}

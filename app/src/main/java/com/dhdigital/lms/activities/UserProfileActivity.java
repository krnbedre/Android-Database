package com.dhdigital.lms.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhdigital.lms.R;
import com.dhdigital.lms.glide.HeaderLoader;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.Files;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.MasterData;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.util.CircleTransform;
import com.kelltontech.volley.ui.activity.BaseActivity;

import java.util.ArrayList;

/**
 * Created by admin on 16/10/17.
 */

public class UserProfileActivity extends BaseActivity {

    private EditText mFirstName, mLastName, mTeamName, mManagerName, mEmpId;
    private Button mChangePwdBtn;
    private ImageView mUserProfImg;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_prof_activity);
        initToolBar();
        mFirstName = (EditText) findViewById(R.id.firstname_value);
        mLastName = (EditText) findViewById(R.id.lastname_value);
        mTeamName = (EditText) findViewById(R.id.team_value);
        mManagerName = (EditText) findViewById(R.id.reportsto_value);
        mEmpId = (EditText) findViewById(R.id.emp_id_value);
        mChangePwdBtn = (Button) findViewById(R.id.change_pwd_button);
        mUserProfImg = (ImageView) findViewById(R.id.userDispIcon);
        populateUserData();
        mChangePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePwdIntent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
                startActivity(changePwdIntent);
            }
        });

        makeTransition();

    }

    private void makeTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFirstName.setTransitionName(getString(R.string.first_name));
            mUserProfImg.setTransitionName(getString(R.string.user_pic));


        }
    }

    private void initToolBar() {

        toolbar = (Toolbar) findViewById(R.id.in_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(getString(R.string.my_profile));
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

    private void populateUserData() {

        Employee loggedInEmp = GlobalData.gLoggedInUser.getEmployee();
        if (null != loggedInEmp) {
            Files fileUpload = loggedInEmp.getFileUpload();
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
                        .into(mUserProfImg);

            }


            mEmpId.setText(String.valueOf(loggedInEmp.getId()));
            mFirstName.setText(loggedInEmp.getFirstName());
            mLastName.setText(loggedInEmp.getLastName());
            mManagerName.setText(null != loggedInEmp.getManager() ? loggedInEmp.getManager().getCompleteName() : "-");
            mTeamName.setText(null != loggedInEmp.getTeam() ? loggedInEmp.getTeam().getName() : "-");
        }
    }

    private String getTeamNames(ArrayList<MasterData> listTeams) {
        String teamName = "";
        for (MasterData team : listTeams
                ) {

            teamName = teamName + "," + team.getName();
        }
        return teamName;
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

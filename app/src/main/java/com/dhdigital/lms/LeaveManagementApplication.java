package com.dhdigital.lms;

import android.content.Context;

import com.dhdigital.lms.db.MasterDataTable;
import com.kelltontech.volley.application.BaseApplication;
import com.kelltontech.volley.database.GenericDB;
import com.kelltontech.volley.ext.RequestManager;


import java.io.File;

/**
 * Created by admin on 27/09/17.
 */

public class LeaveManagementApplication extends BaseApplication {

    public static Context myAppContext;

    public enum TrackerName {
        APP_TRACKER,
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected void initialize() {
        myAppContext = getApplicationContext();
        String imageChachePath = "data/data/" + getString(R.string.app_name) + File.separator + "pics";
        RequestManager.initializeWith(this.getApplicationContext(), new RequestManager.Config(imageChachePath, 5242880, 4));

        GenericDB.DB_SQL_CREATE_TABLE_QUERIES.add(MasterDataTable.CREATE_LEAVE_ENTITLEMENT_TABLE);
        GenericDB.DB_SQL_CREATE_TABLE_QUERIES.add(MasterDataTable.CREATE_LEAVE_REASON);
        GenericDB.DB_SQL_CREATE_TABLE_QUERIES.add(MasterDataTable.CREATE_LEAVE_TYPE);
        GenericDB.DB_SQL_CREATE_TABLE_QUERIES.add(MasterDataTable.CREATE_HOLIDAY_TABLE);
        GenericDB.DB_SQL_CREATE_TABLE_QUERIES.add(MasterDataTable.CREATE_TEAM_TABLE);
        GenericDB.DB_SQL_CREATE_TABLE_QUERIES.add(MasterDataTable.CREATE_USER_DATA);

    }
}

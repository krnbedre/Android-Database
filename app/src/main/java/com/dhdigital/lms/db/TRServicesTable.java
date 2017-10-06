package com.dhdigital.lms.db;

import android.content.Context;

import com.kelltontech.volley.application.BaseApplication;
import com.kelltontech.volley.database.GenericDB;


/**
 * Created by Kiran Bedre on 14/12/15.
 * DarkHorse BOA
 * <p/>
 * Not being used this time, may be use in-case we need to save the TR with added services locally
 */
public class TRServicesTable {
    private final static String TABLE_NAME = TRServicesTable.class.getSimpleName();
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, travel_start_date INTEGER, travel_end_date INTEGER, travel_reason_json TEXT, trip_brief_desc TEXT, " +
                    "list_flight_request TEXT, list_train_request TEXT, list_bus_request TEXT, list_car_hire_request TEXT, list_taxi_cab_request TEXT, list_hotel_request TEXT, list_hotel_request TEXT)";

    private static TRServicesTable _instance;
    private GenericDB genericDB;

    public TRServicesTable(BaseApplication pApplication) {
        genericDB = pApplication.getGenericDB();
    }

    public static TRServicesTable getInstance(Context context) {
        if (_instance == null) {
            _instance = new TRServicesTable((BaseApplication) context.getApplicationContext());
        }
        return _instance;
    }

    /*public void insert(TravelRequest travelRequest) {
        ContentValues cv = new ContentValues();
        genericDB.insert(TABLE_NAME, cv);
    }*/

}

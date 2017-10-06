package com.dhdigital.lms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dhdigital.lms.modal.CityMasterData;
import com.dhdigital.lms.modal.Holiday;
import com.dhdigital.lms.modal.LeaveEntitlement;
import com.dhdigital.lms.modal.LeaveType;
import com.dhdigital.lms.modal.MasterData;
import com.dhdigital.lms.modal.TravelReason;
import com.kelltontech.volley.application.BaseApplication;
import com.kelltontech.volley.database.GenericDB;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Bedre on 7/12/15.
 * DarkHorse BOA
 */
public class MasterDataTable {

    private final static String USER_INFO_TABLE = "user_info";



    //public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, m_id INTEGER, code TEXT, name TEXT, type TEXT)";


    //NEW TABLES
    private final static String TABLE_NAME = MasterDataTable.class.getSimpleName();
    private final static String LEAVE_ENTITLEMENT_TABLE = "leave_entitlement";
    private final static String LEAVE_TYPE_TABLE = "leave_type";
    private final static String LEAVE_REASON_TABLE = "leave_reason";
    private final static String TEAM_TABLE = "team";
    private final static String HOLIDAY_TABLE = "holidays";

    //public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, m_id INTEGER, code TEXT, name TEXT, type TEXT)";
    public static final String CREATE_LEAVE_ENTITLEMENT_TABLE = "CREATE TABLE " + LEAVE_ENTITLEMENT_TABLE + " (m_id INTEGER PRIMARY KEY, code TEXT, name TEXT, type TEXT , count INTEGER, leave_type_id INTEGER , startDate INTEGER , endDate INTEGER)";
    public static final String CREATE_LEAVE_TYPE = "CREATE TABLE " + LEAVE_TYPE_TABLE + " (m_id INTEGER PRIMARY KEY, code TEXT, name TEXT, type TEXT , leave_type TEXT)";
    public static final String CREATE_LEAVE_REASON = "CREATE TABLE " + LEAVE_REASON_TABLE + "(m_id INTEGER PRIMARY KEY, code TEXT, name TEXT, type TEXT )";
    public static final String CREATE_TEAM_TABLE = "CREATE TABLE " + TEAM_TABLE + "(m_id INTEGER PRIMARY KEY, code TEXT, name TEXT, type TEXT )";
    public static final String CREATE_HOLIDAY_TABLE = "CREATE TABLE " + HOLIDAY_TABLE + " (m_id INTEGER PRIMARY KEY, code TEXT, name TEXT, type TEXT, holiday_date INTEGER )";





    public static final String CREATE_USER_DATA = "CREATE TABLE " + USER_INFO_TABLE + " (username TEXT, tenantName TEXT, status INTEGER)";
    private static MasterDataTable _instance;
    private GenericDB genericDB;
    private List<CityMasterData> dataList = new ArrayList<>();
    private String type;

    public MasterDataTable(BaseApplication pApplication) {
        genericDB = pApplication.getGenericDB();
    }

    public static MasterDataTable getInstance(Context context) {
        if (_instance == null) {
            _instance = new MasterDataTable((BaseApplication) context.getApplicationContext());
        }
        return _instance;
    }

    public void insertUserInfo(String username, String tenantName, int refresh_status) {
        int delStatus = genericDB.delete(USER_INFO_TABLE,null, null);
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("tenantName", tenantName);
        cv.put("status", refresh_status);
        genericDB.insert(USER_INFO_TABLE, cv);
    }

    public void updateUserInfo(String username, String tenantName, int refresh_status){

        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("tenantName", tenantName);
        cv.put("status", refresh_status);
        genericDB.update(USER_INFO_TABLE, cv,"tenantName=?", new String[]{tenantName});
    }


    public void insert(LeaveEntitlement _data, MasterData.TYPE type) {
        ContentValues cv = new ContentValues();
        cv.put("m_id", _data.getId());
        cv.put("code", _data.getCode());
        cv.put("name", _data.getName());
        cv.put("leave_type_id", _data.getLeaveType().getId());
        cv.put("type", type.toString());
        cv.put("count",_data.getCount());
        cv.put("startDate",_data.getStartDate());
        cv.put("endDate",_data.getEndDate());
        genericDB.insert(LEAVE_ENTITLEMENT_TABLE, cv);
    }

    public void insertLeaveEntitlement(List<LeaveEntitlement> dataList, MasterData.TYPE type) {
        int delStatus = genericDB.delete(LEAVE_ENTITLEMENT_TABLE,"type=?", new String[]{type.toString()});
        for (LeaveEntitlement data : dataList) {
            Log.d("LOG","insert API data : "+data +" type : "+type);
            insert(data, type);
        }
    }


    public void insert(LeaveType _data, MasterData.TYPE type) {
        ContentValues cv = new ContentValues();
        cv.put("m_id", _data.getId());
        cv.put("code", _data.getCode());
        cv.put("name", _data.getName());
        cv.put("leave_type",_data.getLeaveType());
        cv.put("type", type.toString());
        genericDB.insert(LEAVE_TYPE_TABLE, cv);
    }


    public void insertLeaveTypes(List<LeaveType> dataList, MasterData.TYPE type) {
        int delStatus = genericDB.delete(LEAVE_TYPE_TABLE,"type=?", new String[]{type.toString()});
        for (LeaveType data : dataList) {
            Log.d("LOG","insert API data : "+data +" type : "+type);
            insert(data, type);
        }
    }

    public void insertRowToLeaveReason(MasterData _data, MasterData.TYPE type) {
        ContentValues cv = new ContentValues();
        cv.put("m_id", _data.getId());
        cv.put("code", _data.getCode());
        cv.put("name", _data.getName());
        cv.put("type", type.toString());
        genericDB.insert(LEAVE_REASON_TABLE, cv);
    }

    public void insertLeaveReasonData(List<MasterData> dataList, MasterData.TYPE type) {
        int delStatus = genericDB.delete(LEAVE_REASON_TABLE,"type=?", new String[]{type.toString()});
        for (MasterData data : dataList) {
            Log.d("LOG","insert API data : "+data +" type : "+type);
            insertRowToLeaveReason(data, type);
        }
    }



    public void insertRowToTeam(MasterData _data, MasterData.TYPE type) {
        ContentValues cv = new ContentValues();
        cv.put("m_id", _data.getId());
        cv.put("code", _data.getCode());
        cv.put("name", _data.getName());
        cv.put("type", type.toString());
        genericDB.insert(TEAM_TABLE, cv);
    }

    public void insertTeamData(List<MasterData> dataList, MasterData.TYPE type) {
        int delStatus = genericDB.delete(TEAM_TABLE,"type=?", new String[]{type.toString()});
        for (MasterData data : dataList) {
            Log.d("LOG","insert API data : "+data +" type : "+type);
            insertRowToTeam(data, type);
        }
    }


    public void insert(Holiday _data, MasterData.TYPE type) {
        ContentValues cv = new ContentValues();
        cv.put("m_id", _data.getId());
        cv.put("code", _data.getCode());
        cv.put("name", _data.getName());
        cv.put("holiday_date",_data.getDate());
        cv.put("type", type.toString());
        genericDB.insert(HOLIDAY_TABLE, cv);
    }


    public void insertHolidayList(List<Holiday> dataList, MasterData.TYPE type) {
        int delStatus = genericDB.delete(HOLIDAY_TABLE,"type=?", new String[]{type.toString()});
        for (Holiday data : dataList) {
            Log.d("LOG","insert API data : "+data +" type : "+type);
            insert(data, type);
        }
    }





    public List<MasterData> getAllByTeamType(MasterData.TYPE type) {
        Cursor cursor = genericDB.select(TEAM_TABLE, null, "type=?", new String[]{type.toString()}, null);
        List<MasterData> mDataList = new ArrayList<MasterData>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }



    public List<MasterData> getAllByLeaveReasonType(MasterData.TYPE type) {
        Cursor cursor = genericDB.select(LEAVE_REASON_TABLE, null, "type=?", new String[]{type.toString()}, null);
        List<MasterData> mDataList = new ArrayList<MasterData>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }


    public List<MasterData> getAllByType(MasterData.TYPE type) {
        Cursor cursor = genericDB.select(TABLE_NAME, null, "type=?", new String[]{type.toString()}, null);
        List<MasterData> mDataList = new ArrayList<MasterData>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }

    private MasterData cursorToMasterData(Cursor cursor) {

        MasterData data = new MasterData();
        data.setId(cursor.getInt(cursor.getColumnIndex("m_id")));
        data.setCode(cursor.getString(cursor.getColumnIndex("code")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        return data;

    }


    public List<LeaveEntitlement> getAllByLeaveEntitlementType() {

        Cursor cursor = genericDB.select(LEAVE_ENTITLEMENT_TABLE, null, null, null, null);
        List<LeaveEntitlement> mDataList = new ArrayList<LeaveEntitlement>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToLeaveEntitlementData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }

    public List<LeaveType> getAllByleaveType() {
        Cursor cursor = genericDB.select(LEAVE_TYPE_TABLE, null, null, null, null);
        List<LeaveType> mDataList = new ArrayList<LeaveType>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToLeaveTypeMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }

    public LeaveEntitlement getLeaveEntitlement(int leaveTypeId){
        LeaveEntitlement data = new LeaveEntitlement();
        Cursor cursor = genericDB.select(LEAVE_ENTITLEMENT_TABLE, null, "leave_type_id=?", new String[]{Integer.toString(leaveTypeId)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                data = cursorToLeaveEntitlementData(cursor);
            }
        }
        return data;
    }




    public List<MasterData> getAllByLeaveReason() {
        Cursor cursor = genericDB.select(LEAVE_REASON_TABLE, null, null, null, null);
        List<MasterData> mDataList = new ArrayList<MasterData>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }


    public List<MasterData> getAllByTeamType() {
        Cursor cursor = genericDB.select(TEAM_TABLE, null, null, null, null);
        List<MasterData> mDataList = new ArrayList<MasterData>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }



    public List<Holiday> getAllHolidaysList() {

        Cursor cursor = genericDB.select(HOLIDAY_TABLE, null, null, null, null);
        List<Holiday> mDataList = new ArrayList<Holiday>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToHolidayTypeMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }


    public int getRefreshStatus(){
        Cursor cursor = genericDB.select(USER_INFO_TABLE, null, null, null, null);
        int status = 0;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                status =  cursor.getInt(cursor.getColumnIndex("status"));
            }
        }cursor.close();
        return status;
    }





    /*public List<CityMasterData> getAllByType(int country_id, int city_id, boolean toggleTravelType) {

        String TABLE_NAME = CITY_TABLE;
        String[] COLUMNS = null;
        String SELECTION = null;
        *//*
        if(toggleTravelType){
            SELECTION =   "city_country_id=? AND m_id!=?";
        }else{
            SELECTION =   "city_country_id!=? AND m_id!=?";
        }*//*
        SELECTION =   "m_id!=?";
        String[] SELECTION_ARGS = new String[]{Integer.toString(city_id)};
        String ORDER_BY = null;

        Cursor cursor = genericDB.select(CITY_TABLE, COLUMNS, SELECTION, SELECTION_ARGS, ORDER_BY);
        List<CityMasterData> mDataList = new ArrayList<CityMasterData>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToCityMasterData(cursor));
            }
            cursor.close();
        }
        return mDataList;
    }*/



    private LeaveEntitlement cursorToLeaveEntitlementData(Cursor cursor) {
        LeaveEntitlement data = new LeaveEntitlement();
        LeaveType leaveType = new LeaveType();
        data.setId(cursor.getInt(cursor.getColumnIndex("m_id")));
        data.setCode(cursor.getString(cursor.getColumnIndex("code")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setCount(cursor.getInt(cursor.getColumnIndex("count")));
        data.setStartDate(cursor.getLong(cursor.getColumnIndex("startDate")));
        data.setEndDate(cursor.getLong(cursor.getColumnIndex("endDate")));
        //data.setLeaveType(cursor.getString(cursor.getColumnIndex("has_guest_house")));
        leaveType = getLeaveTypeMasterData(cursor.getInt(cursor.getColumnIndex("m_id")));

        data.setLeaveType(leaveType);
        return data;
    }


    private LeaveType cursorToLeaveTypeMasterData(Cursor cursor) {
        LeaveType data = new LeaveType();
        data.setId(cursor.getInt(cursor.getColumnIndex("m_id")));
        data.setCode(cursor.getString(cursor.getColumnIndex("code")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setLeaveType(cursor.getString(cursor.getColumnIndex("leave_type")));
        return data;
    }


    private Holiday cursorToHolidayTypeMasterData(Cursor cursor) {
        Holiday data = new Holiday();
        data.setId(cursor.getInt(cursor.getColumnIndex("m_id")));
        data.setCode(cursor.getString(cursor.getColumnIndex("code")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setDate(cursor.getLong(cursor.getColumnIndex("holiday_date")));
        return data;
    }

    private TravelReason cursorToTravReasonMasterData(Cursor cursor) {
        TravelReason data = new TravelReason();
        data.setId(cursor.getInt(cursor.getColumnIndex("m_id")));
        data.setCode(cursor.getString(cursor.getColumnIndex("code")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setHangoutsSuggestion(cursor.getString(cursor.getColumnIndex("hangoutsSuggestion")));
        //countryClassData = getMasterData("Country",cursor.getInt(cursor.getColumnIndex("city_country_id")));
        //data.setCityCountry(countryClassData);
        return data;
    }





    private LeaveType getLeaveTypeMasterData(int id) {

        LeaveType data = new LeaveType();
        Cursor cursor = genericDB.select(LEAVE_TYPE_TABLE, new String[]{"m_id","code","name","leave_type"}, "m_id=?", new String[]{Integer.toString(id)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                data = cursorToLeaveTypeMasterData(cursor);
            }
        }
        return data;
    }


    private MasterData getMasterData(String type, int id){
        MasterData data = new MasterData();
        Cursor cursor = genericDB.select(TABLE_NAME, new String[]{"m_id","code","name"}, "type=? AND m_id=?", new String[]{type.toString(), Integer.toString(id)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                data = cursorToMasterData(cursor);
            }
        }
        return data;
    }

    /*private CityMasterData getCityMasterData(int id){
        CityMasterData data = new CityMasterData();
        Cursor cursor = genericDB.select(CITY_TABLE, null, "m_id=?", new String[]{Integer.toString(id)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                data = cursorToCityMasterData(cursor);
            }
        }
        return data;
    }

    public List<ProviderMasterData> getProviderFromLoc(int id) {
        ProviderMasterData data = new ProviderMasterData();
        Cursor cursor = genericDB.select(HOTEL_PROVIDER,null,"location_id=?",new String[]{Integer.toString(id)},null);
        List<ProviderMasterData> mDataList = new ArrayList<ProviderMasterData>();
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mDataList.add(cursorToProviderData(cursor));
            }
        }
        return  mDataList;
    }

    private ProviderMasterData cursorToProviderData(Cursor cursor) {
        ProviderMasterData data = new ProviderMasterData();
        data.setId(cursor.getInt(cursor.getColumnIndex("m_id")));
        data.setCode(cursor.getString(cursor.getColumnIndex("code")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setTentativeAmount(cursor.getDouble(cursor.getColumnIndex("tentativeAmount")));
        CityMasterData location = new CityMasterData();
        location = getCityMasterData(cursor.getInt(cursor.getColumnIndex("location_id")));
        data.setLocation(location);

        MasterData category = new MasterData();
        category = getMasterData("HotelCategory",cursor.getInt(cursor.getColumnIndex("category_id")));

        data.setCategory(category);
        return data;
    }*/
}

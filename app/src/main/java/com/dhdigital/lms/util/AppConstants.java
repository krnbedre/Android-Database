package com.dhdigital.lms.util;

/**
 * Created by Kiran Bedre on 6/11/15.
 * DarkHorse BOA
 */
public interface AppConstants {
    public static final String APP_TAG = "LeaveMgmt";
    public static final int TIMEOUT_SPLASH_SCREEN = 3000;

    public static final int REQUEST_TIMEOUT_MIN = 5000; // in millis
    public static final int REQUEST_TIMEOUT_AVG = 8000; // in millis
    public static final int REQUEST_TIMEOUT_MAX = 12000; // in millis

    public static final long MAX_ALLOWED_FILE_SIZE_TO_UPLOAD = 1 * 1024 * 1024;

    // error messages for user
    public static final String ERROR_MSG_N0_CONNECTION = "Please check your internet connection";
    public static final String ERROR_MSG_NETWORK = "There is some problem, please try again";
    public static final String ERROR_MSG_PARSE = "There is some problem, please try again";
    public static final String ERROR_MSG_SERVER = "Server is busy, please try after some time";
    public static final String ERROR_MSG_UNAUTHORIZED = "UnAuthorized";
    public static final String ERROR_MSG_TIMEOUT = "Request taken too much time, please try again";
    public static final String ERROR_MSG_GENERIC = "Something went wrong, please try again";






    public static final String PROGRESS_DIALOG_LOGGING_IN = "Logging in...";
    public static final String PROGRESS_DIALOG_LOADING = "Loading...";


    public static final String NAVIGATION = "navigation";

    public static final String APPLY_LEAVE = "Apply Leave";
    public static final String MY_LEAVES = "My Leaves";





    public static final String ACTION_DH_FINISH_SELF = "action_dh_finish_self";

    public static final int FRAGMENT_FLIGHT = 0;

    public static final String LOG_OUT = "Log out";


    public static final String APPROVE_LEAVE = "Approve Leave";

    public static final String VIEW_POLICIES = "Policies";
    public static final String CHANGE_PASSWORD = "Change Password";
    public static final String USER_NAME = "Employee User name";
    public static final String CALENDAR = "Holiday List";
    public static final String LOGGED_IN = "Logged In";
    public static final String LOGGED_OUT = "Logged Out";



    public static final int MY_LEAVES_FILTER_INTENT = 106;
    public static final int EMPLOYEE_FILTER_INTENT = 109;
    public static final String LEAVE_TYPE_FILTER = "leave type filter";
    public static final String EMP_FILTER = "emp filter";
    public static final String EMP_NAME_FILTER = "emp name filter";
    public static final String STATUS_FILTER = "status filter";
    public static final String START_DATE_FILTER = "start date filter";
    public static final String END_DATE_FILTER = "end date filter";
    public static final String APPROVER = "approver";
    public static final String REQUESTOR = "requestor";
    public static final String CUMULATIVE_LEAVE_CHART = "leaves chart";
    public static final String MONTH_LEAVE_CHART = "month leaves chart";

    public static final String APPROVED = "Approved";

    public static final String REJECTED = "Rejected";

    public static final String CANCELLED = "Cancelled";

    public static final String PENDING = "Pending";
    public static final String TAKEN = "Taken";
    public static final String LEAVES_FRAGMENT = "Leaves Fragment";
    public static final String NOTIFICATION = "notification";
    public static final String EXTRA_NOTIFICATION_ACTION = "extra_notification_action";



    // Adding status codes







}


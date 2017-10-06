package com.dhdigital.lms.util;

/**
 * Created by Kiran Bedre on 6/11/15.
 * DarkHorse BOA
 */
public interface AppConstants {
    public static final String APP_TAG = "TravelExpense";
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

    public static final int PAGE_SIZE_MY_TR_LIST = 20;
    public static final int PAGE_SIZE_MY_EC_LIST = 20;
    public static final int PAGE_SIZE_TR_DRAFT_LIST = 20;
    public static final int PAGE_SIZE_EC_DRAFT_LIST = 20;
    public static final int PAGE_SIZE_MY_TASK = 20;
    public static final int PAGE_SIZE_UNCLAIMED_TR = 10;
    public static final int PAGE_NUMBER = 0;




    public static final String MSG_MASTER_DATA_FETCH = "Please wait, refreshing some needed information...";

    String FROM_DATE = "fromDate";
    String TO_DATE = "toDate";
    String FROM_DATE_SET = "fromDateSet";
    String TO_DATE_SET = "toDateSet";
    String MONTH = "month";
    String YEAR = "year";
    String DAY = "day";

    public static final String MODULE_TRAVEL_REQUEST = "Travel Request";
    public static final String MODULE_EXPENSE_CLAIM = "Expense Claim";


    public static final String TR_DRAFT_NAME = "TRAVELDRAFT";
    public static final String EC_DRAFT_NAME = "EXPENSEDRAFT";
    public static final String TR = "TRAVEL";
    public static final String EC = "EXPENSE";

    public static final String EDITABLE_BOOLEAN = "editable";
    public static final String MULTIPLE_TR_EDIT_BOOLEAN = "multiple_tr_edit_boolean";

    public static final String PROGRESS_DIALOG_LOGGING_IN = "Logging in...";
    public static final String PROGRESS_DIALOG_ADDING_SERVICE_TO_TR = "Adding \"Service\" to your Travel Request";
    public static final String PROGRESS_DIALOG_ADDING_SERVICE_TO_EC = "Adding \"Service\" to your Expense Claim";
    public static final String PROGRESS_DIALOG_INITIATE_TR = "Submitting your Travel Request";
    public static final String PROGRESS_DIALOG_INITIATE_EC = "Submitting your Expense Claim";
    public static final String PROGRESS_DIALOG_WITHDRAW_TR = "Withdrawing Travel Request";
    public static final String PROGRESS_DIALOG_WITHDRAW_EC = "Withdrawing Expense Claim";
    public static final String PROGRESS_DIALOG_FATCH_APPROVER_EMP = "Fatching Approver employees";
    public static final String PROGRESS_DIALOG_LOADING = "Loading...";


    public static final String NAVIGATION = "navigation";
    public static final String NEW_TRAVEL_REQUEST = "New Travel Request";
    public static final String NEW_EXPENSE_CLAIM = "New Expense Claim";


    public static final String LEAVE = "Leave";
    public static final String NEW_LEAVE_REQUEST = "New Leave Request";
    public static final String MY_LEAVES = "My Leaves";



    /*My Draft String
   * */
    public static final String DRAFT_ID = "draft_id";
    public static final String MY_DRAFT = "My Drafts";

    /*My Request String
    * */
    public static final String MY_TRAVEL_REQUEST = "My Requests";
    public static final String MY_EXPENSE_CLAIM = "My Claims";
    public static final String REFERENCE_NO = "reference_no";


    public static final String MY_INBOX = "My Inbox";
    public static final String TASK_ID = "task_id";
    public static final String MY_INBOX_NAME = "my_inbox_name";

    public static final String SERVICE_SELECTION = "service_selection";

    public static final String ADD_MULTIPLE_TR = "add_multiple_tr";

    public static final String ACTION_DH_FINISH_SELF = "action_dh_finish_self";

    public static final int FRAGMENT_FLIGHT = 0;

    public static final String LOG_OUT = "Log out";

    public static final String TRAVEL = "Travel";
    public static final String EXPENSE = "Expense";
    public static final String REPORTS = "Reports";
    public static final String SETTINGS = "Settings";
    public static final String VIEW_REPORTS = "View Reports";
    public static final String TASK_TYPE = "task_type";

    public static final String PERSONAL_TASK = "Personal Tasks";
    public static final String TRAVEL_DESK_TASK = "Travel Desk Tasks";
    public static final String FINANCE_TASK = "Finance Tasks";
    public static final String VIEW_POLICIES = "Policies";
    public static final String CHANGE_PASSWORD = "Change Password";
    public static final String USER_NAME = "Employee User name";
    public static final String COST_CENTRE_SIZE = "no of cost centres";
    public static final String MODULE = "module";

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String TRAVEL_DESK_ROLE = "TRAVEL_DESK";
    public static final String FINANCE_ROLE = "FINANCE";

    public static final String TR_NOT_ADDED = "TR_NOT_ADDED";
    public static final String TR_ADDED = "TR_ADDED";


    //Constants for DBCORP stages
    public static final String STATE_TRAVEL_REQ = "TRAVEL_REQUEST";
    public static final String STATE_APPROVAL = "APPROVAL";
    public static final String STATE_TR_BOOKING = "TRAVEL_BOOKING";
    public static final String STATE_ACCOM_CONV = "ACCOMODATION_&_CONVEYENCE";
    public static final String STATE_COMPLETE = "COMPLETE";
    public static final String SERVICE_ADD = "SERVICE_ADDED";
    public static final String TRAVEL_DETAILS = "TRAVEL_DETAILS";
    public static final int SERVICE_ADDED = 6;

    public static final String LINKING_TR = "linking_tr";

    // Adding status codes







}


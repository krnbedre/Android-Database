package com.dhdigital.lms.adapters;

/**
 * Created by Parveen on 8/8/2016.
 */


import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.UserRole;
import com.dhdigital.lms.util.AppConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static LinkedHashMap<String, List<String>> getData() {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> leavesList = new ArrayList<String>();

        List<String> inbox = new ArrayList<String>();
        inbox.add(AppConstants.APPROVE_LEAVE);
     /*   if( null != GlobalData.gLoggedInUser) {
            List<Authority> authorityList = GlobalData.gLoggedInUser.getAuthorities();
            for (int iterator = 0; iterator < authorityList.size(); iterator++) {
                if (authorityList.get(iterator).getAuthority().equals(AppConstants.TRAVEL_DESK_ROLE)) {
                    inbox.add(AppConstants.TRAVEL_DESK_TASK);
                }
                if (authorityList.get(iterator).getAuthority().equals(AppConstants.FINANCE_ROLE)) {
                    inbox.add(AppConstants.FINANCE_TASK);
                }
            }
        }*/





        List<String> settings = new ArrayList<String>();
        settings.add(AppConstants.VIEW_POLICIES);
        List<String> logout = new ArrayList<>();
        // logout.add(AppConstants.LOG_OUT);


        expandableListDetail.put(AppConstants.APPLY_LEAVE, leavesList);
        expandableListDetail.put(AppConstants.MY_LEAVES, leavesList);

        List<UserRole> userRoles = GlobalData.gLoggedInUser.getUserRoles();
        for (int i = 0; i < userRoles.size(); i++) {
            if (userRoles.get(i).getAuthority().equalsIgnoreCase("APPROVER")) {
                expandableListDetail.put(AppConstants.APPROVE_LEAVE, leavesList);
            }
        }


        expandableListDetail.put(AppConstants.CALENDAR, leavesList);
        expandableListDetail.put(AppConstants.LOG_OUT, logout);
        return expandableListDetail;
    }
}
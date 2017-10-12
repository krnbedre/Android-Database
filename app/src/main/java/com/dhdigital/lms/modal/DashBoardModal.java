package com.dhdigital.lms.modal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 11/10/17.
 */

public class DashBoardModal {

    private ArrayList<MonthWiseLeave> leaveList = new ArrayList<>();
    private Map<String, Object> leaveEntitlement = new HashMap<>();
    private Map<String, Object> leaveBalance = new HashMap<>();

    public ArrayList<MonthWiseLeave> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(ArrayList<MonthWiseLeave> leaveList) {
        this.leaveList = leaveList;
    }

    public Map<String, Object> getLeaveEntitlement() {
        return leaveEntitlement;
    }

    public void setLeaveEntitlement(Map<String, Object> leaveEntitlement) {
        this.leaveEntitlement = leaveEntitlement;
    }

    public Map<String, Object> getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(Map<String, Object> leaveBalance) {
        this.leaveBalance = leaveBalance;
    }
}

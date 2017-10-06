package com.dhdigital.lms.modal;

/**
 * Created by admin on 05/10/17.
 */

public class LeaveModal {



    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    private String id;
    private long startDate;
    private long endDate;
    private int leaveCount = 0;
    private Leave leave;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getCount() {
        return leaveCount;
    }

    public void setCount(int count) {
        this.leaveCount = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

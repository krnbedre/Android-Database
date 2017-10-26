package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 05/10/17.
 */

public class LeaveModal implements Parcelable {



    private String id;
    private long startDate;
    private long endDate;
    private int leaveCount = 0;
    private Leave leave;

    private RequestedBy requestedBy;
    private Employee approver;
    private LeaveType leaveType;
    private MasterData leaveReason;
    private MasterData status;
    private String comments;
    private String approverComments;


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


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getApproverComments() {
        return approverComments;
    }

    public void setApproverComments(String approverComments) {
        this.approverComments = approverComments;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public RequestedBy getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(RequestedBy requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public MasterData getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(MasterData leaveReason) {
        this.leaveReason = leaveReason;
    }

    public MasterData getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }


    public class RequestedBy {
        Employee employee;

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }
    }
}

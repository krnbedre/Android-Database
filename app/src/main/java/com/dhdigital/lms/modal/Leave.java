package com.dhdigital.lms.modal;

/**
 * Created by admin on 03/10/17.
 */

public class Leave  {

    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Employee requestedBy) {
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

    public void setStatus(MasterData status) {
        this.status = status;
    }

    Employee requestedBy;
    Employee approver;
    LeaveType leaveType;
    MasterData leaveReason;
    MasterData status;

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    long fromDate = 0;
    long toDate = 0;
}

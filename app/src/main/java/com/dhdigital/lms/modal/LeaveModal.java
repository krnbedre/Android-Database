package com.dhdigital.lms.modal;

/**
 * Created by admin on 05/10/17.
 */

public class LeaveModal {



    private String id;
    private long startDate;
    private long endDate;
    private int leaveCount = 0;
    private Leave leave;

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

    public void setStatus(MasterData status) {
        this.status = status;
    }

    RequestedBy requestedBy;
    Employee approver;
    LeaveType leaveType;
    MasterData leaveReason;
    MasterData status;


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


    public class RequestedBy {
        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }

        Employee employee;
    }
}

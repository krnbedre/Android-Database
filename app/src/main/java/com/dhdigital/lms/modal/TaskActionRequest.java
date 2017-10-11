package com.dhdigital.lms.modal;

/**
 * Created by admin on 09/10/17.
 */

public class TaskActionRequest {

    private String taskid;
    private String statusCode;
    private String comments;
    private String delegateToEmpNo;
    //private MasterData rejectReason;
    private String id;
    private Employee delegateTo;
    private String instanceId;


    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDelegateToEmpNo() {
        return delegateToEmpNo;
    }

    public void setDelegateToEmpNo(String delegateToEmpNo) {
        this.delegateToEmpNo = delegateToEmpNo;
    }
/*
    public MasterData getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(MasterData rejectReason) {
        this.rejectReason = rejectReason;
    }*/


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Employee getDelegateTo() {
        return delegateTo;
    }

    public void setDelegateTo(Employee delegateTo) {
        this.delegateTo = delegateTo;
    }


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}


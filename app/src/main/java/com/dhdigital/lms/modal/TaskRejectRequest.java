package com.dhdigital.lms.modal;

/**
 * Created by admin on 09/10/17.
 */

public class TaskRejectRequest extends TaskActionRequest {


    private MasterData rejectReason;

    public MasterData getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(MasterData rejectReason) {
        this.rejectReason = rejectReason;
    }


}

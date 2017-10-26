package com.dhdigital.lms.gcm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shiv on 29/3/16.
 */
public class NotificationAction implements Parcelable {
    public static final Creator<NotificationAction> CREATOR = new Creator<NotificationAction>() {
        @Override
        public NotificationAction createFromParcel(Parcel in) {
            return new NotificationAction(in);
        }

        @Override
        public NotificationAction[] newArray(int size) {
            return new NotificationAction[size];
        }
    };
    private String title;
    private String body;
    private String module;
    private String type;
    private String leaveId;
    private String referenceNo;

    public NotificationAction() {
    }

    public NotificationAction(Parcel in) {
        module = in.readString();
        type = in.readString();
        leaveId = in.readString();
        referenceNo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(module);
        parcel.writeString(type);
        parcel.writeString(leaveId);
        parcel.writeString(referenceNo);
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

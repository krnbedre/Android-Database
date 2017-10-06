package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 27/09/17.
 */

public class LeaveType implements Parcelable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    private int id;
    private String code;
    private String name;
    private String leaveType;
    private Double balance = 0.0;


    public LeaveType(){

    }

    protected LeaveType(Parcel in) {
        id = in.readInt();
        code = in.readString();
        name = in.readString();
        leaveType = in.readString();

    }


    public static final Creator<LeaveType> CREATOR = new Creator<LeaveType>() {
        @Override
        public LeaveType createFromParcel(Parcel in) {
            return new LeaveType(in);
        }

        @Override
        public LeaveType[] newArray(int size) {
            return new LeaveType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(leaveType);


    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

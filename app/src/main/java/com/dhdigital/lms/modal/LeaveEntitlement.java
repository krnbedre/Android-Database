package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 27/09/17.
 */

public class LeaveEntitlement implements Parcelable{

    private int id;

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

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String code;
    private String name;
    private LeaveType leaveType;
    private int count;
    private long validFrom;
    private long validTo;


    public LeaveEntitlement(){

    }

    protected LeaveEntitlement(Parcel in) {
        id = in.readInt();
        code = in.readString();
        name = in.readString();
        leaveType = in.readParcelable(LeaveType.class.getClassLoader());
        count = in.readInt();
        validFrom = in.readLong();
        validTo = in.readLong();

    }


    public static final Creator<LeaveEntitlement> CREATOR = new Creator<LeaveEntitlement>() {
        @Override
        public LeaveEntitlement createFromParcel(Parcel in) {
            return new LeaveEntitlement(in);
        }

        @Override
        public LeaveEntitlement[] newArray(int size) {
            return new LeaveEntitlement[size];
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
        dest.writeInt(count);
        dest.writeLong(validFrom);
        dest.writeLong(validTo);
        dest.writeParcelable(leaveType, flags);


    }

    public long getStartDate() {
        return validFrom;
    }

    public void setStartDate(long startDate) {
        this.validFrom = startDate;
    }

    public long getEndDate() {
        return validTo;
    }

    public void setEndDate(long endDate) {
        this.validTo = endDate;
    }
}

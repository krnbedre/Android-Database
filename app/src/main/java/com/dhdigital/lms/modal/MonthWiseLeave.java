package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 11/10/17.
 */

public class MonthWiseLeave implements Parcelable{

    private float Approved;
    private float Rejected;
    private float Cancelled;
    private float Pending;
    private int Year;
    private String Month;

    public float getApproved() {
        return Approved;
    }

    public void setApproved(float approved) {
        Approved = approved;
    }

    public float getRejected() {
        return Rejected;
    }

    public void setRejected(float rejected) {
        Rejected = rejected;
    }

    public float getCancelled() {
        return Cancelled;
    }

    public void setCancelled(float cancelled) {
        Cancelled = cancelled;
    }

    public float getPending() {
        return Pending;
    }

    public void setPending(float pending) {
        Pending = pending;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }


    public MonthWiseLeave() {
    }

    public MonthWiseLeave(Parcel in) {
        Approved = in.readInt();
        Rejected = in.readFloat();
        Cancelled = in.readFloat();
        Year = in.readInt();
        Pending = in.readFloat();
        Month = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(Year);
        parcel.writeFloat(Approved);
        parcel.writeFloat(Rejected);
        parcel.writeFloat(Cancelled);
        parcel.writeFloat(Pending);
        parcel.writeString(Month);
    }


    public static final Creator<MonthWiseLeave> CREATOR = new Creator<MonthWiseLeave>() {
        @Override
        public MonthWiseLeave createFromParcel(Parcel in) {
            return new MonthWiseLeave(in);
        }

        @Override
        public MonthWiseLeave[] newArray(int size) {
            return new MonthWiseLeave[size];
        }
    };
}

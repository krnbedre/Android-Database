package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 27/09/17.
 */

public class Holiday implements Parcelable {


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

    public long getDate() {
        return holiday;
    }

    public void setDate(long date) {
        this.holiday = date;
    }

    private int id;
    private String code;
    private String name;
    private long holiday;




    public Holiday(){

    }

    protected Holiday(Parcel in) {
        id = in.readInt();
        code = in.readString();
        name = in.readString();
        holiday = in.readLong();


    }


    public static final Creator<Holiday> CREATOR = new Creator<Holiday>() {
        @Override
        public Holiday createFromParcel(Parcel in) {
            return new Holiday(in);
        }

        @Override
        public Holiday[] newArray(int size) {
            return new Holiday[size];
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
        dest.writeLong(holiday);



    }
}

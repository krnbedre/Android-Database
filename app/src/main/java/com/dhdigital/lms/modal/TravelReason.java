package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 18/05/17.
 */

public class TravelReason implements Parcelable {

    private String hangoutsSuggestion;

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

    private int id;
    private String code;
    private String name;

    public String getHangoutsSuggestion() {
        return hangoutsSuggestion;
    }

    public void setHangoutsSuggestion(String hangoutsSuggestion) {
        this.hangoutsSuggestion = hangoutsSuggestion;
    }



    public TravelReason() {

    }

    protected TravelReason(Parcel in) {

        id = in.readInt();
        code = in.readString();
        name = in.readString();
        hangoutsSuggestion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(code);
        parcel.writeString(name);
        parcel.writeString(hangoutsSuggestion);
    }


    public static final Creator<TravelReason> CREATOR = new Creator<TravelReason>() {
        @Override
        public TravelReason createFromParcel(Parcel in) {
            return new TravelReason(in);
        }

        @Override
        public TravelReason[] newArray(int size) {
            return new TravelReason[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof TravelReason)) return false;
        TravelReason m = (TravelReason) o;
        if ((m.getId() == this.getId()) && (m.getName().equals(this.getName())) && (m.getCode().equals(this.getCode()) && (m.getHangoutsSuggestion().equalsIgnoreCase(this.getHangoutsSuggestion())))) {
            return true;
        }
        return false;
    }
}

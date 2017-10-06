package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kiran Bedre on 27/11/15.
 * DarkHorse BOA
 */
public class MasterData implements Parcelable {

    public static final Creator<MasterData> CREATOR = new Creator<MasterData>() {
        @Override
        public MasterData createFromParcel(Parcel in) {
            return new MasterData(in);
        }

        @Override
        public MasterData[] newArray(int size) {
            return new MasterData[size];
        }
    };

    private Integer id;
    private String code;
    private String name;

    public MasterData() {
    }

    public MasterData(Parcel in) {
        id = in.readInt();
        code = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(code);
        parcel.writeString(name);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof MasterData)) return false;
        MasterData m = (MasterData) o;
        if ((m.getId() == this.getId()) && (m.getName().equals(this.getName())) && (m.getCode().equals(this.getCode()))) {
            return true;
        }
        return false;
    }



    public enum TYPE {
        LeaveEntitlement,
        LeaveType,
        LeaveReason,
        Team,
        Holiday
    }
}

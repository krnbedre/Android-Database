package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Darkhorse on 11/2/2016.
 */

public class CityMasterData implements Parcelable {


    private int id;
    private String code;
    private String name;
    private String cityClass;
    private String hasGuestHouse;
    //private MasterData cityClass;
    private MasterData cityCountry;

    public CityMasterData() {
    }


    protected CityMasterData(Parcel in) {
        id = in.readInt();
        code = in.readString();
        name = in.readString();
        cityClass = in.readString();
        hasGuestHouse = in.readString();
        //cityClass = in.readParcelable(MasterData.class.getClassLoader());
        cityCountry = in.readParcelable(MasterData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(cityClass);
        dest.writeString(hasGuestHouse);
        //dest.writeParcelable(cityClass, flags);
        dest.writeParcelable(cityCountry, flags);
    }

    public static final Creator<CityMasterData> CREATOR = new Creator<CityMasterData>() {
        @Override
        public CityMasterData createFromParcel(Parcel in) {
            return new CityMasterData(in);
        }

        @Override
        public CityMasterData[] newArray(int size) {
            return new CityMasterData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

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


    //public MasterData getCityClass() { return cityClass;}
    public MasterData getCityCountry() { return cityCountry; }

    //public void setCityClass(MasterData cityClass) { this.cityClass =  cityClass;}
    public void setCityCountry(MasterData cityCountry) { this.cityCountry = cityCountry; }


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

    public String getCityClass() {
        return cityClass;
    }

    public void setCityClass(String cityClass) {
        this.cityClass = cityClass;
    }

    public String getHasGuestHouse() {
        return hasGuestHouse;
    }

    public void setHasGuestHouse(String hasGuestHouse) {
        this.hasGuestHouse = hasGuestHouse;
    }
}

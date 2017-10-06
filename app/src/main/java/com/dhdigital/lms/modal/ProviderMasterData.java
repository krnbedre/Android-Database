package com.dhdigital.lms.modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 22/05/17.
 */

public class ProviderMasterData implements Parcelable {



    private int id;
    private String code;
    private String name;
    private Double tentativeAmount;
    private CityMasterData location;
    private MasterData category;

    public ProviderMasterData(){

    }

    protected ProviderMasterData(Parcel in) {
        id = in.readInt();
        code = in.readString();
        name = in.readString();
        tentativeAmount = in.readDouble();
        category = in.readParcelable(MasterData.class.getClassLoader());
        location = in.readParcelable(CityMasterData.class.getClassLoader());
    }


    public static final Creator<ProviderMasterData> CREATOR = new Creator<ProviderMasterData>() {
        @Override
        public ProviderMasterData createFromParcel(Parcel in) {
            return new ProviderMasterData(in);
        }

        @Override
        public ProviderMasterData[] newArray(int size) {
            return new ProviderMasterData[size];
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
        dest.writeDouble(tentativeAmount);
        dest.writeParcelable(category, flags);
        dest.writeParcelable(location,flags);

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

    public Double getTentativeAmount() {
        return tentativeAmount;
    }

    public void setTentativeAmount(Double tentativeAmount) {
        this.tentativeAmount = tentativeAmount;
    }

    public CityMasterData getLocation() {
        return location;
    }

    public void setLocation(CityMasterData location) {
        this.location = location;
    }

    public MasterData getCategory() {
        return category;
    }

    public void setCategory(MasterData category) {
        this.category = category;
    }
}

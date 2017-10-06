/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.kelltontech.volley.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * To be used with all database tables.
 *
 * @author kapil.vij
 */
public class BaseModel implements IModel, Parcelable {

    public static Parcelable.Creator<BaseModel> CREATOR = new Parcelable.Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel source) {
            return new BaseModel(source);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
    private int primaryKey;

    /**
     * no-arg constructor
     */
    public BaseModel() {
        // nothing to do here
    }

    /**
     * Reconstruct from the Parcel
     *
     * @param source
     */
    public BaseModel(Parcel source) {
        primaryKey = source.readInt();
    }

    /**
     * @return the primaryKey
     */
    public int getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryKey);
    }
}

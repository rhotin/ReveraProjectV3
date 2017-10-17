package com.ideaone.tabletapp1;

import android.os.Parcel;
import android.os.Parcelable;


public class SettingsCompanyObject implements Parcelable {
    String name;
    String text;

    SettingsCompanyObject(String mName, String mComp) {
        this.name = mName;
        this.text = mComp;
    }


    protected SettingsCompanyObject(Parcel in) {
        name = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(text);
    }

    public static final Creator<SettingsCompanyObject> CREATOR = new Creator<SettingsCompanyObject>() {
        @Override
        public SettingsCompanyObject createFromParcel(Parcel in) {
            return new SettingsCompanyObject(in);
        }

        @Override
        public SettingsCompanyObject[] newArray(int size) {
            return new SettingsCompanyObject[size];
        }
    };
}

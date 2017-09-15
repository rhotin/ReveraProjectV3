package com.ideaone.reveraprojectapp1;

import android.os.Parcel;
import android.os.Parcelable;


public class SettingsLocationObject implements Parcelable {
    String handle;

    SettingsLocationObject(String mName) {
        this.handle = mName;
    }


    protected SettingsLocationObject(Parcel in) {
        handle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(handle);
    }

    public static final Creator<SettingsLocationObject> CREATOR = new Creator<SettingsLocationObject>() {
        @Override
        public SettingsLocationObject createFromParcel(Parcel in) {
            return new SettingsLocationObject(in);
        }

        @Override
        public SettingsLocationObject[] newArray(int size) {
            return new SettingsLocationObject[size];
        }
    };
}

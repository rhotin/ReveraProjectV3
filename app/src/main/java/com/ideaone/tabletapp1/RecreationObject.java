package com.ideaone.tabletapp1;

import android.os.Parcel;
import android.os.Parcelable;

class RecreationObject implements Parcelable {
    String creatorID;
    String displayID;
    String dateM;
    String ev_1;
    String min_1;
    String hr_1;
    String activity_1;
    String location_1;

    String ev_2;
    String min_2;
    String hr_2;
    String activity_2;
    String location_2;

    String ev_3;
    String min_3;
    String hr_3;
    String activity_3;
    String location_3;

    String ev_4;
    String min_4;
    String hr_4;
    String activity_4;
    String location_4;

    String ev_5;
    String min_5;
    String hr_5;
    String activity_5;
    String location_5;

    String ev_6;
    String min_6;
    String hr_6;
    String activity_6;
    String location_6;

    String ev_7;
    String min_7;
    String hr_7;
    String activity_7;
    String location_7;

    String ev_8;
    String min_8;
    String hr_8;
    String activity_8;
    String location_8;

    String ev_9;
    String min_9;
    String hr_9;
    String activity_9;
    String location_9;

    String ev_10;
    String min_10;
    String hr_10;
    String activity_10;
    String location_10;

    String handle;
    String recId;

    RecreationObject(String creator_ID, String display_ID, String date_,
                     String ev1, String min1, String hr1, String activity1, String location1,
                     String ev2, String min2, String hr2, String activity2, String location2,
                     String ev3, String min3, String hr3, String activity3, String location3,
                     String ev4, String min4, String hr4, String activity4, String location4,
                     String ev5, String min5, String hr5, String activity5, String location5,
                     String ev6, String min6, String hr6, String activity6, String location6,
                     String ev7, String min7, String hr7, String activity7, String location7,
                     String ev8, String min8, String hr8, String activity8, String location8,
                     String ev9, String min9, String hr9, String activity9, String location9,
                     String ev10, String min10, String hr10, String activity10, String location10,
                     String handle_, String rec_Id) {
        this.creatorID = creator_ID;
        this.displayID = display_ID;
        this.dateM = date_;
        this.ev_1 = ev1;
        this.min_1 = min1;
        this.hr_1 = hr1;
        this.activity_1 = activity1;
        this.location_1 = location1;

        this.ev_2 = ev2;
        this.min_2 = min2;
        this.hr_2 = hr2;
        this.activity_2 = activity2;
        this.location_2 = location2;

        this.ev_3 = ev3;
        this.min_3 = min3;
        this.hr_3 = hr3;
        this.activity_3 = activity3;
        this.location_3 = location3;

        this.ev_4 = ev4;
        this.min_4 = min4;
        this.hr_4 = hr4;
        this.activity_4 = activity4;
        this.location_4 = location4;

        this.ev_5 = ev5;
        this.min_5 = min5;
        this.hr_5 = hr5;
        this.activity_5 = activity5;
        this.location_5 = location5;

        this.ev_6 = ev6;
        this.min_6 = min6;
        this.hr_6 = hr6;
        this.activity_6 = activity6;
        this.location_6 = location6;

        this.ev_7 = ev7;
        this.min_7 = min7;
        this.hr_7 = hr7;
        this.activity_7 = activity7;
        this.location_7 = location7;

        this.ev_8 = ev8;
        this.min_8 = min8;
        this.hr_8 = hr8;
        this.activity_8 = activity8;
        this.location_8 = location8;

        this.ev_9 = ev9;
        this.min_9 = min9;
        this.hr_9 = hr9;
        this.activity_9 = activity9;
        this.location_9 = location9;

        this.ev_10 = ev10;
        this.min_10 = min10;
        this.hr_10 = hr10;
        this.activity_10 = activity10;
        this.location_10 = location10;

        this.handle = handle_;
        this.recId = rec_Id;
    }

    private RecreationObject(Parcel in) {
        creatorID = in.readString();
        displayID = in.readString();
        dateM = in.readString();
        ev_1 = in.readString();
        min_1 = in.readString();
        hr_1 = in.readString();
        activity_1 = in.readString();
        location_1 = in.readString();

        ev_2 = in.readString();
        min_2 = in.readString();
        hr_2 = in.readString();
        activity_2 = in.readString();
        location_2 = in.readString();

        ev_3 = in.readString();
        min_3 = in.readString();
        hr_3 = in.readString();
        activity_3 = in.readString();
        location_3 = in.readString();

        ev_4 = in.readString();
        min_4 = in.readString();
        hr_4 = in.readString();
        activity_4 = in.readString();
        location_4 = in.readString();

        ev_5 = in.readString();
        min_5 = in.readString();
        hr_5 = in.readString();
        activity_5 = in.readString();
        location_5 = in.readString();

        ev_6 = in.readString();
        min_6 = in.readString();
        hr_6 = in.readString();
        activity_6 = in.readString();
        location_6 = in.readString();

        ev_7 = in.readString();
        min_7 = in.readString();
        hr_7 = in.readString();
        activity_7 = in.readString();
        location_7 = in.readString();

        ev_8 = in.readString();
        min_8 = in.readString();
        hr_8 = in.readString();
        activity_8 = in.readString();
        location_8 = in.readString();

        ev_9 = in.readString();
        min_9 = in.readString();
        hr_9 = in.readString();
        activity_9 = in.readString();
        location_9 = in.readString();

        ev_10 = in.readString();
        min_10 = in.readString();
        hr_10 = in.readString();
        activity_10 = in.readString();
        location_10 = in.readString();

        handle = in.readString();
        recId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creatorID);
        dest.writeString(displayID);
        dest.writeString(dateM);
        dest.writeString(ev_1);
        dest.writeString(min_1);
        dest.writeString(hr_1);
        dest.writeString(activity_1);
        dest.writeString(location_1);

        dest.writeString(ev_2);
        dest.writeString(min_2);
        dest.writeString(hr_2);
        dest.writeString(activity_2);
        dest.writeString(location_2);

        dest.writeString(ev_3);
        dest.writeString(min_3);
        dest.writeString(hr_3);
        dest.writeString(activity_3);
        dest.writeString(location_3);

        dest.writeString(ev_4);
        dest.writeString(min_4);
        dest.writeString(hr_4);
        dest.writeString(activity_4);
        dest.writeString(location_4);

        dest.writeString(ev_5);
        dest.writeString(min_5);
        dest.writeString(hr_5);
        dest.writeString(activity_5);
        dest.writeString(location_5);

        dest.writeString(ev_6);
        dest.writeString(min_6);
        dest.writeString(hr_6);
        dest.writeString(activity_6);
        dest.writeString(location_6);

        dest.writeString(ev_7);
        dest.writeString(min_7);
        dest.writeString(hr_7);
        dest.writeString(activity_7);
        dest.writeString(location_7);

        dest.writeString(ev_8);
        dest.writeString(min_8);
        dest.writeString(hr_8);
        dest.writeString(activity_8);
        dest.writeString(location_8);

        dest.writeString(ev_9);
        dest.writeString(min_9);
        dest.writeString(hr_9);
        dest.writeString(activity_9);
        dest.writeString(location_9);

        dest.writeString(ev_10);
        dest.writeString(min_10);
        dest.writeString(hr_10);
        dest.writeString(activity_10);
        dest.writeString(location_10);

        dest.writeString(handle);
        dest.writeString(recId);
    }

    @SuppressWarnings("unused")
    public static final Creator<RecreationObject> CREATOR = new Creator<RecreationObject>() {
        @Override
        public RecreationObject createFromParcel(Parcel in) {
            return new RecreationObject(in);
        }

        @Override
        public RecreationObject[] newArray(int size) {
            return new RecreationObject[size];
        }
    };
}
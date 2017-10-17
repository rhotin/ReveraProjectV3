package com.ideaone.tabletapp1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roman on 4/17/2016.
 */
public class MsgObjectHome implements Parcelable{

    String msgAuthor;
    String msgText;

    MsgObjectHome (String mAuthor, String mText){
        this.msgAuthor = mAuthor;
        this.msgText = mText;
    }


    protected MsgObjectHome(Parcel in) {
        msgAuthor = in.readString();
        msgText = in.readString();
    }

    public static final Creator<MsgObjectHome> CREATOR = new Creator<MsgObjectHome>() {
        @Override
        public MsgObjectHome createFromParcel(Parcel in) {
            return new MsgObjectHome(in);
        }

        @Override
        public MsgObjectHome[] newArray(int size) {
            return new MsgObjectHome[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msgAuthor);
        dest.writeString(msgText);
    }
}

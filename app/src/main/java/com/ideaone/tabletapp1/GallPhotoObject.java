package com.ideaone.tabletapp1;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


class GallPhotoObject implements Parcelable {
    String creatorID;
    String created;
    String mClass;
    String type;
    String photoExt;
    String photoName;
    String ID;
    Bitmap thumbnail;
    Bitmap fullImg;

    GallPhotoObject(String creator_ID, String created_date, String mclass_, String type_,
                    String photo_ext, String photo_name, String id, Bitmap photo_thumbnail, Bitmap photo_full) {
        this.creatorID = creator_ID;
        this.created = created_date;
        this.mClass = mclass_;
        this.type = type_;
        this.photoExt = photo_ext;
        this.photoName = photo_name;
        this.ID = id;
        this.thumbnail = photo_thumbnail;
        this.fullImg = photo_full;
    }

    private GallPhotoObject(Parcel in) {
        creatorID = in.readString();
        created = in.readString();
        mClass = in.readString();
        type = in.readString();
        photoExt = in.readString();
        photoName = in.readString();
        ID = in.readString();
        thumbnail = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        fullImg = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creatorID);
        dest.writeString(created);
        dest.writeString(mClass);
        dest.writeString(type);
        dest.writeString(photoExt);
        dest.writeString(photoName);
        dest.writeString(ID);
        dest.writeValue(thumbnail);
        dest.writeValue(fullImg);
    }

    @SuppressWarnings("unused")
    public static final Creator<GallPhotoObject> CREATOR = new Creator<GallPhotoObject>() {
        @Override
        public GallPhotoObject createFromParcel(Parcel in) {
            return new GallPhotoObject(in);
        }

        @Override
        public GallPhotoObject[] newArray(int size) {
            return new GallPhotoObject[size];
        }
    };
}
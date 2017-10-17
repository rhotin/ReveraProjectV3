package com.ideaone.tabletapp1;

import android.os.Parcel;
import android.os.Parcelable;

class GallAlbumObject implements Parcelable {

    String albumCreatorID;
    String albumCreated;
    String albumName;
    String albumDisplayID;
    String albumWeb;
    String albumDisplay;
    String albumModified;
    String albumHandle;
    String albumID;

    GallAlbumObject(String album_CreatorID, String album_Created, String album_name, String album_DisplayID,
                    String album_Web, String album_Display, String album_Modified, String album_Handle,
                    String album_id){
        this.albumCreatorID = album_CreatorID;
        this.albumCreated = album_Created;
        this.albumName = album_name;
        this.albumDisplayID = album_DisplayID;
        this.albumWeb = album_Web;
        this.albumDisplay = album_Display;
        this.albumModified = album_Modified;
        this.albumHandle = album_Handle;
        this.albumID = album_id;
    }

    private GallAlbumObject(Parcel in) {
        albumCreatorID = in.readString();
        albumCreated = in.readString();
        albumName = in.readString();
        albumDisplayID = in.readString();
        albumWeb = in.readString();
        albumDisplay = in.readString();
        albumModified = in.readString();
        albumHandle = in.readString();
        albumID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumCreatorID);
        dest.writeString(albumCreated);
        dest.writeString(albumName);
        dest.writeString(albumDisplayID);
        dest.writeString(albumWeb);
        dest.writeString(albumDisplay);
        dest.writeString(albumModified);
        dest.writeString(albumHandle);
        dest.writeString(albumID);
    }

    @SuppressWarnings("unused")
    public static final Creator<GallAlbumObject> CREATOR = new Creator<GallAlbumObject>() {
        @Override
        public GallAlbumObject createFromParcel(Parcel in) {
            return new GallAlbumObject(in);
        }

        @Override
        public GallAlbumObject[] newArray(int size) {
            return new GallAlbumObject[size];
        }
    };
}
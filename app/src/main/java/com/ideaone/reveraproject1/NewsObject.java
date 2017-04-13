package com.ideaone.reveraproject1;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roman on 2016-07-11.
 */
public class NewsObject implements Parcelable {
    String author;
    String title;
    String description;
    String url;
    String urlToImage;
    Bitmap image;


    NewsObject(String news_author, String news_title, String news_description,
               String news_url, String news_image_url, Bitmap news_image) {
        this.author = news_author;
        this.title = news_title;
        this.description = news_description;
        this.url = news_url;
        this.urlToImage = news_image_url;
        this.image = news_image;
    }

    protected NewsObject(Parcel in) {
        author = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        urlToImage = in.readString();
        image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeValue(image);
    }

    @SuppressWarnings("unused")
    public static final Creator<NewsObject> CREATOR = new Creator<NewsObject>() {
        @Override
        public NewsObject createFromParcel(Parcel in) {
            return new NewsObject(in);
        }

        @Override
        public NewsObject[] newArray(int size) {
            return new NewsObject[size];
        }
    };
}

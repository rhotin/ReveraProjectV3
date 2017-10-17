package com.ideaone.tabletapp1;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PromoObject implements Parcelable {
    String creatorID;
    String created;
    String name;
    String zone;
    String displayID;
    int length;
    int priority;
    String dateStart;
    String dateEnd;
    String web;
    String display;
    String calendar;
    String bulletin;
    String kiosk;
    String type;

    String promoType;
    String modified;
    int showMonday;
    int showTuesday;
    int showWednesday;
    int showThursday;
    int showFriday;
    int showSaturday;
    int showSunday;
    String url;

    String text;
    Bitmap photo;
    Bitmap backPhoto;

    PromoObject(String a_creatorID, String a_created, String a_name, String a_zone,
                String a_displayID, int a_length, int a_priority,
                String a_dateStart, String a_dateEnd, String a_web, String a_display,
                String a_calendar, String a_bulletin, String a_kiosk, String a_type, String a_promoType,
                String a_modified, int a_showMonday, int a_showTuesday, int a_showWednesday,
                int a_showThursday, int a_showFriday, int a_showSaturday, int a_showSunday,
                String a_url, String a_text, Bitmap a_photo, Bitmap a_backPhoto) {
        this.creatorID = a_creatorID;
        this.created = a_created;
        this.name = a_name;
        this.zone = a_zone;
        this.displayID = a_displayID;
        this.length = a_length;
        this.priority = a_priority;
        this.dateStart = a_dateStart;
        this.dateEnd = a_dateEnd;
        this.web = a_web;
        this.display = a_display;
        this.calendar = a_calendar;
        this.bulletin = a_bulletin;
        this.kiosk = a_kiosk;
        this.type = a_type;
        this.promoType = a_promoType;
        this.modified = a_modified;
        this.showMonday = a_showMonday;
        this.showTuesday = a_showTuesday;
        this.showWednesday = a_showWednesday;
        this.showThursday = a_showThursday;
        this.showFriday = a_showFriday;
        this.showSaturday = a_showSaturday;
        this.showSunday = a_showSunday;
        this.url = a_url;
        this.text = a_text;
        this.photo = a_photo;
        this.backPhoto = a_backPhoto;
    }

    protected PromoObject(Parcel in) {
        creatorID = in.readString();
        created = in.readString();
        name = in.readString();
        zone = in.readString();
        displayID = in.readString();
        length = in.readInt();
        priority = in.readInt();
        dateStart = in.readString();
        dateEnd = in.readString();
        web = in.readString();
        display = in.readString();
        calendar = in.readString();
        bulletin = in.readString();
        kiosk = in.readString();
        type = in.readString();
        promoType = in.readString();
        modified = in.readString();
        showMonday = in.readInt();
        showTuesday = in.readInt();
        showWednesday = in.readInt();
        showThursday = in.readInt();
        showFriday = in.readInt();
        showSaturday = in.readInt();
        showSunday = in.readInt();
        url = in.readString();
        text = in.readString();
        photo = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        backPhoto = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creatorID);
        dest.writeString(created);
        dest.writeString(name);
        dest.writeString(zone);
        dest.writeString(displayID);
        dest.writeInt(length);
        dest.writeInt(priority);
        dest.writeString(dateStart);
        dest.writeString(dateEnd);
        dest.writeString(web);
        dest.writeString(display);
        dest.writeString(calendar);
        dest.writeString(bulletin);
        dest.writeString(kiosk);
        dest.writeString(type);
        dest.writeString(promoType);
        dest.writeString(modified);
        dest.writeInt(showMonday);
        dest.writeInt(showTuesday);
        dest.writeInt(showWednesday);
        dest.writeInt(showThursday);
        dest.writeInt(showFriday);
        dest.writeInt(showSaturday);
        dest.writeInt(showSunday);
        dest.writeString(url);
        dest.writeString(text);
        dest.writeValue(photo);
        dest.writeValue(backPhoto);
    }

    @SuppressWarnings("unused")
    public static final Creator<PromoObject> CREATOR = new Creator<PromoObject>() {
        @Override
        public PromoObject createFromParcel(Parcel in) {
            return new PromoObject(in);
        }

        @Override
        public PromoObject[] newArray(int size) {
            return new PromoObject[size];
        }
    };
}
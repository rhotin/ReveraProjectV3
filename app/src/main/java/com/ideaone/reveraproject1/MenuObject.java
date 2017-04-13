package com.ideaone.reveraproject1;

import android.os.Parcel;
import android.os.Parcelable;


class MenuObject implements Parcelable {
    String creatorID;
    String modified;
    String displayID;
    String dateM;
    String lunchSoup;
    String lunchSalad;
    String lunchEntree1;
    String lunchEntree2;
    String lunchDessert;
    String lunchOther;
    String dinnerSoup;
    String dinnerSalad;
    String dinnerEntree1;
    String dinnerEntree2;
    String dinnerPotato;
    String dinnerVeg;
    String dinnerDessert;
    String dinnerOther;
    String handle;
    String menuId;

    MenuObject(String creator_ID, String modified_, String display_ID, String date_, String lunch_soup, String lunch_salad,
               String lunch_entree1, String lunch_entree2, String lunch_dessert, String lunch_other,
               String dinner_soup, String dinner_salad, String dinne_entree1, String dinner_entree2,
               String dinner_potato, String dinner_veg, String dinner_dessert, String dinner_other,
               String handle_, String menu_Id) {
        this.creatorID = creator_ID;
        this.modified = modified_;
        this.displayID = display_ID;
        this.dateM = date_;
        this.lunchSoup = lunch_soup;
        this.lunchSalad = lunch_salad;
        this.lunchEntree1 = lunch_entree1;
        this.lunchEntree2 = lunch_entree2;
        this.lunchDessert = lunch_dessert;
        this.lunchOther = lunch_other;
        this.dinnerSoup = dinner_soup;
        this.dinnerSalad = dinner_salad;
        this.dinnerEntree1 = dinne_entree1;
        this.dinnerEntree2 = dinner_entree2;
        this.dinnerPotato = dinner_potato;
        this.dinnerVeg = dinner_veg;
        this.dinnerDessert = dinner_other;
        this.dinnerOther = dinner_dessert;
        this.handle = handle_;
        this.menuId = menu_Id;
    }

    private MenuObject(Parcel in) {
        creatorID = in.readString();
        modified = in.readString();
        displayID = in.readString();
        dateM = in.readString();
        lunchSoup = in.readString();
        lunchSalad = in.readString();
        lunchEntree1 = in.readString();
        lunchEntree2 = in.readString();
        lunchDessert = in.readString();
        lunchOther = in.readString();
        dinnerSoup = in.readString();
        dinnerSalad = in.readString();
        dinnerEntree1 = in.readString();
        dinnerEntree2 = in.readString();
        dinnerPotato = in.readString();
        dinnerVeg = in.readString();
        dinnerDessert = in.readString();
        dinnerOther = in.readString();
        handle = in.readString();
        menuId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(creatorID);
        dest.writeString(modified);
        dest.writeString(displayID);
        dest.writeString(dateM);
        dest.writeString(lunchSoup);
        dest.writeString(lunchSalad);
        dest.writeString(lunchEntree1);
        dest.writeString(lunchEntree2);
        dest.writeString(lunchDessert);
        dest.writeString(lunchOther);
        dest.writeString(dinnerSoup);
        dest.writeString(dinnerSalad);
        dest.writeString(dinnerEntree1);
        dest.writeString(dinnerEntree2);
        dest.writeString(dinnerPotato);
        dest.writeString(dinnerVeg);
        dest.writeString(dinnerDessert);
        dest.writeString(dinnerOther);
        dest.writeString(handle);
        dest.writeString(menuId);
    }

    @SuppressWarnings("unused")
    public static final Creator<MenuObject> CREATOR = new Creator<MenuObject>() {
        @Override
        public MenuObject createFromParcel(Parcel in) {
            return new MenuObject(in);
        }

        @Override
        public MenuObject[] newArray(int size) {
            return new MenuObject[size];
        }
    };
}
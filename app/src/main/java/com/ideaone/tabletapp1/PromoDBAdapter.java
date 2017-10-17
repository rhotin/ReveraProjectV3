package com.ideaone.tabletapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PromoDBAdapter {
    private static final String KEY_ROWID = "_id";
    private static final String KEY_CREATORID = "creatorID";
    private static final String KEY_CREATED = "created";
    private static final String KEY_NAME = "name";
    private static final String KEY_ZONE = "zone";
    private static final String KEY_DISPLAYID = "displayID";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_DATE_START = "dateStart";
    private static final String KEY_DATE_END = "dateEnd";
    private static final String KEY_WEB = "web";
    private static final String KEY_DISPLAY = "display";
    private static final String KEY_CALENDAR = "calendar";
    private static final String KEY_BULLETIN = "messages";
    private static final String KEY_KIOSK = "kiosk";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PROMO_TYPE = "promoType";
    private static final String KEY_MODIFIED = "modifiedDate";
    private static final String KEY_SHOW_MONDAY = "showMonday";
    private static final String KEY_SHOW_TUESDAY = "showTuesday";
    private static final String KEY_SHOW_WEDNESDAY = "showWednesday";
    private static final String KEY_SHOW_THURSDAY = "showThursday";
    private static final String KEY_SHOW_FRIDAY = "showFriday";
    private static final String KEY_SHOW_SATURDAY = "showSaturday";
    private static final String KEY_SHOW_SUNDAY = "showSunday";
    private static final String KEY_URL = "url";
    private static final String KEY_TEXT = "text";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_BACKPHOTO = "backphoto";

    private static final String DATABASE_NAME = "MyPromoDB";
    private static final String DATABASE_TABLE = "promoDB";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
            "create table if not exists " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    + KEY_CREATORID + " text not null, "
                    + KEY_CREATED + " text, "
                    + KEY_NAME + " text not null, "
                    + KEY_ZONE + " text, "
                    + KEY_DISPLAYID + " text, "
                    + KEY_LENGTH + " INTEGER, "
                    + KEY_PRIORITY + " INTEGER, "
                    + KEY_DATE_START + " text not null, "
                    + KEY_DATE_END + " text not null, "
                    + KEY_WEB + " text, "
                    + KEY_DISPLAY + " text, "
                    + KEY_CALENDAR + " text, "
                    + KEY_BULLETIN + " text, "
                    + KEY_KIOSK + " text, "
                    + KEY_TYPE + " text not null, "
                    + KEY_PROMO_TYPE + " text not null, "
                    + KEY_MODIFIED + " text not null, "
                    + KEY_SHOW_MONDAY + " INTEGER, "
                    + KEY_SHOW_TUESDAY + " INTEGER, "
                    + KEY_SHOW_WEDNESDAY + " INTEGER, "
                    + KEY_SHOW_THURSDAY + " INTEGER, "
                    + KEY_SHOW_FRIDAY + " INTEGER, "
                    + KEY_SHOW_SATURDAY + " INTEGER, "
                    + KEY_SHOW_SUNDAY + " INTEGER, "
                    + KEY_URL + " text, "
                    + KEY_TEXT + " text, "
                    + KEY_PHOTO + " BLOB, "
                    + KEY_BACKPHOTO + " BLOB"
                    + ");";


    final Context context;

    private DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public PromoDBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("DBAdapter", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    //---opens the database---
    public PromoDBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    public void resetDB() {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        try {
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---insert an item into the database---
    public void insertItem(String mcreatorID, String mcreated, String mname, String mzone,
                           String mdisplayID, int mlength, int mpriority, String mdateStart,
                           String mdateEnd, String mweb, String mdisplay, String mcalendar,
                           String mbulletin, String mkiosk, String mtype, String mPromoType, String mModify,
                           int mshowMonday, int mshowTuesday, int mshowWednesday, int mshowThursday,
                           int mshowFriday, int mshowSaturday, int mshowSunday, String mUrl,
                           String mtext, byte[] mphoto, byte[] mbackPhoto) {
        //  dbAlb.execSQL(DATABASE_CREATE);
        ContentValues insertValues = new ContentValues();
        insertValues.put(KEY_CREATORID, mcreatorID);
        insertValues.put(KEY_CREATED, mcreated);
        insertValues.put(KEY_NAME, mname);
        insertValues.put(KEY_ZONE, mzone);
        insertValues.put(KEY_DISPLAYID, mdisplayID);
        insertValues.put(KEY_LENGTH, mlength);
        insertValues.put(KEY_PRIORITY, mpriority);
        insertValues.put(KEY_DATE_START, mdateStart);
        insertValues.put(KEY_DATE_END, mdateEnd);
        insertValues.put(KEY_WEB, mweb);
        insertValues.put(KEY_DISPLAY, mdisplay);
        insertValues.put(KEY_CALENDAR, mcalendar);
        insertValues.put(KEY_BULLETIN, mbulletin);
        insertValues.put(KEY_KIOSK, mkiosk);
        insertValues.put(KEY_TYPE, mtype);
        insertValues.put(KEY_PROMO_TYPE, mPromoType);
        insertValues.put(KEY_MODIFIED, mModify);
        insertValues.put(KEY_SHOW_MONDAY, mshowMonday);
        insertValues.put(KEY_SHOW_TUESDAY, mshowTuesday);
        insertValues.put(KEY_SHOW_WEDNESDAY, mshowWednesday);
        insertValues.put(KEY_SHOW_THURSDAY, mshowThursday);
        insertValues.put(KEY_SHOW_FRIDAY, mshowFriday);
        insertValues.put(KEY_SHOW_SATURDAY, mshowSaturday);
        insertValues.put(KEY_SHOW_SUNDAY, mshowSunday);
        insertValues.put(KEY_URL, mUrl);
        insertValues.put(KEY_TEXT, mtext);
        insertValues.put(KEY_PHOTO, mphoto);
        insertValues.put(KEY_BACKPHOTO, mbackPhoto);
        db.insertOrThrow(DATABASE_TABLE, null, insertValues);
    }

    //---retrieves all the contacts---
    public Cursor getAllItems() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID,
                        KEY_CREATED, KEY_NAME, KEY_ZONE, KEY_DISPLAYID, KEY_LENGTH, KEY_PRIORITY,
                        KEY_DATE_START, KEY_DATE_END, KEY_WEB, KEY_DISPLAY, KEY_CALENDAR, KEY_BULLETIN,
                        KEY_KIOSK, KEY_TYPE, KEY_PROMO_TYPE, KEY_MODIFIED, KEY_SHOW_MONDAY,
                        KEY_SHOW_TUESDAY, KEY_SHOW_WEDNESDAY, KEY_SHOW_THURSDAY, KEY_SHOW_FRIDAY,
                        KEY_SHOW_SATURDAY, KEY_SHOW_SUNDAY, KEY_URL, KEY_TEXT, KEY_PHOTO, KEY_BACKPHOTO},
                null, null, null, null, KEY_PRIORITY);
    }

    //---retrieves a particular contact---
    public Cursor getItem(String mcreatorID, String mcreated, String mname, String mzone,
                          String mdisplayID, int mlength, int mpriority, String mdateStart,
                          String mdateEnd, String mweb, String mdisplay, String mcalendar,
                          String mbulletin, String mkiosk, String mtype, String mPromoType, String mModify,
                          int mshowMonday, int mshowTuesday, int mshowWednesday, int mshowThursday,
                          int mshowFriday, int mshowSaturday, int mshowSunday, String mUrl,
                          String mtext, byte[] mphoto, byte[] mbackPhoto) throws SQLException {
        return db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID,
                        KEY_CREATED, KEY_NAME, KEY_ZONE, KEY_DISPLAYID, KEY_LENGTH, KEY_PRIORITY,
                        KEY_DATE_START, KEY_DATE_END, KEY_WEB, KEY_DISPLAY, KEY_CALENDAR, KEY_BULLETIN,
                        KEY_KIOSK, KEY_TYPE, KEY_PROMO_TYPE, KEY_MODIFIED, KEY_SHOW_MONDAY, KEY_SHOW_TUESDAY,
                        KEY_SHOW_WEDNESDAY, KEY_SHOW_THURSDAY, KEY_SHOW_FRIDAY, KEY_SHOW_SATURDAY,
                        KEY_SHOW_SUNDAY, KEY_URL, KEY_TEXT, KEY_PHOTO, KEY_BACKPHOTO}, KEY_NAME + "='" + mname + "'" +
                        " AND " + KEY_PRIORITY + "='" + mpriority + "'" +
                        " AND " + KEY_TYPE + "='" + mtype + "'", null,
                null, null, null, null);
    }

    //---updates a contact---

    public boolean updateItem(String mcreatorID, String mcreated, String mname, String mzone,
                              String mdisplayID, int mlength, int mpriority, String mdateStart,
                              String mdateEnd, String mweb, String mdisplay, String mcalendar,
                              String mbulletin, String mkiosk, String mtype, String mPromoType, String mModify,
                              int mshowMonday, int mshowTuesday, int mshowWednesday, int mshowThursday,
                              int mshowFriday, int mshowSaturday, int mshowSunday, String mUrl,
                              String mtext, byte[] mphoto, byte[] mbackPhoto) {
        ContentValues args = new ContentValues();
        args.put(KEY_CREATORID, mcreatorID);
        args.put(KEY_CREATED, mcreated);
        args.put(KEY_NAME, mname);
        return db.update(DATABASE_TABLE, args, KEY_NAME + "=" + mname, null) > 0;
    }
}

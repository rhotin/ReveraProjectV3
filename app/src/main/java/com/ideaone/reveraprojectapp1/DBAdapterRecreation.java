package com.ideaone.reveraprojectapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterRecreation {
    static final String KEY_ROWID = "_id";
    private static final String KEY_CREATORID = "creatorID";
    private static final String KEY_DISPLAYID = "displayID";
    private static final String KEY_DATE = "date";
    //1
    private static final String KEY_EV_1 = "event1";
    private static final String KEY_MIN_1 = "min1";
    private static final String KEY_HR_1 = "hr1";
    private static final String KEY_ACTIVITY_1 = "activity1";
    private static final String KEY_LOCATION_1 = "location1";
    //2
    private static final String KEY_EV_2 = "event2";
    private static final String KEY_MIN_2 = "min2";
    private static final String KEY_HR_2 = "hr2";
    private static final String KEY_ACTIVITY_2 = "activity2";
    private static final String KEY_LOCATION_2 = "location2";
    //3
    private static final String KEY_EV_3 = "event3";
    private static final String KEY_MIN_3 = "min3";
    private static final String KEY_HR_3 = "hr3";
    private static final String KEY_ACTIVITY_3 = "activity3";
    private static final String KEY_LOCATION_3 = "location3";
    //4
    private static final String KEY_EV_4 = "event4";
    private static final String KEY_MIN_4 = "min4";
    private static final String KEY_HR_4 = "hr4";
    private static final String KEY_ACTIVITY_4 = "activity4";
    private static final String KEY_LOCATION_4 = "location4";
    //5
    private static final String KEY_EV_5 = "event5";
    private static final String KEY_MIN_5 = "min5";
    private static final String KEY_HR_5 = "hr5";
    private static final String KEY_ACTIVITY_5 = "activity5";
    private static final String KEY_LOCATION_5 = "location5";
    //6
    private static final String KEY_EV_6 = "event6";
    private static final String KEY_MIN_6 = "min6";
    private static final String KEY_HR_6 = "hr6";
    private static final String KEY_ACTIVITY_6 = "activity6";
    private static final String KEY_LOCATION_6 = "location6";
    //7
    private static final String KEY_EV_7 = "event7";
    private static final String KEY_MIN_7 = "min7";
    private static final String KEY_HR_7 = "hr7";
    private static final String KEY_ACTIVITY_7 = "activity7";
    private static final String KEY_LOCATION_7 = "location7";
    //8
    private static final String KEY_EV_8 = "event8";
    private static final String KEY_MIN_8 = "min8";
    private static final String KEY_HR_8 = "hr8";
    private static final String KEY_ACTIVITY_8 = "activity8";
    private static final String KEY_LOCATION_8 = "location8";
    //9
    private static final String KEY_EV_9 = "event9";
    private static final String KEY_MIN_9 = "min9";
    private static final String KEY_HR_9 = "hr9";
    private static final String KEY_ACTIVITY_9 = "activity9";
    private static final String KEY_LOCATION_9 = "location9";
    //10
    private static final String KEY_EV_10 = "event10";
    private static final String KEY_MIN_10 = "min10";
    private static final String KEY_HR_10 = "hr10";
    private static final String KEY_ACTIVITY_10 = "activity10";
    private static final String KEY_LOCATION_10 = "location10";

    private static final String KEY_HANDLE = "handle";
    private static final String KEY_ID = "menuID";

    private static final String DATABASE_NAME = "MyDBRec";
    private static final String DATABASE_TABLE = "recItems";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    + KEY_CREATORID + " text, "
                    + KEY_DISPLAYID + " text, "
                    + KEY_DATE + " text, "
                    + KEY_EV_1 + " text, "
                    + KEY_MIN_1 + " text, "
                    + KEY_HR_1 + " text, "
                    + KEY_ACTIVITY_1 + " text, "
                    + KEY_LOCATION_1 + " text, "
                    + KEY_EV_2 + " text, "
                    + KEY_MIN_2 + " text, "
                    + KEY_HR_2 + " text, "
                    + KEY_ACTIVITY_2 + " text, "
                    + KEY_LOCATION_2 + " text, "
                    + KEY_EV_3 + " text, "
                    + KEY_MIN_3 + " text, "
                    + KEY_HR_3 + " text, "
                    + KEY_ACTIVITY_3 + " text, "
                    + KEY_LOCATION_3 + " text, "
                    + KEY_EV_4 + " text, "
                    + KEY_MIN_4 + " text, "
                    + KEY_HR_4 + " text, "
                    + KEY_ACTIVITY_4 + " text, "
                    + KEY_LOCATION_4 + " text, "
                    + KEY_EV_5 + " text, "
                    + KEY_MIN_5 + " text, "
                    + KEY_HR_5 + " text, "
                    + KEY_ACTIVITY_5 + " text, "
                    + KEY_LOCATION_5 + " text, "
                    + KEY_EV_6 + " text, "
                    + KEY_MIN_6 + " text, "
                    + KEY_HR_6 + " text, "
                    + KEY_ACTIVITY_6 + " text, "
                    + KEY_LOCATION_6 + " text, "
                    + KEY_EV_7 + " text, "
                    + KEY_MIN_7 + " text, "
                    + KEY_HR_7 + " text, "
                    + KEY_ACTIVITY_7 + " text, "
                    + KEY_LOCATION_7 + " text, "
                    + KEY_EV_8 + " text, "
                    + KEY_MIN_8 + " text, "
                    + KEY_HR_8 + " text, "
                    + KEY_ACTIVITY_8 + " text, "
                    + KEY_LOCATION_8 + " text, "
                    + KEY_EV_9 + " text, "
                    + KEY_MIN_9 + " text, "
                    + KEY_HR_9 + " text, "
                    + KEY_ACTIVITY_9 + " text, "
                    + KEY_LOCATION_9 + " text, "
                    + KEY_EV_10 + " text, "
                    + KEY_MIN_10 + " text, "
                    + KEY_HR_10 + " text, "
                    + KEY_ACTIVITY_10 + " text, "
                    + KEY_LOCATION_10 + " text, "
                    + KEY_HANDLE + " text, "
                    + KEY_ID + " text);";

    final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapterRecreation(Context ctx) {
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
    public DBAdapterRecreation open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    public void resetDB() {
        db.execSQL("DROP TABLE IF EXISTS favItems");
        try {
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---insert a contact into the database---
    public void insertItem(String creatorID, String displayID, String date,
                           String ev_1, String min_1, String hr_1, String activity_1, String location_1,
                           String ev_2, String min_2, String hr_2, String activity_2, String location_2,
                           String ev_3, String min_3, String hr_3, String activity_3, String location_3,
                           String ev_4, String min_4, String hr_4, String activity_4, String location_4,
                           String ev_5, String min_5, String hr_5, String activity_5, String location_5,
                           String ev_6, String min_6, String hr_6, String activity_6, String location_6,
                           String ev_7, String min_7, String hr_7, String activity_7, String location_7,
                           String ev_8, String min_8, String hr_8, String activity_8, String location_8,
                           String ev_9, String min_9, String hr_9, String activity_9, String location_9,
                           String ev_10, String min_10, String hr_10, String activity_10, String location_10,
                           String handle, String menuID) {
        ContentValues insertValues = new ContentValues();
        insertValues.put(KEY_CREATORID, creatorID);
        insertValues.put(KEY_DISPLAYID, displayID);
        insertValues.put(KEY_DATE, date);
        //1
        insertValues.put(KEY_EV_1, ev_1);
        insertValues.put(KEY_MIN_1, min_1);
        insertValues.put(KEY_HR_1, hr_1);
        insertValues.put(KEY_ACTIVITY_1, activity_1);
        insertValues.put(KEY_LOCATION_1, location_1);
        //2
        insertValues.put(KEY_EV_2, ev_2);
        insertValues.put(KEY_MIN_2, min_2);
        insertValues.put(KEY_HR_2, hr_2);
        insertValues.put(KEY_ACTIVITY_2, activity_2);
        insertValues.put(KEY_LOCATION_2, location_2);
        //3
        insertValues.put(KEY_EV_3, ev_3);
        insertValues.put(KEY_MIN_3, min_3);
        insertValues.put(KEY_HR_3, hr_3);
        insertValues.put(KEY_ACTIVITY_3, activity_3);
        insertValues.put(KEY_LOCATION_3, location_3);
        //4
        insertValues.put(KEY_EV_4, ev_4);
        insertValues.put(KEY_MIN_4, min_4);
        insertValues.put(KEY_HR_4, hr_4);
        insertValues.put(KEY_ACTIVITY_4, activity_4);
        insertValues.put(KEY_LOCATION_4, location_4);
        //5
        insertValues.put(KEY_EV_5, ev_5);
        insertValues.put(KEY_MIN_5, min_5);
        insertValues.put(KEY_HR_5, hr_5);
        insertValues.put(KEY_ACTIVITY_5, activity_5);
        insertValues.put(KEY_LOCATION_5, location_5);
        //6
        insertValues.put(KEY_EV_6, ev_6);
        insertValues.put(KEY_MIN_6, min_6);
        insertValues.put(KEY_HR_6, hr_6);
        insertValues.put(KEY_ACTIVITY_6, activity_6);
        insertValues.put(KEY_LOCATION_6, location_6);
        //7
        insertValues.put(KEY_EV_7, ev_7);
        insertValues.put(KEY_MIN_7, min_7);
        insertValues.put(KEY_HR_7, hr_7);
        insertValues.put(KEY_ACTIVITY_7, activity_7);
        insertValues.put(KEY_LOCATION_7, location_7);
        //8
        insertValues.put(KEY_EV_8, ev_8);
        insertValues.put(KEY_MIN_8, min_8);
        insertValues.put(KEY_HR_8, hr_8);
        insertValues.put(KEY_ACTIVITY_8, activity_8);
        insertValues.put(KEY_LOCATION_8, location_8);
        //9
        insertValues.put(KEY_EV_9, ev_9);
        insertValues.put(KEY_MIN_9, min_9);
        insertValues.put(KEY_HR_9, hr_9);
        insertValues.put(KEY_ACTIVITY_9, activity_9);
        insertValues.put(KEY_LOCATION_9, location_9);
        //100
        insertValues.put(KEY_EV_10, ev_10);
        insertValues.put(KEY_MIN_10, min_10);
        insertValues.put(KEY_HR_10, hr_10);
        insertValues.put(KEY_ACTIVITY_10, activity_10);
        insertValues.put(KEY_LOCATION_10, location_10);

        insertValues.put(KEY_HANDLE, handle);
        insertValues.put(KEY_ID, menuID);
        db.insertOrThrow(DATABASE_TABLE, null, insertValues);
    }

    //---deletes a particular contact---
    public boolean deleteItem(String date) {
        return db.delete(DATABASE_TABLE, KEY_DATE + "='" + date + "'", null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllItems() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_DISPLAYID, KEY_DATE,
                KEY_EV_1, KEY_MIN_1, KEY_HR_1, KEY_ACTIVITY_1, KEY_LOCATION_1,
                KEY_EV_2, KEY_MIN_2, KEY_HR_2, KEY_ACTIVITY_2, KEY_LOCATION_2,
                KEY_EV_3, KEY_MIN_3, KEY_HR_3, KEY_ACTIVITY_3, KEY_LOCATION_3,
                KEY_EV_4, KEY_MIN_4, KEY_HR_4, KEY_ACTIVITY_4, KEY_LOCATION_4,
                KEY_EV_5, KEY_MIN_5, KEY_HR_5, KEY_ACTIVITY_5, KEY_LOCATION_5,
                KEY_EV_6, KEY_MIN_6, KEY_HR_6, KEY_ACTIVITY_6, KEY_LOCATION_6,
                KEY_EV_7, KEY_MIN_7, KEY_HR_7, KEY_ACTIVITY_7, KEY_LOCATION_7,
                KEY_EV_8, KEY_MIN_8, KEY_HR_8, KEY_ACTIVITY_8, KEY_LOCATION_8,
                KEY_EV_9, KEY_MIN_9, KEY_HR_9, KEY_ACTIVITY_9, KEY_LOCATION_9,
                KEY_EV_10, KEY_MIN_10, KEY_HR_10, KEY_ACTIVITY_10, KEY_LOCATION_10,
                KEY_HANDLE, KEY_ID}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getItem(String date) throws SQLException {
        return db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_DISPLAYID, KEY_DATE,
                        KEY_EV_1, KEY_MIN_1, KEY_HR_1, KEY_ACTIVITY_1, KEY_LOCATION_1,
                        KEY_EV_2, KEY_MIN_2, KEY_HR_2, KEY_ACTIVITY_2, KEY_LOCATION_2,
                        KEY_EV_3, KEY_MIN_3, KEY_HR_3, KEY_ACTIVITY_3, KEY_LOCATION_3,
                        KEY_EV_4, KEY_MIN_4, KEY_HR_4, KEY_ACTIVITY_4, KEY_LOCATION_4,
                        KEY_EV_5, KEY_MIN_5, KEY_HR_5, KEY_ACTIVITY_5, KEY_LOCATION_5,
                        KEY_EV_6, KEY_MIN_6, KEY_HR_6, KEY_ACTIVITY_6, KEY_LOCATION_6,
                        KEY_EV_7, KEY_MIN_7, KEY_HR_7, KEY_ACTIVITY_7, KEY_LOCATION_7,
                        KEY_EV_8, KEY_MIN_8, KEY_HR_8, KEY_ACTIVITY_8, KEY_LOCATION_8,
                        KEY_EV_9, KEY_MIN_9, KEY_HR_9, KEY_ACTIVITY_9, KEY_LOCATION_9,
                        KEY_EV_10, KEY_MIN_10, KEY_HR_10, KEY_ACTIVITY_10, KEY_LOCATION_10,
                        KEY_HANDLE, KEY_ID}, KEY_DATE + "='" + date + "'", null,
                null, null, null, null);
    }

    //---updates a contact---
    public boolean updateItem(String creatorID, String displayID, String date,
                              String ev_1, String min_1, String hr_1, String activity_1, String location_1,
                              String ev_2, String min_2, String hr_2, String activity_2, String location_2,
                              String ev_3, String min_3, String hr_3, String activity_3, String location_3,
                              String ev_4, String min_4, String hr_4, String activity_4, String location_4,
                              String ev_5, String min_5, String hr_5, String activity_5, String location_5,
                              String ev_6, String min_6, String hr_6, String activity_6, String location_6,
                              String ev_7, String min_7, String hr_7, String activity_7, String location_7,
                              String ev_8, String min_8, String hr_8, String activity_8, String location_8,
                              String ev_9, String min_9, String hr_9, String activity_9, String location_9,
                              String ev_10, String min_10, String hr_10, String activity_10, String location_10,
                              String handle, String menuID) {
        ContentValues args = new ContentValues();
        args.put(KEY_CREATORID, creatorID);
        args.put(KEY_DISPLAYID, displayID);
        args.put(KEY_DATE, date);
        //1
        args.put(KEY_EV_1, ev_1);
        args.put(KEY_MIN_1, min_1);
        args.put(KEY_HR_1, hr_1);
        args.put(KEY_ACTIVITY_1, activity_1);
        args.put(KEY_LOCATION_1, location_1);
        //2
        args.put(KEY_EV_2, ev_2);
        args.put(KEY_MIN_2, min_2);
        args.put(KEY_HR_2, hr_2);
        args.put(KEY_ACTIVITY_2, activity_2);
        args.put(KEY_LOCATION_2, location_2);
        //3
        args.put(KEY_EV_3, ev_3);
        args.put(KEY_MIN_3, min_3);
        args.put(KEY_HR_3, hr_3);
        args.put(KEY_ACTIVITY_3, activity_3);
        args.put(KEY_LOCATION_3, location_3);
        //4
        args.put(KEY_EV_4, ev_4);
        args.put(KEY_MIN_4, min_4);
        args.put(KEY_HR_4, hr_4);
        args.put(KEY_ACTIVITY_4, activity_4);
        args.put(KEY_LOCATION_4, location_4);
        //5
        args.put(KEY_EV_5, ev_5);
        args.put(KEY_MIN_5, min_5);
        args.put(KEY_HR_5, hr_5);
        args.put(KEY_ACTIVITY_5, activity_5);
        args.put(KEY_LOCATION_5, location_5);
        //6
        args.put(KEY_EV_6, ev_6);
        args.put(KEY_MIN_6, min_6);
        args.put(KEY_HR_6, hr_6);
        args.put(KEY_ACTIVITY_6, activity_6);
        args.put(KEY_LOCATION_6, location_6);
        //7
        args.put(KEY_EV_7, ev_7);
        args.put(KEY_MIN_7, min_7);
        args.put(KEY_HR_7, hr_7);
        args.put(KEY_ACTIVITY_7, activity_7);
        args.put(KEY_LOCATION_7, location_7);
        //8
        args.put(KEY_EV_8, ev_8);
        args.put(KEY_MIN_8, min_8);
        args.put(KEY_HR_8, hr_8);
        args.put(KEY_ACTIVITY_8, activity_8);
        args.put(KEY_LOCATION_8, location_8);
        //9
        args.put(KEY_EV_9, ev_9);
        args.put(KEY_MIN_9, min_9);
        args.put(KEY_HR_9, hr_9);
        args.put(KEY_ACTIVITY_9, activity_9);
        args.put(KEY_LOCATION_9, location_9);
        //100
        args.put(KEY_EV_10, ev_10);
        args.put(KEY_MIN_10, min_10);
        args.put(KEY_HR_10, hr_10);
        args.put(KEY_ACTIVITY_10, activity_10);
        args.put(KEY_LOCATION_10, location_10);

        args.put(KEY_HANDLE, handle);
        args.put(KEY_ID, menuID);
        return db.update(DATABASE_TABLE, args, KEY_DATE + "='" + date + "'", null) > 0;
    }
}

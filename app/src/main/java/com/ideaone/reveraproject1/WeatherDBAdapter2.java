package com.ideaone.reveraproject1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WeatherDBAdapter2 {
    static final String KEY_ROWID = "_id";
    static final String KEY_LATITUDE = "latitude";
    static final String KEY_LONGITUDE = "longitude";
    static final String KEY_LOCATION_NAME = "location";
    //today weather
    static final String KEY_1_TIME = "time1";
    static final String KEY_1_SUMMARY_C = "summary1C";
    static final String KEY_1_ICON_C = "icon1C";
    static final String KEY_1_SUMMARY = "summary1";
    static final String KEY_1_ICON = "icon1";
    static final String KEY_1_TEMPERATURE_DAY = "temperature1";
    static final String KEY_1_TEMPERATURE_MIN = "temperature1min";
    static final String KEY_1_TEMPERATURE_MAX = "temperature1max";
    static final String KEY_1_TEMPERATURE_NIGHT = "temperature1night";
    static final String KEY_1_TEMPERATURE_EVE = "temperature1eve";
    static final String KEY_1_TEMPERATURE_MORN = "temperature1morn";
    //tomorrow weather
    static final String KEY_2_TIME = "time2";
    static final String KEY_2_SUMMARY = "summary2";
    static final String KEY_2_ICON = "icon2";
    static final String KEY_2_TEMPERATURE_DAY = "temperature2";
    static final String KEY_2_TEMPERATURE_MIN = "temperature2min";
    static final String KEY_2_TEMPERATURE_MAX = "temperature2max";
    static final String KEY_2_TEMPERATURE_NIGHT = "temperature2night";
    static final String KEY_2_TEMPERATURE_EVE = "temperature2eve";
    static final String KEY_2_TEMPERATURE_MORN = "temperature2morn";
    //3rd day weather
    static final String KEY_3_TIME = "time3";
    static final String KEY_3_SUMMARY = "summary3";
    static final String KEY_3_ICON = "icon3";
    static final String KEY_3_TEMPERATURE_DAY = "temperature3";
    static final String KEY_3_TEMPERATURE_MIN = "temperature3min";
    static final String KEY_3_TEMPERATURE_MAX = "temperature3max";
    static final String KEY_3_TEMPERATURE_NIGHT = "temperature3night";
    static final String KEY_3_TEMPERATURE_EVE = "temperature3eve";
    static final String KEY_3_TEMPERATURE_MORN = "temperature3morn";
    //4th day weather
    static final String KEY_4_TIME = "time4";
    static final String KEY_4_SUMMARY = "summary4";
    static final String KEY_4_ICON = "icon4";
    static final String KEY_4_TEMPERATURE_DAY = "temperature4";
    static final String KEY_4_TEMPERATURE_MIN = "temperature4min";
    static final String KEY_4_TEMPERATURE_MAX = "temperature4max";
    static final String KEY_4_TEMPERATURE_NIGHT = "temperature4night";
    static final String KEY_4_TEMPERATURE_EVE = "temperature4eve";
    static final String KEY_4_TEMPERATURE_MORN = "temperature4morn";


    static final String DATABASE_NAME = "MyWeatherDB2";
    static final String DATABASE_TABLE = "weatherDB2";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            "create table if not exists " + DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_LATITUDE + " DOUBLE, "
                    + KEY_LONGITUDE + " DOUBLE, "
                    + KEY_LOCATION_NAME + " text, "
                    + KEY_1_TIME + " INTEGER, "
                    + KEY_1_SUMMARY_C + " text, "
                    + KEY_1_ICON_C + " text, "
                    + KEY_1_SUMMARY + " text, "
                    + KEY_1_ICON + " text, "
                    + KEY_1_TEMPERATURE_DAY + " DOUBLE, "
                    + KEY_1_TEMPERATURE_MIN + " DOUBLE, "
                    + KEY_1_TEMPERATURE_MAX + " DOUBLE, "
                    + KEY_1_TEMPERATURE_NIGHT + " DOUBLE, "
                    + KEY_1_TEMPERATURE_EVE + " DOUBLE, "
                    + KEY_1_TEMPERATURE_MORN + " DOUBLE, "
                    + KEY_2_TIME + " INTEGER, "
                    + KEY_2_SUMMARY + " text, "
                    + KEY_2_ICON + " text, "
                    + KEY_2_TEMPERATURE_DAY + " DOUBLE, "
                    + KEY_2_TEMPERATURE_MIN + " DOUBLE, "
                    + KEY_2_TEMPERATURE_MAX + " DOUBLE, "
                    + KEY_2_TEMPERATURE_NIGHT + " DOUBLE, "
                    + KEY_2_TEMPERATURE_EVE + " DOUBLE, "
                    + KEY_2_TEMPERATURE_MORN + " DOUBLE, "
                    + KEY_3_TIME + " INTEGER, "
                    + KEY_3_SUMMARY + " text, "
                    + KEY_3_ICON + " text, "
                    + KEY_3_TEMPERATURE_DAY + " DOUBLE, "
                    + KEY_3_TEMPERATURE_MIN + " DOUBLE, "
                    + KEY_3_TEMPERATURE_MAX + " DOUBLE, "
                    + KEY_3_TEMPERATURE_NIGHT + " DOUBLE, "
                    + KEY_3_TEMPERATURE_EVE + " DOUBLE, "
                    + KEY_3_TEMPERATURE_MORN + " DOUBLE, "
                    + KEY_4_TIME + " INTEGER, "
                    + KEY_4_SUMMARY + " text, "
                    + KEY_4_ICON + " text, "
                    + KEY_4_TEMPERATURE_DAY + " DOUBLE, "
                    + KEY_4_TEMPERATURE_MIN + " DOUBLE, "
                    + KEY_4_TEMPERATURE_MAX + " DOUBLE, "
                    + KEY_4_TEMPERATURE_NIGHT + " DOUBLE, "
                    + KEY_4_TEMPERATURE_EVE + " DOUBLE, "
                    + KEY_4_TEMPERATURE_MORN + " DOUBLE "
                    + ");";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public WeatherDBAdapter2(Context ctx) {
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
            Log.w("WeatherDBAdapter", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    //---opens the database---
    public WeatherDBAdapter2 open() throws SQLException {
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

    //---insert a contact into the database---
    public void insertItem(double mLat, double mLong, String mLocation,
                           int m1Time, String m1SummaryC, String m1IconC, String m1Summary, String m1Icon, double m1Temp, double m1TempMin,
                           double m1TempMax, double m1TempNight, double m1TempEve, double m1TempMorn,
                           int m2Time, String m2Summary, String m2Icon, double m2Temp, double m2TempMin,
                           double m2TempMax, double m2TempNight, double m2TempEve, double m2TempMorn,
                           int m3Time, String m3Summary, String m3Icon, double m3Temp, double m3TempMin,
                           double m3TempMax, double m3TempNight, double m3TempEve, double m3TempMorn,
                           int m4Time, String m4Summary, String m4Icon, double m4Temp, double m4TempMin,
                           double m4TempMax, double m4TempNight, double m4TempEve, double m4TempMorn) {
        //  db.execSQL(DATABASE_CREATE);
        ContentValues insertValues = new ContentValues();
        insertValues.put(KEY_LATITUDE, mLat);
        insertValues.put(KEY_LONGITUDE, mLong);
        insertValues.put(KEY_LOCATION_NAME, mLocation);
        insertValues.put(KEY_1_TIME, m1Time);
        insertValues.put(KEY_1_SUMMARY_C, m1SummaryC);
        insertValues.put(KEY_1_ICON_C, m1IconC);
        insertValues.put(KEY_1_SUMMARY, m1Summary);
        insertValues.put(KEY_1_ICON, m1Icon);
        insertValues.put(KEY_1_TEMPERATURE_DAY, m1Temp);
        insertValues.put(KEY_1_TEMPERATURE_MIN, m1TempMin);
        insertValues.put(KEY_1_TEMPERATURE_MAX, m1TempMax);
        insertValues.put(KEY_1_TEMPERATURE_NIGHT, m1TempNight);
        insertValues.put(KEY_1_TEMPERATURE_EVE, m1TempEve);
        insertValues.put(KEY_1_TEMPERATURE_MORN, m1TempMorn);
        insertValues.put(KEY_2_TIME, m2Time);
        insertValues.put(KEY_2_SUMMARY, m2Summary);
        insertValues.put(KEY_2_ICON, m2Icon);
        insertValues.put(KEY_2_TEMPERATURE_DAY, m2Temp);
        insertValues.put(KEY_2_TEMPERATURE_MIN, m2TempMin);
        insertValues.put(KEY_2_TEMPERATURE_MAX, m2TempMax);
        insertValues.put(KEY_2_TEMPERATURE_NIGHT, m2TempNight);
        insertValues.put(KEY_2_TEMPERATURE_EVE, m2TempEve);
        insertValues.put(KEY_2_TEMPERATURE_MORN, m2TempMorn);
        insertValues.put(KEY_3_TIME, m3Time);
        insertValues.put(KEY_3_SUMMARY, m3Summary);
        insertValues.put(KEY_3_ICON, m3Icon);
        insertValues.put(KEY_3_TEMPERATURE_DAY, m3Temp);
        insertValues.put(KEY_3_TEMPERATURE_MIN, m3TempMin);
        insertValues.put(KEY_3_TEMPERATURE_MAX, m3TempMax);
        insertValues.put(KEY_3_TEMPERATURE_NIGHT, m3TempNight);
        insertValues.put(KEY_3_TEMPERATURE_EVE, m3TempEve);
        insertValues.put(KEY_3_TEMPERATURE_MORN, m3TempMorn);
        insertValues.put(KEY_4_TIME, m4Time);
        insertValues.put(KEY_4_SUMMARY, m4Summary);
        insertValues.put(KEY_4_ICON, m4Icon);
        insertValues.put(KEY_4_TEMPERATURE_DAY, m4Temp);
        insertValues.put(KEY_4_TEMPERATURE_MIN, m4TempMin);
        insertValues.put(KEY_4_TEMPERATURE_MAX, m4TempMax);
        insertValues.put(KEY_4_TEMPERATURE_NIGHT, m4TempNight);
        insertValues.put(KEY_4_TEMPERATURE_EVE, m4TempEve);
        insertValues.put(KEY_4_TEMPERATURE_MORN, m4TempMorn);
        db.insertOrThrow(DATABASE_TABLE, null, insertValues);
    }

    //---retrieves all the contacts---
    public Cursor getAllItems() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_LATITUDE, KEY_LONGITUDE, KEY_LOCATION_NAME,
                        KEY_1_TIME, KEY_1_SUMMARY_C, KEY_1_ICON_C, KEY_1_SUMMARY, KEY_1_ICON, KEY_1_TEMPERATURE_DAY, KEY_1_TEMPERATURE_MIN,
                        KEY_1_TEMPERATURE_MAX, KEY_1_TEMPERATURE_NIGHT, KEY_1_TEMPERATURE_EVE, KEY_1_TEMPERATURE_MORN,
                        KEY_2_TIME, KEY_2_SUMMARY, KEY_2_ICON, KEY_2_TEMPERATURE_DAY, KEY_2_TEMPERATURE_MIN,
                        KEY_2_TEMPERATURE_MAX, KEY_2_TEMPERATURE_NIGHT, KEY_2_TEMPERATURE_EVE, KEY_2_TEMPERATURE_MORN,
                        KEY_3_TIME, KEY_3_SUMMARY, KEY_3_ICON, KEY_3_TEMPERATURE_DAY, KEY_3_TEMPERATURE_MIN,
                        KEY_3_TEMPERATURE_MAX, KEY_3_TEMPERATURE_NIGHT, KEY_3_TEMPERATURE_EVE, KEY_3_TEMPERATURE_MORN,
                        KEY_4_TIME, KEY_4_SUMMARY, KEY_4_ICON, KEY_4_TEMPERATURE_DAY, KEY_4_TEMPERATURE_MIN,
                        KEY_4_TEMPERATURE_MAX, KEY_4_TEMPERATURE_NIGHT, KEY_4_TEMPERATURE_EVE, KEY_4_TEMPERATURE_MORN},
                null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getItem(double mLat, double mLong, double cTemperature) throws SQLException {
        return db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_LATITUDE, KEY_LONGITUDE, KEY_LOCATION_NAME,
                        KEY_1_TIME, KEY_1_SUMMARY_C, KEY_1_ICON_C, KEY_1_SUMMARY, KEY_1_ICON, KEY_1_TEMPERATURE_DAY, KEY_1_TEMPERATURE_MIN,
                        KEY_1_TEMPERATURE_MAX, KEY_1_TEMPERATURE_NIGHT, KEY_1_TEMPERATURE_EVE, KEY_1_TEMPERATURE_MORN,
                        KEY_2_TIME, KEY_2_SUMMARY, KEY_2_ICON, KEY_2_TEMPERATURE_DAY, KEY_2_TEMPERATURE_MIN,
                        KEY_2_TEMPERATURE_MAX, KEY_2_TEMPERATURE_NIGHT, KEY_2_TEMPERATURE_EVE, KEY_2_TEMPERATURE_MORN,
                        KEY_3_TIME, KEY_3_SUMMARY, KEY_3_ICON, KEY_3_TEMPERATURE_DAY, KEY_3_TEMPERATURE_MIN,
                        KEY_3_TEMPERATURE_MAX, KEY_3_TEMPERATURE_NIGHT, KEY_3_TEMPERATURE_EVE, KEY_3_TEMPERATURE_MORN,
                        KEY_4_TIME, KEY_4_SUMMARY, KEY_4_ICON, KEY_4_TEMPERATURE_DAY, KEY_4_TEMPERATURE_MIN,
                        KEY_4_TEMPERATURE_MAX, KEY_4_TEMPERATURE_NIGHT, KEY_4_TEMPERATURE_EVE, KEY_4_TEMPERATURE_MORN},
                KEY_1_TEMPERATURE_DAY + "='" + cTemperature + "'" +
                        " AND " + KEY_LATITUDE + "='" + mLat + "'" +
                        " AND " + KEY_LONGITUDE + "='" + mLong + "'", null,
                null, null, null, null);
    }
}

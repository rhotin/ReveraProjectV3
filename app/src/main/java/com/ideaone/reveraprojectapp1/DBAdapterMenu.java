package com.ideaone.reveraprojectapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBAdapterMenu {
    private static final String KEY_ROWID = "_id";
    private static final String KEY_CREATORID = "creatorID";
    private static final String KEY_MODIFIED = "modified";
    private static final String KEY_DISPLAYID = "displayID";
    private static final String KEY_DATE = "date";
    private static final String KEY_LAUNCH_SOUP = "lunchSoup";
    private static final String KEY_LAUNCH_SALAD = "lunchSalad";
    private static final String KEY_LAUNCH_ENTREE1 = "lunchEntree1";
    private static final String KEY_LAUNCH_ENTREE2 = "lunchEntree2";
    private static final String KEY_LAUNCH_DESSERT = "lunchDessert";
    private static final String KEY_LAUNCH_OTHER = "lunchOther";
    private static final String KEY_DINNER_SOUP = "dinnerSoup";
    private static final String KEY_DINNER_SALAD = "dinnerSalad";
    private static final String KEY_DINNER_ENTREE1 = "dinnerEntree1";
    private static final String KEY_DINNER_ENTREE2 = "dinnerEntree2";
    private static final String KEY_DINNER_POTATO = "dinnerPotato";
    private static final String KEY_DINNER_VEG = "dinnerVeg";
    private static final String KEY_DINNER_DESSERT = "dinnerDessert";
    private static final String KEY_DINNER_OTHER = "dinnerOther";
    private static final String KEY_HANDLE = "handle";
    private static final String KEY_ID = "menuID";
    //  private static final String KEY_JSON_MENU = "jsonFile";
    //  private static final String KEY_TIME = "timeUpdated";

    private static final String DATABASE_NAME = "MyDBMenu";
    private static final String DATABASE_TABLE = "menuItems";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    + KEY_CREATORID + " text, "
                    + KEY_MODIFIED + " text, "
                    + KEY_DISPLAYID + " text, "
                    + KEY_DATE + " text, "
                    + KEY_LAUNCH_SOUP + " text, "
                    + KEY_LAUNCH_SALAD + " text, "
                    + KEY_LAUNCH_ENTREE1 + " text, "
                    + KEY_LAUNCH_ENTREE2 + " text, "
                    + KEY_LAUNCH_DESSERT + " text, "
                    + KEY_LAUNCH_OTHER + " text, "
                    + KEY_DINNER_SOUP + " text, "
                    + KEY_DINNER_SALAD + " text, "
                    + KEY_DINNER_ENTREE1 + " text, "
                    + KEY_DINNER_ENTREE2 + " text, "
                    + KEY_DINNER_POTATO + " text, "
                    + KEY_DINNER_VEG + " text, "
                    + KEY_DINNER_DESSERT + " text, "
                    + KEY_DINNER_OTHER + " text, "
                    + KEY_HANDLE + " text, "
                    + KEY_ID + " text);";

    final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    DBAdapterMenu(Context ctx) {
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
    public DBAdapterMenu open() throws SQLException {
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
    public void insertItem(String creatorID, String modified_, String displayID, String date, String lunchSoup, String lunchSalad,
                           String lunchEntree1, String lunchEntree2, String lunchDessert, String lunchOther,
                           String dinnerSoup, String dinnerSalad, String dinnerEntree1, String dinnerEntree2,
                           String dinnerPotato, String dinnerVeg, String dinnerDessert, String dinnerOther,
                           String handle, String menuID) {
        ContentValues insertValues = new ContentValues();

        insertValues.put(KEY_CREATORID, creatorID);
        insertValues.put(KEY_MODIFIED, modified_);
        insertValues.put(KEY_DISPLAYID, displayID);
        insertValues.put(KEY_DATE, date);
        insertValues.put(KEY_LAUNCH_SOUP, lunchSoup);
        insertValues.put(KEY_LAUNCH_SALAD, lunchSalad);
        insertValues.put(KEY_LAUNCH_ENTREE1, lunchEntree1);
        insertValues.put(KEY_LAUNCH_ENTREE2, lunchEntree2);
        insertValues.put(KEY_LAUNCH_DESSERT, lunchDessert);
        insertValues.put(KEY_LAUNCH_OTHER, lunchOther);
        insertValues.put(KEY_DINNER_SOUP, dinnerSoup);
        insertValues.put(KEY_DINNER_SALAD, dinnerSalad);
        insertValues.put(KEY_DINNER_ENTREE1, dinnerEntree1);
        insertValues.put(KEY_DINNER_ENTREE2, dinnerEntree2);
        insertValues.put(KEY_DINNER_POTATO, dinnerPotato);
        insertValues.put(KEY_DINNER_VEG, dinnerVeg);
        insertValues.put(KEY_DINNER_DESSERT, dinnerDessert);
        insertValues.put(KEY_DINNER_OTHER, dinnerOther);
        insertValues.put(KEY_HANDLE, handle);
        insertValues.put(KEY_ID, menuID);
        db.insertOrThrow(DATABASE_TABLE, null, insertValues);
    }

    //---deletes a particular contact---
    public boolean deleteItem(String lunchSoup) {
        return db.delete(DATABASE_TABLE, KEY_LAUNCH_SOUP + "='" + lunchSoup + "'", null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllItems() {
       /* try {
            dbAlb.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_MODIFIED, KEY_DISPLAYID, KEY_DATE, KEY_LAUNCH_SOUP,
                KEY_LAUNCH_SALAD, KEY_LAUNCH_ENTREE1, KEY_LAUNCH_ENTREE2, KEY_LAUNCH_DESSERT, KEY_LAUNCH_OTHER,
                KEY_DINNER_SOUP, KEY_DINNER_SALAD, KEY_DINNER_ENTREE1, KEY_DINNER_ENTREE2, KEY_DINNER_POTATO,
                KEY_DINNER_VEG, KEY_DINNER_DESSERT, KEY_DINNER_OTHER, KEY_HANDLE, KEY_ID}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getItem(String date) throws SQLException {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_MODIFIED, KEY_DISPLAYID, KEY_DATE, KEY_LAUNCH_SOUP,
                        KEY_LAUNCH_SALAD, KEY_LAUNCH_ENTREE1, KEY_LAUNCH_ENTREE2, KEY_LAUNCH_DESSERT, KEY_LAUNCH_OTHER,
                        KEY_DINNER_SOUP, KEY_DINNER_SALAD, KEY_DINNER_ENTREE1, KEY_DINNER_ENTREE2, KEY_DINNER_POTATO,
                        KEY_DINNER_VEG, KEY_DINNER_DESSERT, KEY_DINNER_OTHER, KEY_HANDLE, KEY_ID},
                KEY_DATE + "='" + date + "'", null, null, null, null, null);
    }

    //---updates a contact---
    public boolean updateItem(String creatorID, String modified_, String displayId, String date, String lunchSoup, String lunchSalad,
                              String lunchEntree1, String lunchEntree2, String lunchDessert, String lunchOther,
                              String dinnerSoup, String dinnerSalad, String dinnerEntree1, String dinnerEntree2,
                              String dinnerPotato, String dinnerVeg, String dinnerDessert, String dinnerOther,
                              String handle, String menuID) {
        ContentValues args = new ContentValues();
        args.put(KEY_CREATORID, creatorID);
        args.put(KEY_MODIFIED, modified_);
        args.put(KEY_DISPLAYID, displayId);
        args.put(KEY_DATE, date);
        args.put(KEY_LAUNCH_SOUP, lunchSoup);
        args.put(KEY_LAUNCH_SALAD, lunchSalad);
        args.put(KEY_LAUNCH_ENTREE1, lunchEntree1);
        args.put(KEY_LAUNCH_ENTREE2, lunchEntree2);
        args.put(KEY_LAUNCH_DESSERT, lunchDessert);
        args.put(KEY_LAUNCH_OTHER, lunchOther);
        args.put(KEY_DINNER_SOUP, dinnerSoup);
        args.put(KEY_DINNER_SALAD, dinnerSalad);
        args.put(KEY_DINNER_ENTREE1, dinnerEntree1);
        args.put(KEY_DINNER_ENTREE2, dinnerEntree2);
        args.put(KEY_DINNER_POTATO, dinnerPotato);
        args.put(KEY_DINNER_VEG, dinnerVeg);
        args.put(KEY_DINNER_DESSERT, dinnerDessert);
        args.put(KEY_DINNER_OTHER, dinnerOther);
        args.put(KEY_HANDLE, handle);
        args.put(KEY_ID, menuID);

        return db.update(DATABASE_TABLE, args, KEY_DATE + "='" + date + "'", null) > 0;
    }
}

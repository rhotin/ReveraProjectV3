package com.ideaone.tabletapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterAlbums {
    private static final String KEY_ROWID = "_id";
    private static final String KEY_CREATORID = "creatorID";
    private static final String KEY_CREATED = "created";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DISPLAYID = "displayID";
    private static final String KEY_WEB = "web";
    private static final String KEY_DISPLAY = "display";
    private static final String KEY_MODIFIED = "modified";
    private static final String KEY_HANDLE = "handle";
    private static final String KEY_ID = "albumID";

    private static final String DATABASE_NAME = "MyDBAlbums";
    private static final String DATABASE_TABLE = "albumItems";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    + KEY_CREATORID + " text, "
                    + KEY_DISPLAYID + " text, "
                    + KEY_CREATED + " text, "
                    + KEY_TITLE + " text, "
                    + KEY_WEB + " text, "
                    + KEY_DISPLAY + " text, "
                    + KEY_MODIFIED + " text, "
                    + KEY_HANDLE + " text, "
                    + KEY_ID + " text);";

    final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapterAlbums(Context ctx) {
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
    public DBAdapterAlbums open() throws SQLException {
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
    public void insertItem(String creatorID, String created, String title, String displayID, String web,
                           String display, String modified, String handle, String menuID) {
        ContentValues insertValues = new ContentValues();

        insertValues.put(KEY_CREATORID, creatorID);
        insertValues.put(KEY_CREATED, created);
        insertValues.put(KEY_TITLE, title);
        insertValues.put(KEY_DISPLAYID, displayID);
        insertValues.put(KEY_WEB, web);
        insertValues.put(KEY_DISPLAY, display);
        insertValues.put(KEY_MODIFIED, modified);
        insertValues.put(KEY_HANDLE, handle);
        insertValues.put(KEY_ID, menuID);
        db.insertOrThrow(DATABASE_TABLE, null, insertValues);
    }

    //---deletes a particular contact---
    public boolean deleteItem(String lunchSoup) {
        return db.delete(DATABASE_TABLE, KEY_TITLE + "='" + lunchSoup + "'", null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllItems() {
       /* try {
            dbAlb.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_CREATED, KEY_TITLE,
                KEY_DISPLAYID, KEY_WEB, KEY_DISPLAY, KEY_MODIFIED, KEY_HANDLE, KEY_ID},null, null,
                null, null, KEY_CREATED);
    }

    //---retrieves a particular contact---
    public Cursor getItem(String date) throws SQLException {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_CREATED, KEY_TITLE,
                        KEY_DISPLAYID, KEY_WEB, KEY_DISPLAY, KEY_MODIFIED, KEY_HANDLE, KEY_ID},
                KEY_ID + "='" + date + "'", null, null, null, null, null);
    }

    //---updates a contact---
    public boolean updateItem(String creatorID, String created, String title, String displayId, String web,
                              String display, String modified, String handle, String menuID) {
        ContentValues args = new ContentValues();
        args.put(KEY_CREATORID, creatorID);
        args.put(KEY_CREATED, created);
        args.put(KEY_TITLE, title);
        args.put(KEY_DISPLAYID, displayId);
        args.put(KEY_WEB, web);
        args.put(KEY_DISPLAY, display);
        args.put(KEY_MODIFIED, modified);
        args.put(KEY_HANDLE, handle);
        args.put(KEY_ID, menuID);

        return db.update(DATABASE_TABLE, args, KEY_ID + "='" + menuID + "'", null) > 0;
    }
}

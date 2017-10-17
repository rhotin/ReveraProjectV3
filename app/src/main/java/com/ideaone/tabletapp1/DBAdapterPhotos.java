package com.ideaone.tabletapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterPhotos {
    private static final String KEY_ROWID = "_id";
    private static final String KEY_CREATORID = "creatorID";
    private static final String KEY_CREATED = "created";
    private static final String KEY_CLASS = "class";
    private static final String KEY_TYPE = "type";
    private static final String KEY_EXT = "ext";
    private static final String KEY_HANDLE = "handle";
    private static final String KEY_ID = "albumID";
    private static final String KEY_IMAGE250 = "thumb";
    private static final String KEY_IMAGEFULL = "image";

    private static final String DATABASE_NAME = "MyDBPhotos";
    private static final String DATABASE_TABLE = "photoItems";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    + KEY_CREATORID + " text, "
                    + KEY_TYPE + " text, "
                    + KEY_CREATED + " text, "
                    + KEY_CLASS + " text, "
                    + KEY_EXT + " text, "
                    + KEY_HANDLE + " text, "
                    + KEY_ID + " text, "
                    + KEY_IMAGE250 + " BLOB, "
                    + KEY_IMAGEFULL + " BLOB);";

    final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapterPhotos(Context ctx) {
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
    public DBAdapterPhotos open() throws SQLException {
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
    public void insertItem(String creatorID, String created, String mClass, String mType, String ext,
                           String handle, String photoID, byte[] image250, byte[] imageFull) {
        ContentValues insertValues = new ContentValues();

        insertValues.put(KEY_CREATORID, creatorID);
        insertValues.put(KEY_CREATED, created);
        insertValues.put(KEY_CLASS, mClass);
        insertValues.put(KEY_TYPE, mType);
        insertValues.put(KEY_EXT, ext);
        insertValues.put(KEY_HANDLE, handle);
        insertValues.put(KEY_ID, photoID);
        insertValues.put(KEY_IMAGE250, image250);
        insertValues.put(KEY_IMAGEFULL, imageFull);
        db.insertOrThrow(DATABASE_TABLE, null, insertValues);
    }

    //---deletes a particular contact---
    public boolean deleteItem(String lunchSoup) {
        return db.delete(DATABASE_TABLE, KEY_CLASS + "='" + lunchSoup + "'", null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllItems() {
       /* try {
            dbAlb.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_CREATED, KEY_CLASS,
                        KEY_TYPE, KEY_EXT, KEY_HANDLE, KEY_ID, KEY_IMAGE250, KEY_IMAGEFULL}, null, null,
                null, null, KEY_CREATED);
    }

    //---retrieves a particular contact---
    public Cursor getItem(String photoName) throws SQLException {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_CREATORID, KEY_CREATED, KEY_CLASS,
                        KEY_TYPE, KEY_EXT, KEY_HANDLE, KEY_ID, KEY_IMAGE250, KEY_IMAGEFULL},
                KEY_HANDLE + "='" + photoName + "'", null, null, null, null, null);
    }

    //---updates a contact---
    public boolean updateItem(String creatorID, String created, String mClass, String mType, String ext,
                              String handle, String photoID, byte[] image250, byte[] imageFull) {
        ContentValues args = new ContentValues();
        args.put(KEY_CREATORID, creatorID);
        args.put(KEY_CREATED, created);
        args.put(KEY_CLASS, mClass);
        args.put(KEY_TYPE, mType);
        args.put(KEY_EXT, ext);
        args.put(KEY_HANDLE, handle);
        args.put(KEY_ID, photoID);
        args.put(KEY_IMAGE250, image250);
        args.put(KEY_IMAGEFULL, imageFull);

        return db.update(DATABASE_TABLE, args, KEY_CREATED + "='" + created + "'", null) > 0;
    }
}

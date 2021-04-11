package com.example.bbcnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PUBDATE = "pubdate";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_NOTES = "notes";

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;
    private static String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + COLUMN_TITLE + " TEXT Primary key," + COLUMN_DESCRIPTION
            + " TEXT," + COLUMN_PUBDATE + " TEXT," + COLUMN_LINK + " TEXT," + COLUMN_NOTES + " TEXT)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME + "");
    }

    public Boolean insertIntoDatabase (String title, String description, String pubDate,
                                    String link, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_PUBDATE, pubDate);
        contentValues.put(COLUMN_LINK, link);
        contentValues.put(COLUMN_NOTES, notes);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor readData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +"", null);
        return cursor;
    }

    public boolean deleteData (String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_TITLE + "=?", new String[]{title}) > 0;

    }


}

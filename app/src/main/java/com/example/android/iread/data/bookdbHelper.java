package com.example.android.iread.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;


import com.example.android.iread.R;


public class bookdbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "books.db";
    private  static final int DATABASE_VERSION = 1;

    public bookdbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + bookContract.bookEntry.TABLE_NAME + "("
                + bookContract.bookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + bookContract.bookEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + bookContract.bookEntry.COLUMN_AUTHOR_NAME + " TEXT, "
                + bookContract.bookEntry.COLUMN_PDF_LINK + " TEXT NOT NULL, "
                + bookContract.bookEntry.COLUMN_IMAGE_LINK + " TEXT, "
                + bookContract.bookEntry.COLUMN_PAGES + " TEXT, "
                + bookContract.bookEntry.COLUMN_PUBLISHED_DATE + " TEXT, "
                + bookContract.bookEntry.COLUMN_DESCRIPTION + " TEXT, "
                + bookContract.bookEntry.COLUMN_PDF_NAME + " TEXT NOT NULL, "
                + bookContract.bookEntry.COLUMN_CURRENT_READS + " TEXT, "
                + bookContract.bookEntry.COLUMN_FAV + " TEXT);";

        String SQL_CREATE_PROFILE_TABLE = "CREATE TABLE " + bookContract.profileEntry.TABLE_NAME + "("
                + bookContract.profileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + bookContract.profileEntry.COLUMN_NAME + " TEXT, "
                + bookContract.profileEntry.COLUMN_FAV_AUTHOR + " TEXT, "
                + bookContract.profileEntry.COLUMN_FAV_QUOTE +  " TEXT);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        db.execSQL(SQL_CREATE_PROFILE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){

    }
}
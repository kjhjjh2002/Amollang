package com.example.amolloang.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DiaryDbHelper extends SQLiteOpenHelper {

    private static DiaryDbHelper sInstance;

    private final static int DB_VERSION = 1;

    private final static String DB_NAME = "diary.db";


    private final static String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                    DiaryContract.DiaryEntry.TABLE_NAME,
                    DiaryContract.DiaryEntry._ID,
                    DiaryContract.DiaryEntry.COLUMN_NAME_TITLE,
                    DiaryContract.DiaryEntry.COLUMN_NAME_CONTENTS,
                    DiaryContract.DiaryEntry.COLUMN_NAME_DATE,
                    DiaryContract.DiaryEntry.COLUMN_NAME_MOOD);


    private final static String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS "+ DiaryContract.DiaryEntry.TABLE_NAME;


    public static DiaryDbHelper getInstance(Context context){
        if (sInstance == null){
            sInstance = new DiaryDbHelper(context);
        }

        return sInstance;
    }


    private DiaryDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 호출시에 테이블 생성
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

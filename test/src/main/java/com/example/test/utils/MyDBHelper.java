package com.example.test.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.test.model.Student;


public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context) {
        super(context, "dorm.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Student.Student);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists student");
        onCreate(db);
    }
}

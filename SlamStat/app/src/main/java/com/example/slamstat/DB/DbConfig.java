package com.example.slamstat.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbConfig extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_slamstats";
    private static final int DATABASE_VERSION = 1;
    public static final String USERS_TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";


    public DbConfig(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor insertUser(String name, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + USERS_TABLE_NAME + " (" + COLUMN_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ") VALUES ('" + name + "', '" + email + "', '" + password + "')");
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = '" + email + "'", null);
    }

    public Cursor getUserDataById(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + COLUMN_ID + " = " + userId, null);
    }

    public Cursor login(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = '" + email + "' AND " + COLUMN_PASSWORD + " = '" + password + "'", null);
    }

    public void updateProfile(int userId, String name, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + USERS_TABLE_NAME + " SET " + COLUMN_NAME + " = '" + name + "', " + COLUMN_EMAIL + " = '" + email + "', " + COLUMN_PASSWORD + " = '" + password + "' WHERE " + COLUMN_ID + " = " + userId);
        db.close();
    }
}



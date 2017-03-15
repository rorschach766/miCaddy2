package com.example.jmo.micaddy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jmo.micaddy.activity.LoginActivity;

import java.util.HashMap;

/**
 * Created by jmo on 28/02/2017. Class to handle calls to the DB
 */

public class SQLiteHandler extends SQLiteOpenHelper{

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    //Variables
    //DB Version
    private static final int DATABASE_VERSION = 1;

    //DB Name
    private static final String DATABASE_NAME = "mi_caddy";

    //Table name
    private static  final String TABLE_USER = "user";

    //Columns
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_SECONDNAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HANDICAP = "handicap";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME + " TEXT,"
                + KEY_SECONDNAME + " TEXT," + KEY_UID + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_HANDICAP + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old table
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USER);

        onCreate(db);
    }

    //Store user details
    public void addUser(String firstName, String lastName, String uid, String email, int handicap) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, firstName);
        values.put(KEY_SECONDNAME, lastName);
        values.put(KEY_EMAIL, email);
        values.put(KEY_HANDICAP, handicap);
        values.put(KEY_UID, uid);

        //Insert Row
        long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite" + id);
    }

    //Get user from DB
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("firstName", cursor.getString(1));
            user.put("lastName", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("handicap", cursor.getString(5));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Fetching user from Sqlite" + user.toString());

        return user;
    }

    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
}

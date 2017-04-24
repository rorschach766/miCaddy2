package com.example.jmo.micaddy.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jmo.micaddy.activity.LoginActivity;

import java.util.ArrayList;
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
    private static
    final String TABLE_USER = "user";
    private static final String TABLE_ROUNDS = "round";
    private static final String TABLE_HOLES = "holes";

    //Columns for user table
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_SECONDNAME = "lastName";
    private static final String KEY_UID = "uid";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HANDICAP = "handicap";

    //Columns for rounds tables
    private static final String KEY_COURSE_UID = "courseUID";
    private static final String KEY_COURSE_NAME = "courseName";
    private static final String KEY_DATE = "date";
    private static final String KEY_GOLFER_ID= "golfer_id";

    //Columns for holes table
    private static final String KEY_HOLE_ID = "hole_id";
    private static final String KEY_HOLE_NUM = "hole_number";
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_YARDS = "yards";
    private static final String KEY_PAR = "par";
    private static final String KEY_SHOTS = "shots";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME + " TEXT,"
                + KEY_SECONDNAME + " TEXT," + KEY_UID + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_HANDICAP + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");

        String CREATE_ROUNDS_TABLE = "CREATE TABLE " + TABLE_ROUNDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COURSE_NAME + " TEXT, "
                + KEY_DATE + " TEXT, " + KEY_COURSE_UID + " TEXT, "
                + KEY_GOLFER_ID + " TEXT, " + " FOREIGN KEY ("+ KEY_GOLFER_ID +") REFERENCES "
                + TABLE_USER + "(" +KEY_UID + "))";
        db.execSQL(CREATE_ROUNDS_TABLE);

        Log.d(TAG, "Rounds table created");

        String CREATE_HOLES_TABLE = "CREATE TABLE " + TABLE_HOLES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_HOLE_ID + " TEXT, "
                + KEY_HOLE_NUM + " TEXT, " + KEY_COURSE_ID + " TEXT, "
                + KEY_YARDS + " TEXT, " + KEY_PAR + " TEXT, " + KEY_SHOTS + " TEXT, "
                + " FOREIGN KEY ("+ KEY_COURSE_ID +") REFERENCES "
                + TABLE_ROUNDS + "(" +KEY_COURSE_UID + "))";
        db.execSQL(CREATE_HOLES_TABLE);

        Log.d(TAG, "Rounds table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old table
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ROUNDS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_HOLES);

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

    public void createRound(String courseName, String date, String uid, String golferId){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_NAME, courseName);
        values.put(KEY_DATE, date);
        values.put(KEY_COURSE_UID, uid);
        values.put(KEY_GOLFER_ID, golferId);

        long id = db.insert(TABLE_ROUNDS, null, values);
        db.close();

        Log.d(TAG, "New round inserted into sqlite " + id);
    }

    public void createHole(String uid, String holeNum, String courseID, String yards, String par, int shots){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HOLE_ID, uid);
        values.put(KEY_HOLE_NUM, holeNum);
        values.put(KEY_COURSE_ID, courseID);
        values.put(KEY_YARDS, yards);
        values.put(KEY_PAR, par);
        values.put(KEY_SHOTS, shots);

        long id = db.insert(TABLE_HOLES, null, values);
        db.close();

        Log.d(TAG, "New hole inserted into sqlite " + id);
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

    public HashMap<String, String> getRoundsDetails() {
        HashMap<String, String> rounds = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_ROUNDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToPosition(cursor.getCount());
        if (cursor.moveToLast()) {
            rounds.put("courseName", cursor.getString(1));
            rounds.put("date", cursor.getString(2));
            rounds.put("courseUID", cursor.getString(3));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Fetching round from Sqlite" + rounds.toString());

        return rounds;
    }

    /*public HashMap<String, String> getHoles(){
        HashMap<String, String> holes = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_HOLES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            holes.put("holeNum", cursor.getString(2));
            holes.put("yards", cursor.getString(4));
            holes.put("par", cursor.getString(5));
            holes.put("shots", cursor.getString(6));
        }

        cursor.close();
        db.close();

        Log.d(TAG, "Fetching holes from Sqlite" + holes.toString());

        return holes;
    }*/

    public ArrayList<HashMap<String, String>> getAllHoles(){

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_HOLES, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            HashMap<String, String> hashmap = new HashMap<String, String>();
            hashmap.put("holeNum", cursor.getString(2));
            hashmap.put("yards", cursor.getString(4));
            hashmap.put("par", cursor.getString(5));
            hashmap.put("shots", cursor.getString(6));

            arrayList.add(hashmap);
            cursor.moveToNext();
        }

        return arrayList;
    }

    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteRounds(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ROUNDS, null, null);
        db.close();

        Log.d(TAG, "Deleted all rounds from sqlite");
    }

    public void deleteHoles(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_HOLES, null, null);
        db.close();

        Log.d(TAG, "Deleted all rounds from sqlite");
    }
}

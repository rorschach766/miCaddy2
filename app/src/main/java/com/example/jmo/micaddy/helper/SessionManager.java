package com.example.jmo.micaddy.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by jmo on 28/02/2017. Class maintains session data throughout the com.example.jmo.micaddy.app using
 * SharedPreferences. Boolean flag isLoggedIn in shared preferences to check login status
 */

public class SessionManager
{
    //Variables
    //LogCat
    private static String TAG = SessionManager.class.getSimpleName();

    //Shared Pref.
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    //Shared pref mode
    int PRIVATE_MODE = 0;

    //File name
    private static final String PREF_NAME = "MiCaddyLogin";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    //Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        editor.commit();

        Log.d(TAG, "User login session modified");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}

package com.example.jmo.micaddy.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jmo on 28/02/2017. Class initiating core volley objects to be executed on com.example.jmo.micaddy.app
 * launch
 */

public class  AppController extends Application {

    //Variable declarations
    public static  final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static  AppController mInstance;

    //Start up login screen on com.example.jmo.micaddy.app launch
    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    //Constructor
    public static synchronized AppController getInstance(){
        return mInstance;
    }


    /**Core volley objects
     *
     */

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG:tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}

package com.example.jmo.micaddy.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmo.micaddy.R;
import com.example.jmo.micaddy.activity.LoginActivity;
import com.example.jmo.micaddy.helper.SQLiteHandler;
import com.example.jmo.micaddy.helper.SessionManager;

/**
 * Created by jmo on 14/03/2017. Fragment to handle logout function
 */

public class Logout extends Fragment {

    //SQL items and Session manager declarations
    private SQLiteHandler db;
    private SessionManager session;

    //Method to handle the layout. No layout is used but the view needs to be set
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = new SQLiteHandler(getActivity());

        session = new SessionManager(getActivity());

        logOutUser();
        return view;
    }

    //Set title of fragment
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Logout");
    }

    //Logout function
    public void logOutUser(){
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

package com.example.jmo.micaddy.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jmo.micaddy.MainActivity;
import com.example.jmo.micaddy.R;
import com.example.jmo.micaddy.helper.SQLiteHandler;
import com.example.jmo.micaddy.helper.SessionManager;

import java.util.HashMap;

/**
 * Created by jmo on 07/03/2017.
 */

public class Home extends Fragment {

    private TextView txtName;
    private TextView txtHandicap;

    private SQLiteHandler db;
    protected SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle("Home");

        txtName = (TextView) view.findViewById(R.id.firstName);
        txtHandicap = (TextView) view.findViewById(R.id.handicap);

        db = new SQLiteHandler(getActivity().getApplicationContext());

        session = new SessionManager(getActivity());

        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("firstName");
        String handicap = user.get("handicap");

        txtName.setText(name);
        txtHandicap.setText(handicap);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");
    }
}

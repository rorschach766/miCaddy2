package com.example.jmo.micaddy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jmo.micaddy.MainActivity;
import com.example.jmo.micaddy.R;
import com.example.jmo.micaddy.activity.CreateRoundActivity;

/**
 * Created by jmo on 14/03/2017.
 */

public class Rounds extends Fragment {

    private Button btnCreate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rounds, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle("Rounds");

        btnCreate = (Button) view.findViewById(R.id.btn_start_round);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        CreateRoundActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Rounds");
    }
}

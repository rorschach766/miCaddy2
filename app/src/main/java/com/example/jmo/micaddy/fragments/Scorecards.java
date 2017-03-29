package com.example.jmo.micaddy.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jmo.micaddy.MainActivity;
import com.example.jmo.micaddy.R;
import com.example.jmo.micaddy.helper.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jmo on 14/03/2017.
 */

public class Scorecards extends Fragment {

    private EditText scorecardCourse;
    private EditText scorecardDate;

    private SQLiteHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new SQLiteHandler(getActivity().getApplicationContext());

        HashMap<String, String> rounds = db.getRoundsDetails();

        if(db.getRoundsDetails().size() != 0){

            View view = inflater.inflate(R.layout.fragment_scorecards, container, false);

            ((MainActivity) getActivity())
                    .setActionBarTitle("Scorecard");

            scorecardCourse = (EditText) view.findViewById(R.id.course);
            scorecardCourse.setEnabled(false);
            scorecardDate = (EditText) view.findViewById(R.id.date);
            scorecardDate.setEnabled(false);


            final String course = rounds.get("courseName");
            final String date = rounds.get("date");

                scorecardCourse.setText(course);
                scorecardDate.setText(date);


                ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
                arrayList = db.getAllHoles();

                if(arrayList.size() != 0){

                EditText []holeNum = new EditText[18];
                holeNum[0]=(EditText) view.findViewById(R.id.scorecardHoleNum1);
                holeNum[1]=(EditText) view.findViewById(R.id.scorecardHoleNum2);
                holeNum[2]=(EditText) view.findViewById(R.id.scorecardHoleNum3);
                holeNum[3]=(EditText) view.findViewById(R.id.scorecardHoleNum4);
                holeNum[4]=(EditText) view.findViewById(R.id.scorecardHoleNum5);
                holeNum[5]=(EditText) view.findViewById(R.id.scorecardHoleNum6);
                holeNum[6]=(EditText) view.findViewById(R.id.scorecardHoleNum7);
                holeNum[7]=(EditText) view.findViewById(R.id.scorecardHoleNum8);
                holeNum[8]=(EditText) view.findViewById(R.id.scorecardHoleNum9);
                holeNum[9]=(EditText) view.findViewById(R.id.scorecardHoleNum10);
                holeNum[10]=(EditText) view.findViewById(R.id.scorecardHoleNum11);
                holeNum[11]=(EditText) view.findViewById(R.id.scorecardHoleNum12);
                holeNum[12]=(EditText) view.findViewById(R.id.scorecardHoleNum13);
                holeNum[13]=(EditText) view.findViewById(R.id.scorecardHoleNum14);
                holeNum[14]=(EditText) view.findViewById(R.id.scorecardHoleNum15);
                holeNum[15]=(EditText) view.findViewById(R.id.scorecardHoleNum16);
                holeNum[16]=(EditText) view.findViewById(R.id.scorecardHoleNum17);
                holeNum[17]=(EditText) view.findViewById(R.id.scorecardHoleNum18);

                EditText []holeYards = new EditText[18];
                holeYards[0]=(EditText) view.findViewById(R.id.scorecardYards1);
                holeYards[1]=(EditText) view.findViewById(R.id.scorecardYards2);
                holeYards[2]=(EditText) view.findViewById(R.id.scorecardYards3);
                holeYards[3]=(EditText) view.findViewById(R.id.scorecardYards4);
                holeYards[4]=(EditText) view.findViewById(R.id.scorecardYards5);
                holeYards[5]=(EditText) view.findViewById(R.id.scorecardYards6);
                holeYards[6]=(EditText) view.findViewById(R.id.scorecardYards7);
                holeYards[7]=(EditText) view.findViewById(R.id.scorecardYards8);
                holeYards[8]=(EditText) view.findViewById(R.id.scorecardYards9);
                holeYards[9]=(EditText) view.findViewById(R.id.scorecardYards10);
                holeYards[10]=(EditText) view.findViewById(R.id.scorecardYards11);
                holeYards[11]=(EditText) view.findViewById(R.id.scorecardYards12);
                holeYards[12]=(EditText) view.findViewById(R.id.scorecardYards13);
                holeYards[13]=(EditText) view.findViewById(R.id.scorecardYards14);
                holeYards[14]=(EditText) view.findViewById(R.id.scorecardYards15);
                holeYards[15]=(EditText) view.findViewById(R.id.scorecardYards16);
                holeYards[16]=(EditText) view.findViewById(R.id.scorecardYards17);
                holeYards[17]=(EditText) view.findViewById(R.id.scorecardYards18);

                EditText []holePar = new EditText[18];
                holePar[0]=(EditText) view.findViewById(R.id.scorecardPar1);
                holePar[1]=(EditText) view.findViewById(R.id.scorecardPar2);
                holePar[2]=(EditText) view.findViewById(R.id.scorecardPar3);
                holePar[3]=(EditText) view.findViewById(R.id.scorecardPar4);
                holePar[4]=(EditText) view.findViewById(R.id.scorecardPar5);
                holePar[5]=(EditText) view.findViewById(R.id.scorecardPar6);
                holePar[6]=(EditText) view.findViewById(R.id.scorecardPar7);
                holePar[7]=(EditText) view.findViewById(R.id.scorecardPar8);
                holePar[8]=(EditText) view.findViewById(R.id.scorecardPar9);
                holePar[9]=(EditText) view.findViewById(R.id.scorecardPar10);
                holePar[10]=(EditText) view.findViewById(R.id.scorecardPar11);
                holePar[11]=(EditText) view.findViewById(R.id.scorecardPar12);
                holePar[12]=(EditText) view.findViewById(R.id.scorecardPar13);
                holePar[13]=(EditText) view.findViewById(R.id.scorecardPar14);
                holePar[14]=(EditText) view.findViewById(R.id.scorecardPar15);
                holePar[15]=(EditText) view.findViewById(R.id.scorecardPar16);
                holePar[16]=(EditText) view.findViewById(R.id.scorecardPar17);
                holePar[17]=(EditText) view.findViewById(R.id.scorecardPar18);

                EditText []holeShots = new EditText[18];
                holeShots[0]=(EditText) view.findViewById(R.id.scorecardShots1);
                holeShots[1]=(EditText) view.findViewById(R.id.scorecardShots2);
                holeShots[2]=(EditText) view.findViewById(R.id.scorecardShots3);
                holeShots[3]=(EditText) view.findViewById(R.id.scorecardShots4);
                holeShots[4]=(EditText) view.findViewById(R.id.scorecardShots5);
                holeShots[5]=(EditText) view.findViewById(R.id.scorecardShots6);
                holeShots[6]=(EditText) view.findViewById(R.id.scorecardShots7);
                holeShots[7]=(EditText) view.findViewById(R.id.scorecardShots8);
                holeShots[8]=(EditText) view.findViewById(R.id.scorecardShots9);
                holeShots[9]=(EditText) view.findViewById(R.id.scorecardShots10);
                holeShots[10]=(EditText) view.findViewById(R.id.scorecardShots11);
                holeShots[11]=(EditText) view.findViewById(R.id.scorecardShots12);
                holeShots[12]=(EditText) view.findViewById(R.id.scorecardShots13);
                holeShots[13]=(EditText) view.findViewById(R.id.scorecardShots14);
                holeShots[14]=(EditText) view.findViewById(R.id.scorecardShots15);
                holeShots[15]=(EditText) view.findViewById(R.id.scorecardShots16);
                holeShots[16]=(EditText) view.findViewById(R.id.scorecardShots17);
                holeShots[17]=(EditText) view.findViewById(R.id.scorecardShots18);

                for(int i = 0; i < arrayList.size(); i++){
                    final String numHole = arrayList.get(i).get("holeNum");
                    final String yards = arrayList.get(i).get("yards");
                    final String par = arrayList.get(i).get("par");
                    final String shots = arrayList.get(i).get("shots");

                    holeNum[i].setText(numHole);
                    holeYards[i].setText(yards);
                    holePar[i].setText(par);
                    holeShots[i].setText(shots);

                }
            }

            return view;
        }else {
            View view = inflater.inflate(R.layout.fragment_no_rounds, container, false);

            ((MainActivity) getActivity())
                    .setActionBarTitle("Scorecard");

            return view;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Scorecards");
    }
}

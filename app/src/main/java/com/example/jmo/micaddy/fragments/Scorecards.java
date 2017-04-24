package com.example.jmo.micaddy.fragments;

import android.graphics.Color;
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

import static android.graphics.Color.*;
import static com.example.jmo.micaddy.R.id.holeYards;

/**
 * Created by jmo on 14/03/2017.
 */

public class Scorecards extends Fragment {

    private TextView scorecardCourse;
    private TextView scorecardDate;
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

            scorecardCourse = (TextView) view.findViewById(R.id.course);
            scorecardDate = (TextView) view.findViewById(R.id.date);


            final String course = rounds.get("courseName");
            final String date = rounds.get("date");

                scorecardCourse.setText(course);
                scorecardDate.setText(date);


                ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
                arrayList = db.getAllHoles();

                if(arrayList.size() != 0){

                    TextView []holeNum = new TextView[18];
                holeNum[0]=(TextView) view.findViewById(R.id.scorecardHoleNum1);
                    holeNum[0].setVisibility(View.GONE);
                holeNum[1]=(TextView) view.findViewById(R.id.scorecardHoleNum2);
                    holeNum[1].setVisibility(View.GONE);
                holeNum[2]=(TextView) view.findViewById(R.id.scorecardHoleNum3);
                    holeNum[2].setVisibility(View.GONE);
                holeNum[3]=(TextView) view.findViewById(R.id.scorecardHoleNum4);
                    holeNum[3].setVisibility(View.GONE);
                holeNum[4]=(TextView) view.findViewById(R.id.scorecardHoleNum5);
                    holeNum[4].setVisibility(View.GONE);
                holeNum[5]=(TextView) view.findViewById(R.id.scorecardHoleNum6);
                    holeNum[5].setVisibility(View.GONE);
                holeNum[6]=(TextView) view.findViewById(R.id.scorecardHoleNum7);
                    holeNum[6].setVisibility(View.GONE);
                holeNum[7]=(TextView) view.findViewById(R.id.scorecardHoleNum8);
                    holeNum[7].setVisibility(View.GONE);
                holeNum[8]=(TextView) view.findViewById(R.id.scorecardHoleNum9);
                    holeNum[8].setVisibility(View.GONE);
                holeNum[9]=(TextView) view.findViewById(R.id.scorecardHoleNum10);
                    holeNum[9].setVisibility(View.GONE);
                holeNum[10]=(TextView) view.findViewById(R.id.scorecardHoleNum11);
                    holeNum[10].setVisibility(View.GONE);
                holeNum[11]=(TextView) view.findViewById(R.id.scorecardHoleNum12);
                    holeNum[11].setVisibility(View.GONE);
                holeNum[12]=(TextView) view.findViewById(R.id.scorecardHoleNum13);
                    holeNum[12].setVisibility(View.GONE);
                holeNum[13]=(TextView) view.findViewById(R.id.scorecardHoleNum14);
                    holeNum[13].setVisibility(View.GONE);
                holeNum[14]=(TextView) view.findViewById(R.id.scorecardHoleNum15);
                    holeNum[14].setVisibility(View.GONE);
                holeNum[15]=(TextView) view.findViewById(R.id.scorecardHoleNum16);
                    holeNum[15].setVisibility(View.GONE);
                holeNum[16]=(TextView) view.findViewById(R.id.scorecardHoleNum17);
                    holeNum[16].setVisibility(View.GONE);
                holeNum[17]=(TextView) view.findViewById(R.id.scorecardHoleNum18);
                    holeNum[17].setVisibility(View.GONE);

                    TextView []holeYards = new TextView[18];
                holeYards[0]=(TextView) view.findViewById(R.id.scorecardYards1);
                    holeYards[0].setVisibility(View.GONE);
                holeYards[1]=(TextView) view.findViewById(R.id.scorecardYards2);
                    holeYards[1].setVisibility(View.GONE);
                holeYards[2]=(TextView) view.findViewById(R.id.scorecardYards3);
                    holeYards[2].setVisibility(View.GONE);
                holeYards[3]=(TextView) view.findViewById(R.id.scorecardYards4);
                    holeYards[3].setVisibility(View.GONE);
                holeYards[4]=(TextView) view.findViewById(R.id.scorecardYards5);
                    holeYards[4].setVisibility(View.GONE);
                holeYards[5]=(TextView) view.findViewById(R.id.scorecardYards6);
                    holeYards[5].setVisibility(View.GONE);
                holeYards[6]=(TextView) view.findViewById(R.id.scorecardYards7);
                    holeYards[6].setVisibility(View.GONE);
                holeYards[7]=(TextView) view.findViewById(R.id.scorecardYards8);
                    holeYards[7].setVisibility(View.GONE);
                holeYards[8]=(TextView) view.findViewById(R.id.scorecardYards9);
                    holeYards[8].setVisibility(View.GONE);
                holeYards[9]=(TextView) view.findViewById(R.id.scorecardYards10);
                    holeYards[9].setVisibility(View.GONE);
                holeYards[10]=(TextView) view.findViewById(R.id.scorecardYards11);
                    holeYards[10].setVisibility(View.GONE);
                holeYards[11]=(TextView) view.findViewById(R.id.scorecardYards12);
                    holeYards[11].setVisibility(View.GONE);
                holeYards[12]=(TextView) view.findViewById(R.id.scorecardYards13);
                    holeYards[12].setVisibility(View.GONE);
                holeYards[13]=(TextView) view.findViewById(R.id.scorecardYards14);
                    holeYards[13].setVisibility(View.GONE);
                holeYards[14]=(TextView) view.findViewById(R.id.scorecardYards15);
                    holeYards[14].setVisibility(View.GONE);
                holeYards[15]=(TextView) view.findViewById(R.id.scorecardYards16);
                    holeYards[15].setVisibility(View.GONE);
                holeYards[16]=(TextView) view.findViewById(R.id.scorecardYards17);
                    holeYards[16].setVisibility(View.GONE);
                holeYards[17]=(TextView) view.findViewById(R.id.scorecardYards18);
                    holeYards[17].setVisibility(View.GONE);

                TextView []holePar = new TextView[18];
                holePar[0]=(TextView) view.findViewById(R.id.scorecardPar1);
                    holePar[0].setVisibility(View.GONE);
                holePar[1]=(TextView) view.findViewById(R.id.scorecardPar2);
                    holePar[1].setVisibility(View.GONE);
                holePar[2]=(TextView) view.findViewById(R.id.scorecardPar3);
                    holePar[2].setVisibility(View.GONE);
                holePar[3]=(TextView) view.findViewById(R.id.scorecardPar4);
                    holePar[3].setVisibility(View.GONE);
                holePar[4]=(TextView) view.findViewById(R.id.scorecardPar5);
                    holePar[4].setVisibility(View.GONE);
                holePar[5]=(TextView) view.findViewById(R.id.scorecardPar6);
                    holePar[5].setVisibility(View.GONE);
                holePar[6]=(TextView) view.findViewById(R.id.scorecardPar7);
                    holePar[6].setVisibility(View.GONE);
                holePar[7]=(TextView) view.findViewById(R.id.scorecardPar8);
                    holePar[7].setVisibility(View.GONE);
                holePar[8]=(TextView) view.findViewById(R.id.scorecardPar9);
                    holePar[8].setVisibility(View.GONE);
                holePar[9]=(TextView) view.findViewById(R.id.scorecardPar10);
                    holePar[9].setVisibility(View.GONE);
                holePar[10]=(TextView) view.findViewById(R.id.scorecardPar11);
                    holePar[10].setVisibility(View.GONE);
                holePar[11]=(TextView) view.findViewById(R.id.scorecardPar12);
                    holePar[11].setVisibility(View.GONE);
                holePar[12]=(TextView) view.findViewById(R.id.scorecardPar13);
                    holePar[12].setVisibility(View.GONE);
                holePar[13]=(TextView) view.findViewById(R.id.scorecardPar14);
                    holePar[13].setVisibility(View.GONE);
                holePar[14]=(TextView) view.findViewById(R.id.scorecardPar15);
                    holePar[14].setVisibility(View.GONE);
                holePar[15]=(TextView) view.findViewById(R.id.scorecardPar16);
                    holePar[15].setVisibility(View.GONE);
                holePar[16]=(TextView) view.findViewById(R.id.scorecardPar17);
                    holePar[16].setVisibility(View.GONE);
                holePar[17]=(TextView) view.findViewById(R.id.scorecardPar18);
                    holePar[17].setVisibility(View.GONE);

                TextView []holeShots = new TextView[18];
                holeShots[0]=(TextView) view.findViewById(R.id.scorecardShots1);
                    holeShots[0].setVisibility(View.GONE);
                holeShots[1]=(TextView) view.findViewById(R.id.scorecardShots2);
                    holeShots[1].setVisibility(View.GONE);
                holeShots[2]=(TextView) view.findViewById(R.id.scorecardShots3);
                    holeShots[2].setVisibility(View.GONE);
                holeShots[3]=(TextView) view.findViewById(R.id.scorecardShots4);
                    holeShots[3].setVisibility(View.GONE);
                holeShots[4]=(TextView) view.findViewById(R.id.scorecardShots5);
                    holeShots[4].setVisibility(View.GONE);
                holeShots[5]=(TextView) view.findViewById(R.id.scorecardShots6);
                    holeShots[5].setVisibility(View.GONE);
                holeShots[6]=(TextView) view.findViewById(R.id.scorecardShots7);
                    holeShots[6].setVisibility(View.GONE);
                holeShots[7]=(TextView) view.findViewById(R.id.scorecardShots8);
                    holeShots[7].setVisibility(View.GONE);
                holeShots[8]=(TextView) view.findViewById(R.id.scorecardShots9);
                    holeShots[8].setVisibility(View.GONE);
                holeShots[9]=(TextView) view.findViewById(R.id.scorecardShots10);
                    holeShots[9].setVisibility(View.GONE);
                holeShots[10]=(TextView) view.findViewById(R.id.scorecardShots11);
                    holeShots[10].setVisibility(View.GONE);
                holeShots[11]=(TextView) view.findViewById(R.id.scorecardShots12);
                    holeShots[11].setVisibility(View.GONE);
                holeShots[12]=(TextView) view.findViewById(R.id.scorecardShots13);
                    holeShots[12].setVisibility(View.GONE);
                holeShots[13]=(TextView) view.findViewById(R.id.scorecardShots14);
                    holeShots[13].setVisibility(View.GONE);
                holeShots[14]=(TextView) view.findViewById(R.id.scorecardShots15);
                    holeShots[14].setVisibility(View.GONE);
                holeShots[15]=(TextView) view.findViewById(R.id.scorecardShots16);
                    holeShots[15].setVisibility(View.GONE);
                holeShots[16]=(TextView) view.findViewById(R.id.scorecardShots17);
                    holeShots[16].setVisibility(View.GONE);
                holeShots[17]=(TextView) view.findViewById(R.id.scorecardShots18);
                    holeShots[17].setVisibility(View.GONE);

                for(int i = 0; i < arrayList.size(); i++){


                    final String numHole = arrayList.get(i).get("holeNum");
                    final String yards = arrayList.get(i).get("yards");
                    final String par = arrayList.get(i).get("par");
                    final String shots = arrayList.get(i).get("shots");

                    if(holeNum[i].getText() == null){
                        holeNum[i].setVisibility(View.GONE);
                        holeNum[i].setEnabled(false);
                        holeNum[i].setFocusable(false);
                    }else{
                        holeNum[i].setVisibility(View.VISIBLE);
                        holeNum[i].setEnabled(true);
                        holeNum[i].setFocusable(true);
                        holeNum[i].setText(numHole);
                    }

                    if(holeYards[i].getText() == null){
                        holeYards[i].setVisibility(View.GONE);
                        holeYards[i].setEnabled(false);
                        holeYards[i].setFocusable(false);
                    }else{
                        holeYards[i].setVisibility(View.VISIBLE);
                        holeYards[i].setEnabled(true);
                        holeYards[i].setFocusable(true);
                        holeYards[i].setText(yards);
                    }

                    if(holePar[i].getText() == null){
                        holePar[i].setVisibility(View.GONE);
                        holePar[i].setEnabled(false);
                        holePar[i].setFocusable(false);
                    }else{
                        holePar[i].setVisibility(View.VISIBLE);
                        holePar[i].setEnabled(true);
                        holePar[i].setFocusable(true);
                        holePar[i].setText(par);
                    }

                    if(holeShots[i].getText() == null){
                        holeShots[i].setVisibility(View.GONE);
                        holeShots[i].setEnabled(false);
                        holeShots[i].setFocusable(false);
                    }else{
                        holeShots[i].setVisibility(View.VISIBLE);
                        holeShots[i].setEnabled(true);
                        holeShots[i].setFocusable(true);
                        holeShots[i].setText(shots);
                    }

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

    private void getTotals(){

    }
}

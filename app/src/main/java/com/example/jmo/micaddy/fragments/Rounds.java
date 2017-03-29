package com.example.jmo.micaddy.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jmo.micaddy.MainActivity;
import com.example.jmo.micaddy.R;
import com.example.jmo.micaddy.activity.MapsActivity;
import com.example.jmo.micaddy.app.AppConfig;
import com.example.jmo.micaddy.app.AppController;
import com.example.jmo.micaddy.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jmo on 14/03/2017.
 */

public class Rounds extends Fragment implements View.OnClickListener {

    private static final String TAG = Rounds.class.getSimpleName();
    private EditText inputCourseName;
    private Button btnStart;
    private EditText inputDate;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private String uid;

    private DatePickerDialog datePickerDialog;

    private SimpleDateFormat dateFormatter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_rounds, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle("Create new Round");

        inputCourseName = (EditText) view.findViewById(R.id.course_name);
        inputDate = (EditText) view.findViewById(R.id.edit_date);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        btnStart = (Button) view.findViewById(R.id.btn_start);

        db = new SQLiteHandler(getActivity().getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        final String uid = user.get("uid");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteRounds();
                db.deleteHoles();

                String courseName = inputCourseName.getText().toString().trim();
                String date = inputDate.getText().toString().trim();
                String id = uid;


                if(!courseName.isEmpty() && !date.isEmpty()){
                    createRound(courseName, date, id);
                    Intent intent = new Intent(getActivity().getApplicationContext(),
                            MapsActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please check you have entered the correct details", Toast.LENGTH_LONG).show();
                }
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        findViewsById(view);

        setDateTimeField();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDateTimeField() {
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inm.hideSoftInputFromWindow(getView().getWindowToken(),0);
                if(v == inputDate){
                    datePickerDialog.show();
                }
            }
        });

        Calendar newCalender = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                inputDate.setText(dateFormatter.format(newDate.getTime()));
                datePickerDialog.dismiss();
            }
        },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));
    }

    private void findViewsById(View view) {
        inputDate = (EditText) view.findViewById(R.id.edit_date);
        inputDate.setInputType(InputType.TYPE_NULL);
        inputDate.requestFocus();
    }

    public void onClick(View v){
        if(v == inputDate){
            datePickerDialog.show();
        }
    }

    private void createRound(final String courseName, final String date, final String uid){

        String tag_string_req = "req_round_create";

        pDialog.setMessage("Creating round");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_CREATE_ROUND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Round Create Response: " + response.toString());
                hideDialog();

                db.deleteRounds();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String uid = jObj.getString("uid");

                        JSONObject round = jObj.getJSONObject("rounds");
                        String courseName = round.getString("courseName");
                        String date = round.getString("date");
                        String golferId = round.getString("id");

                        db.createRound(courseName, date, uid, golferId);

                        Toast.makeText(getActivity().getApplicationContext(), "Round successfully created", Toast.LENGTH_LONG).show();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Round Create Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(),Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("courseName", courseName);
                params.put("date", date);
                params.put("id", uid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void showDialog() {
        if(!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }
}

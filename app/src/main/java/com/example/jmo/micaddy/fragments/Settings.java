package com.example.jmo.micaddy.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jmo.micaddy.MainActivity;
import com.example.jmo.micaddy.R;
import com.example.jmo.micaddy.activity.LoginActivity;
import com.example.jmo.micaddy.app.AppConfig;
import com.example.jmo.micaddy.app.AppController;
import com.example.jmo.micaddy.helper.SQLiteHandler;
import com.example.jmo.micaddy.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmo on 14/03/2017.
 */

public class Settings extends Fragment {

    private static final String TAG = Settings.class.getSimpleName();
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtEmail;
    private EditText txtHandicap;
    private Button btnDelete;
    private Button btnEdit;
    private ProgressDialog pDialog;

    private SQLiteHandler db;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ((MainActivity) getActivity())
                .setActionBarTitle("Account");

        txtFirstName = (EditText) view.findViewById(R.id.firstName);
        txtLastName = (EditText) view.findViewById(R.id.lastName);
        txtEmail = (EditText) view.findViewById(R.id.email);
        txtHandicap = (EditText) view.findViewById(R.id.handicap);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnEdit = (Button) view.findViewById(R.id.btn_edit);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getActivity().getApplicationContext());

        session = new SessionManager(getActivity().getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        HashMap<String, String> course = db.getRoundsDetails();

        final String firstName = user.get("firstName");
        final String lastName = user.get("lastName");
        final String email = user.get("email");
        final String handicap = user.get("handicap");
        final String uid = user.get("uid");
        final String courseUid = course.get("courseUID");

        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtEmail.setText(email);
        txtHandicap.setText(handicap);

        txtFirstName.setSelection(txtFirstName.getText().length());
        txtLastName.setSelection(txtLastName.getText().length());
        txtEmail.setSelection(txtEmail.getText().length());
        txtHandicap.setSelection(txtHandicap.getText().length());

        btnEdit.setEnabled(false);
        txtFirstName.addTextChangedListener(watcher);
        txtLastName.addTextChangedListener(watcher);
        txtEmail.addTextChangedListener(watcher);
        txtHandicap.addTextChangedListener(watcher);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder deleteWarning = new AlertDialog.Builder(getActivity());
                deleteWarning.setCancelable(false);
                deleteWarning.setTitle("Confirm");
                deleteWarning.setMessage("Are you sure you want to delete your account? You will lose all your golf data!");
                deleteWarning.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteHoles(courseUid);
                        deleteRounds(uid);
                        deleteUser(uid);
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                deleteWarning.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(uid);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Settings");
    }

    private void deleteHoles(final String uid){

        String tag_string_req = "req_delete_rounds";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_HOLES, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Delete response " + response.toString());

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        db.deleteHoles();
                    }else {
                        String errorMsg = jsonObject.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Deletion Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", uid);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void deleteRounds(final String uid){

        String tag_string_req = "req_delete_rounds";

        StringRequest strRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_ROUNDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Delete response " + response.toString());

                try{
                    JSONObject jobj = new JSONObject(response);
                    boolean error = jobj.getBoolean("error");

                    if(!error){
                        db.deleteRounds();
                    }else {
                        String errorMsg = jobj.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Deletion Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", uid);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strRequest, tag_string_req);
    }

    private void deleteUser(final String uid) {

        String tag_string_req = "req_delete";

        pDialog.setMessage("Deleting...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Delete response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        session.setLogin(false);

                        db.deleteUsers();
                        Intent intent = new Intent(getActivity(),
                                LoginActivity.class);
                        startActivity(intent);

                        Toast.makeText(getActivity().getApplicationContext(), "Account Deleted", Toast.LENGTH_LONG).show();

                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Deletion Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", uid);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateUser(final String uid){

        final String newFirstName = txtFirstName.getText().toString().trim();
        final String newLastName = txtLastName.getText().toString().trim();
        final String newEmail = txtEmail.getText().toString().trim();
        final String newHandicap = txtHandicap.getText().toString().trim();

        String tag_string_req = "req_update";

        pDialog.setMessage("Updating...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDIT_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update response: " + response.toString());
                hideDialog();

                try{
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        session.setLogin(false);

                        db.deleteUsers();

                        Intent intent = new Intent(getActivity(),
                                LoginActivity.class);
                        startActivity(intent);

                        Toast.makeText(getActivity().getApplicationContext(), "Account Updated. Please log in back for changes to take effect", Toast.LENGTH_LONG).show();
                    }else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("firstName", newFirstName);
                params.put("lastName", newLastName);
                params.put("email", newEmail);
                params.put("handicap", newHandicap);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            btnEdit.setEnabled(true);
        }
    };
}


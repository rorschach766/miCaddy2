package com.example.jmo.micaddy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jmo.micaddy.MainActivity;
import com.example.jmo.micaddy.R;
import com.example.jmo.micaddy.app.AppConfig;
import com.example.jmo.micaddy.app.AppController;
import com.example.jmo.micaddy.helper.SQLiteHandler;
import com.example.jmo.micaddy.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmo on 28/02/2017. Class registers users using volley and inserts them into
 * SQLite database
 */
public class RegisterActivity extends Activity {

    //Variable declarations
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnReg;
    private Button btnLinkToLogin;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private EditText inputHandicap;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    //On create method to set layout and initialise XML objects
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFirstName = (EditText) findViewById(R.id.firstName);
        inputLastName = (EditText) findViewById(R.id.lastName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        inputHandicap = (EditText) findViewById(R.id.handicap);
        btnReg = (Button) findViewById(R.id.btnReg);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLogin);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session= new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnReg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String firstName = inputFirstName.getText().toString().trim();
                String lastName = inputLastName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String handicap = inputHandicap.getText().toString().trim();

                if(inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    if(!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() &&
                            !password.isEmpty() && !handicap.isEmpty()){
                        if(password.length() >= 8) {
                            registerUser(firstName, lastName, email, password, handicap);
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Password must be more than 8 characters", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Please check you have entered the correct details", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Method to create new user in SQLite and Database using volley
    private void registerUser(final String firstName, final String lastName, final String email, final String password, final String handicap) {
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String firstName = user.getString("firstName");
                        String lastName = user.getString("lastName");
                        String email = user.getString("email");
                        int handicap = user.getInt("handicap");

                        db.addUser(firstName, lastName, uid, email, handicap);

                        Toast.makeText(getApplicationContext(), "Account successfully created. You may now log in", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Register Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("password", password);
                params.put("handicap", handicap);

                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //Disable back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

    //Method to show dialog
    private void showDialog() {
        if(!pDialog.isShowing())
            pDialog.show();
    }

    //Method to hide dialog
    private void hideDialog() {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

}

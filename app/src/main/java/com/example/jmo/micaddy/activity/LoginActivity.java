package com.example.jmo.micaddy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmo on 28/02/2017. Class to handle the login functions and layouts
 */
public class LoginActivity extends Activity {

    //Variables
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToReg;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    //Set com.example.jmo.micaddy.activity view
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Activity items
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToReg = (Button) findViewById(R.id.btnLinkToReg);

        //Progress Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //Session Manager
        session = new SessionManager(getApplicationContext());

        //Check if user is logged in
        if(session.isLoggedIn()){
            //User is logged in, go to main activty
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                //Check for empty data
                if(!email.isEmpty() && !password.isEmpty()){
                    checkLogin(email, password);
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter your login details", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Link to registration screen
        btnLinkToReg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Verify user details in Database
    private void checkLogin(final String email, final String password) {

        String tag_string_req = "req_login";

        pDialog.setMessage("Logging you in...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        session.setLogin(true);

                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String firstName = user.getString("firstName");
                        String lastName = user.getString("lastName");
                        String email = user.getString("email");
                        int handicap = user.getInt("handicap");

                        db.addUser(firstName, lastName, uid, email,handicap);

                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }


        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if(!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
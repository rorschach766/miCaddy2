package com.example.jmo.micaddy.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.jmo.micaddy.app.AppConfig;
import com.example.jmo.micaddy.app.AppController;
import com.example.jmo.micaddy.helper.SQLiteHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmo on 21/03/2017.
 */

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurLocationMarker;
    private ProgressDialog pDialog;
    private static final String TAG = MapsActivity.class.getSimpleName();

    private SQLiteHandler db;

    private FloatingActionButton fabAddShot;
    private FloatingActionButton fabAddHole;


    private int shots;
    private int numHole = 1;
    private String par;
    private String yards;
    private int totalShots = 0;

    private Dialog dialog;
    private EditText holeNum;
    private EditText holeYards;
    private EditText holePar;
    private TextView txtShotsTotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMaps);
        toolbar.setTitle("Your Round");

        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());

        newHoleDialog();

        //Progress Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ArrayList<LatLng> markerPoints = new ArrayList<>();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fabAddShot = (FloatingActionButton) findViewById(R.id.fabAddShot);
        fabAddHole = (FloatingActionButton) findViewById(R.id.fabCompleteHole);



        fabAddHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numHole < 19){
                    HashMap<String, String> round = db.getRoundsDetails();
                    final String uid = round.get("courseUID");
                    createHole(String.valueOf(numHole-1), uid, yards, par, shots-1);

                    Toast.makeText(getApplicationContext(), "Hole recorded", Toast.LENGTH_SHORT).show();
                    newHoleDialog();
                    shots = 0;
                }else if(numHole == 19){
                    HashMap<String, String> round = db.getRoundsDetails();
                    final String uid = round.get("courseUID");
                    createHole(String.valueOf(numHole-1), uid, yards, par, shots-1);
                    completeDialog();
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermissions();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onLocationChanged(Location location) {

        Location mLastLocation = location;
        if (mCurLocationMarker != null) {
            mCurLocationMarker.remove();
        }

        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        fabAddShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLocation();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permssions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void newLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location1 = locationManager.getLastKnownLocation(provider);

        if(location1!= null){
            double lat = location1.getLatitude();
            double longitude = location1.getLongitude();

            LatLng newLatLng = new LatLng(lat, longitude);

            mMap.addMarker(new MarkerOptions()
                    .position(newLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                    .title("Shot: " + shots));

            shots++;
            totalShots++;
        }
    }

    private void newHoleDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_holes);
        dialog.setCancelable(false);
        dialog.show();

        Button confirm = (Button) dialog.findViewById(R.id.btn_save_hole);

        holeNum = (EditText) dialog.findViewById(R.id.holeNum);
        holeYards = (EditText) dialog.findViewById(R.id.holeYards);
        holePar = (EditText) dialog.findViewById(R.id.holePar);

        holeNum.setText(String.valueOf(numHole));
        holeNum.setEnabled(false);

        holeYards.requestFocus();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numHole = Integer.parseInt(holeNum.getText().toString().trim());
                yards = holeYards.getText().toString().trim();
                par = holePar.getText().toString().trim();

                if((holeNum.getText().length() != 0) && (holeYards.getText().length() != 0) && (holePar.getText().length() != 0)){
                    numHole++;
                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please check you have entered the correct details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void completeDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_complete);
        dialog.setCancelable(false);
        dialog.show();

        Button finish = (Button) dialog.findViewById(R.id.btn_finish);
        txtShotsTotal = (TextView) dialog.findViewById(R.id.totalShots);

        txtShotsTotal.setText(String.valueOf(totalShots));

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MapsActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void createHole(final String numHole, final String uid, final String yards, final String par, final int shots){

        String tag_string_req = "req_create_hole";

        pDialog.setMessage("Updating scorecard...");
        showDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_CREATE_HOLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create hole response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean err = jsonObject.getBoolean("error");

                    if (!err) {
                        String uid = jsonObject.getString("uid");

                        JSONObject hole = jsonObject.getJSONObject("holes");
                        String numHole = hole.getString("holeNumber");
                        String yards = hole.getString("yards");
                        String par = hole.getString("par");
                        String shots = hole.getString("shots");
                        String courseId = hole.getString("id");

                        db.createHole(uid, numHole, courseId, yards, par, Integer.parseInt(shots));

                        Toast.makeText(getApplicationContext(), "Holes saved to scorecard ", Toast.LENGTH_LONG).show();
                    } else {
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Create Hole Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("holeNumber", numHole);
                params.put("id", uid);
                params.put("yards", yards);
                params.put("par", par);
                params.put("shots", String.valueOf(shots));

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
}

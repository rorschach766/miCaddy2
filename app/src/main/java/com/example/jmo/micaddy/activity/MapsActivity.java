package com.example.jmo.micaddy.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.example.jmo.micaddy.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by jmo on 21/03/2017.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (servicesOK()) {
            setContentView(R.layout.activity_maps);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMaps);
            toolbar.setTitle("Your Round");
            setSupportActionBar(toolbar);
            if(initMap()==true){
                Toast.makeText(this, "Maps services not connected", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Maps services connected", Toast.LENGTH_SHORT).show();
            }
        } else {
            setContentView(R.layout.fragment_rounds);
        }

        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_type, menu);
        return true;
    }

    public boolean servicesOK() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog =
                    googleApiAvailability.getErrorDialog(this, isAvailable, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap(){
       if(mMap == null){
           SupportMapFragment mapFragment =
                   (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           mapFragment.getMapAsync((OnMapReadyCallback) this);
       }
       return (mMap != null);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}

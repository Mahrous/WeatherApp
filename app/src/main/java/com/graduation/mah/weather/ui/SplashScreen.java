package com.graduation.mah.weather.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.graduation.mah.weather.R;
import com.graduation.mah.weather.service.InternetConnection;
import com.graduation.mah.weather.utils.GpsUtils;


public class SplashScreen extends AppCompatActivity {
    private static final int PERMISSION_FINE_LOCATION = 99;
    FusedLocationProviderClient locationProviderClient;

    TextView textView;

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    boolean isChecked = false;
    boolean isGPS = false;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    String userId;
    SharedPreferences prefLocation;

    Handler handler = new Handler();
    Runnable runnable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        textView=findViewById(R.id.err_txt);
        textView.setVisibility(View.INVISIBLE);

        SharedPreferences pref = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        isChecked = pref.getBoolean("isChecked", false);
        userId = pref.getString("id", "0");
        askPermission();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (InternetConnection.checkInternetConnection(getApplicationContext()) && gps_enabled) {
                    askLocation();

                        Intent mainIntent = new Intent(getApplicationContext(), BottomNav.class);
                        startActivity(mainIntent);
                        finish();



                } else {
                    isGPS = true;
                    setUpAgain();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable2);
    }

    private void askPermission() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (checkIfGPSIsOpened()) {
                askLocation();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }

    private void askLocation() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    prefLocation = getSharedPreferences("location", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefLocation.edit();
                    try {


                        String lo = String.valueOf(location.getLongitude());
                        String la = String.valueOf(location.getLatitude());
                        editor.putString("long", lo);
                        editor.putString("lat", la);
                        Log.e("TAG", "onSuccess: long = " + lo + " lat = " + la);
                        editor.commit();
                    } catch (Exception e) {
                        Log.e("TAG", "onSuccess: " + e.getMessage());
                    }
//                    editor.putString("address",location.st);
                    //Shared pref here
                    //now you have location

                }
            });
        }
    }

    private void setUpAgain() {

        handler.postDelayed(runnable2 = new Runnable() {
            public void run() {

                handler.postDelayed(runnable2, 100);
                if (InternetConnection.checkInternetConnection(getApplicationContext())) {
                    if (isGPS && gps_enabled) {
                        askLocation();
                        Intent mainIntent = new Intent(getApplicationContext(), BottomNav.class);
                        startActivity(mainIntent);
                        finish();
                        handler.removeCallbacks(runnable2);



                    }

                }else{
                    textView.setVisibility(View.VISIBLE);
                }


            }
        }, 100);

    }

    private boolean checkIfGPSIsOpened() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                    isGPS = isGPSEnable;
                }

            });
            gps_enabled = false;
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkIfGPSIsOpened()) {
                        askLocation();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), " This App  Requires Permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            isGPS = true; // flag maintain before get location
            gps_enabled = true;

        } else {
            isGPS = false;
            gps_enabled = false;

        }
    }


}


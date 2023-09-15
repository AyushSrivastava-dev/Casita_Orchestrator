package com.example.casita_v1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {
    //This class builds the first screen which is presented to the user.
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 1;
    private static int timer = 5000;
    ImageView imageView;
    TextView textView1, textView2;
    Animation upper, bottom;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if location services are enabled
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled || isNetworkEnabled) {

            if (isNetworkAvailable()) {

            } else {
                Toast.makeText(this, "Internet connection is not available", Toast.LENGTH_LONG).show();
            }
        } else {
            // Location services are disabled
            Toast.makeText(getApplicationContext(), "Please turn on GPS location and then open the application again", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE_LOCATION_SETTINGS);


        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {

                                    Double lat = location.getLatitude();
                                    Double longt = location.getLongitude();
                                    String city = getCityName(getApplicationContext(), lat, longt);
                                    Toast.makeText(getApplicationContext(), "City is : " + city, Toast.LENGTH_LONG).show();
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyLocation", MODE_PRIVATE);
                                    SharedPreferences.Editor myEdits = sharedPreferences.edit();

                                    String latitude = "" + lat;
                                    String longitude = "" + longt;
                                    myEdits.putString("lat", latitude);
                                    myEdits.putString("longt", longitude);

                                    myEdits.commit();
                                    init();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("LocationGps", "inside failure");
                                Toast.makeText(getApplicationContext(), "Please turn on GPS location", Toast.LENGTH_LONG).show();
                                SplashScreen.this.finish();
                                System.exit(0);
                            }
                        });

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        startActivity(new Intent(this, this.getClass()));
                        finish();

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                    }
                }
            }
        }
    }

    private void init() {
        imageView = findViewById(R.id.house);
        textView1 = findViewById(R.id.casita);
        textView2 = findViewById(R.id.details);

        upper = AnimationUtils.loadAnimation(this, R.anim.upper_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView.setAnimation(upper);
        textView1.setAnimation(bottom);
        textView2.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onStarting();
            }
        }, timer);
    }


    protected void onStarting() {
        super.onStart();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String user = sh.getString("username", "");
        String pass = sh.getString("password", "");
        Boolean checker = sh.getBoolean("checker", false);
        if (checker) {
            Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
            startActivity(intent);

        }
        if (!user.isEmpty()) {
            if (!pass.isEmpty()) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("datauser");

                Query check_username = databaseReference.orderByChild("username").equalTo(user);

                check_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            String passCheck = snapshot.child(user).child("pass").getValue(String.class);

                            if (passCheck.equals(pass)) {
                                Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                                startActivity(intent);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            // Check if location services are enabled
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled || isNetworkEnabled) {
                // Location services are enabled
                Toast.makeText(this, "Location services are enabled", Toast.LENGTH_LONG).show();
                if (isNetworkAvailable()) {
                    Toast.makeText(this, "Internet connection is available", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Internet connection is not available", Toast.LENGTH_LONG).show();
                }
                recreate();

            } else {
                // Location services are still disabled
                Toast.makeText(this, "Location services are still disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        // Get the ConnectivityManager
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get the active network info
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // Return true if network is available and connected, false otherwise
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String getCityName(Context context, double latitude, double longitude) {
        // Create a Geocoder object
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // Initialize a list of Address objects
        List<Address> addresses;

        try {
            // Get the addresses for the specified latitude and longitude
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        // If no addresses were found, return an empty string
        if (addresses == null || addresses.size() == 0) {
            return "";
        }

        // Get the city name from the first Address object in the list
        Address address = addresses.get(0);
        String cityName = address.getLocality();

        // Return the city name
        return cityName;
    }

}
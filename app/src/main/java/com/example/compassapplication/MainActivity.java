package com.example.compassapplication;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements SensorEventListener {
    private ImageView imageView;
    private SensorManager sensorManager;
    public TextView destinationDistance;
    private float currentDegree = 0f;
    private LocationFetcher locationFetcher;
    private Location location;
    private Button button;
    private float distance;

    private int permissionCheckAFL;
    private int permissionCheckACL;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageViewCompass);
        destinationDistance = findViewById(R.id.destinationDistance);
        button = findViewById(R.id.button);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        button.setOnClickListener(v -> {
            openSetGeographicCoordinatesActivity();
        });

        locationFetcher = new LocationFetcher(this);
        locationFetcher.connectGps();
        locationFetcher.onLocationChanged(location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        permissionCheckAFL = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        permissionCheckACL = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionCheck();
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
            location.getLatitude();
            location.getLongitude();
            Location destinationLocation;
            destinationLocation = SetGeographicCoordinatesActivity.getDestinationLocation();
            distance = location.distanceTo(destinationLocation);
            updateTextView(distance);
        }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        locationFetcher.disconnectGps();


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);

        RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        imageView.startAnimation(rotateAnimation);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void openSetGeographicCoordinatesActivity() {
        Intent intent = new Intent(this, SetGeographicCoordinatesActivity.class);
        startActivity(intent);
    }

    private void permissionCheck() {
        int REQUEST_CODE = 102;
        if(permissionCheckAFL != PackageManager.PERMISSION_GRANTED && permissionCheckACL != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
    }

    private void updateTextView(final float distance) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                destinationDistance.setText("Distance from the destination: " + Float.toString(distance) + "m");
                destinationDistance.invalidate();
            }
        });
    }
}
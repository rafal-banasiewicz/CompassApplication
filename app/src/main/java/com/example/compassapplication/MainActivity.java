package com.example.compassapplication;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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
    private TextView destinationDistance;
    private EditText destinationLatitude;
    private EditText destinationLongitude;
    private float currentDegree = 0f;
    private FusedLocationProviderClient fusedLocationClient;
    private Button button;
    float distance;
    int REQUEST_CODE = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheckAFL = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckACL = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheckAFL != PackageManager.PERMISSION_GRANTED && permissionCheckACL != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }

        imageView = findViewById(R.id.imageViewCompass);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        destinationLatitude = findViewById(R.id.destinationLatitude);
        destinationLongitude = findViewById(R.id.destinationLongitude);
        destinationDistance = findViewById(R.id.destinationDistance);
        button = findViewById(R.id.button);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Location destinationLocation = new Location("Destination");

        button.setOnClickListener(v -> {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    location.getLatitude();
                    location.getLongitude();
                    float destinationLat = Float.parseFloat(destinationLatitude.getText().toString());
                    float destinationLon = Float.parseFloat(destinationLongitude.getText().toString());
                    destinationLocation.setLatitude(destinationLat);
                    destinationLocation.setLongitude(destinationLon);
                    distance = location.distanceTo(destinationLocation);
                }
            });
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
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        destinationDistance.setText("Distance from the destination: " + Float.toString(distance) + "m");

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
}
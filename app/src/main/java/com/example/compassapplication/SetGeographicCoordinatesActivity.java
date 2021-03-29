package com.example.compassapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class SetGeographicCoordinatesActivity extends AppCompatActivity {
    private static Location destinationLocation;
    private Button button;
    private EditText destinationLatitude;
    private EditText destinationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_geograpic_coordinates);
        destinationLatitude = findViewById(R.id.destinationLatitude);
        destinationLongitude = findViewById(R.id.destinationLongitude);
        button = findViewById(R.id.button2);

        destinationLocation = new Location("Destination");

        button.setOnClickListener(v -> {
            setGeoCords();
        });
    }

    private void setGeoCords() {
        float destinationLat = Float.parseFloat(destinationLatitude.getText().toString());
        float destinationLon = Float.parseFloat(destinationLongitude.getText().toString());
        destinationLocation.setLatitude(destinationLat);
        destinationLocation.setLongitude(destinationLon);
        setDestinationLocation(destinationLocation);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setDestinationLocation(Location destLocation) {
        destinationLocation = destLocation;
    }

    public static Location getDestinationLocation() {
        return destinationLocation;
    }

}





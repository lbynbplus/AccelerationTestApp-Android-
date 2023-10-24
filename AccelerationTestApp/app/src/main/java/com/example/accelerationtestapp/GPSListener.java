package com.example.accelerationtestapp;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class GPSListener implements LocationListener {
    private GPSCallback gpsCallback;

    public interface GPSCallback {
        void onLocationDataChanged(Location locData);
    }

    public void setGPSCallback(GPSCallback callback) {
        this.gpsCallback = callback;
    }

    public void onNewLocationData(Location location) {
        if (gpsCallback != null) {
            gpsCallback.onLocationDataChanged(location);
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}

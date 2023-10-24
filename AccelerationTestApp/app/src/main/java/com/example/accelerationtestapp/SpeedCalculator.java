package com.example.accelerationtestapp;

import android.location.Location;

import com.example.accelerationtestapp.AccelerationListener.AccelerationCallback;
import com.example.accelerationtestapp.GPSListener.GPSCallback;

public class SpeedCalculator implements AccelerationCallback, GPSCallback{

    @Override
    public void onAccelerationDataChanged(float accData) {

    }

    @Override
    public void onLocationDataChanged(Location locData) {

    }
}

package com.example.accelerationtestapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerationListener implements SensorEventListener {
    private AccelerationCallback accelerationCallback;


    public interface AccelerationCallback {
        void onAccelerationDataChanged(float accData);
    }

    public void setAccelerationCallback(AccelerationCallback callback) {
        this.accelerationCallback = callback;
    }

    public void onNewAccelerationData(float accData) {
        if (accelerationCallback != null) {
            accelerationCallback.onAccelerationDataChanged(accData);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Temp: Using accelerometer data on a fixed axis as a fit to gps data
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float accData = sensorEvent.values[1]; // Y-axis Acc
            onNewAccelerationData(accData);
        }
        /*
        TODO:
        1.Calculate the angle between the mobile phone's coordinate system and the ground coordinate system using a magnetic field sensor.
        2.Convert the mobile phone coordinate system to the ground coordinate system and subtract the value of the acceleration of gravity.
        3.Find the direction of the largest vector in the current coordinate system, which is the
          value of the acceleration in the actual direction of motion of the car.
        */

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

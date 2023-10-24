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

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

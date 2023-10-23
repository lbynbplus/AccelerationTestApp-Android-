package com.example.accelerationtestapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener accelerationListener;

    private LocationManager locationManager;
    private LocationListener gpsListener;

    // UI Components
    private Button quarterMileButton;
    private Button eighthMileButton;
    private Button zeroTo100Button;
    private Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Acceleration Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerationListener = new AccelerationListener();
        sensorManager.registerListener(accelerationListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        // GPSListener
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsListener = new GPSListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: 请求权限
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsListener);

        // UI Initialization
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        // 获取按钮的引用
        quarterMileButton = findViewById(R.id.quarter_mile_button);
        eighthMileButton = findViewById(R.id.eighth_mile_button);
        zeroTo100Button = findViewById(R.id.zero_to_100_button);
        goButton = findViewById(R.id.car_selection);

        // 初始时，将Go按钮设置为不可见
        goButton.setVisibility(View.INVISIBLE);

        // 定义点击监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goButton.setVisibility(View.VISIBLE);
            }
        };

        // 为按钮设置点击监听器
        quarterMileButton.setOnClickListener(listener);
        eighthMileButton.setOnClickListener(listener);
        zeroTo100Button.setOnClickListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(accelerationListener);
        locationManager.removeUpdates(gpsListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(accelerationListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: 请求权限
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsListener);
    }
}

package com.example.accelerationtestapp;

import android.location.Location;
import android.util.Log;

import com.example.accelerationtestapp.AccelerationListener.AccelerationCallback;
import com.example.accelerationtestapp.GPSListener.GPSCallback;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SpeedCalculator implements AccelerationCallback, GPSCallback{
    private ArrayList<Float> accDataList = new ArrayList<>();
    private ArrayList<Location> locDataList = new ArrayList<>();
    private ArrayList<Float> AccPerHZ = new ArrayList<>();
    private ArrayList<Location> LocPerHZ = new ArrayList<>();
    private Location lastLocation = null;
    private Location startLoc = null;
    private Mode TestMode = null;
    private Timer timer;
    private long startTime;
    private long currentTime;

    public enum TestState {
        IDLE, // Not start yet
        WAITING_FOR_ACCELERATION, // Waiting Acc lager than 1.5
        RECORDING // Recording
    }
    private TestState state = TestState.IDLE;

    @Override
    public void onAccelerationDataChanged(float accData) {
        synchronized(AccPerHZ){
            switch (state) {
                case IDLE:
                    break;
                case WAITING_FOR_ACCELERATION:
                    if (accData > 1.5 && startLoc != null) {
                        state = TestState.RECORDING;
                        startTime = System.currentTimeMillis();
                        Log.d("TestState","Test start recording");
                    }
                    break;
                case RECORDING:
                    //accDataList.add(accData);
                    //AccPerHZ.add(accData);
                    break;
            }
        }
    }
    @Override
    public void onLocationDataChanged(Location locData) {
        float distance = 0;
        synchronized(LocPerHZ){
            switch (state) {
                case IDLE:
                    break;
                case WAITING_FOR_ACCELERATION:
                    startLoc = StartLocCal(lastLocation,locData);
                    break;
                case RECORDING:
                    if (startLoc != null) {
                        //locDataList.add(locData);
                        LocPerHZ.add(locData);
                        distance = lastLocation.distanceTo(startLoc);
                        Log.d("LocationListener", "Distance ：" + distance + "M");
                        currentTime = Math.max(locData.getTime(), currentTime);
                        //Log.d("CurrentTime", "Time:");
                    }
                    switch (TestMode) {
                        case ZERO_TO_100:
                            if (locData.getSpeed() > (110)) {
                                state = TestState.IDLE;
                            }
                            break;
                        case EIGHTH_MILE:
                            if (distance > 220) {
                                state = TestState.IDLE;
                            }
                            break;
                        case QUARTER_MILE:
                            if (distance > 420) {
                                state = TestState.IDLE;
                            }
                            break;
                    }
                    break;
            }
        }
        lastLocation = locData;
    }
    private Location StartLocCal(Location locData1,Location locData2){
        if(locData1 !=null && locData2 != null){
            double midLatitude = (locData1.getLatitude() + locData2.getLatitude()) / 2;
            double midLongitude = (locData1.getLongitude() + locData2.getLongitude()) / 2;
            double midAltitude = (locData1.getAltitude() + locData2.getAltitude())/2;

            Location midLocation = new Location("midpoint");
            midLocation.setLatitude(midLatitude);
            midLocation.setLongitude(midLongitude);
            midLocation.setAltitude(midAltitude);
            midLocation.setTime(locData2.getTime());
            return midLocation;
        }
        return null;
    }
    private void InitTest() {
        AccPerHZ.clear();
        LocPerHZ.clear();
        accDataList.clear();
        locDataList.clear();
    }
    public void Test(Mode mode) {
        InitTest();
        this.TestMode = mode;
        state = TestState.WAITING_FOR_ACCELERATION;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == TestState.RECORDING) {
                    // Process AccPerHZ and LocPerHZ
                    processPreHZ();
                } else if (state == TestState.IDLE) {
                    timer.cancel();
                }
            }
        }, 200, 200);
    }

    private void processPreHZ(){
        synchronized(LocPerHZ){
            double totalLatitude = 0;
            double totalLongitude = 0;
            float totalSpeed = 0;
            long lastTime;
            if (LocPerHZ.isEmpty()) {
                return;
            }
            if (locDataList.isEmpty()){
                lastTime = startTime;
            } else {
                Location lastLocData = locDataList.get(locDataList.size()-1);
                lastTime = lastLocData.getTime();
            }
            // TODO:Here it can be upgraded to a first order Taylor expansion for filtering
            for (Location loc : LocPerHZ) {
                totalLatitude += loc.getLatitude();
                totalLongitude += loc.getLongitude();
                totalSpeed += loc.getSpeed();
            }
            double avgLatitude = totalLatitude / LocPerHZ.size();
            double avgLongitude = totalLongitude / LocPerHZ.size();
            float avgSpeed = totalSpeed / LocPerHZ.size();
            Location midLocation = new Location("midpoint");
            midLocation.setLatitude(avgLatitude);
            midLocation.setLongitude(avgLongitude);
            midLocation.setSpeed(avgSpeed);
            midLocation.setTime(lastTime + 200);
            Log.d("processPreHZ", "Midpoint Latitude: " + avgLatitude + ", Longitude: " + avgLongitude);
            Log.d("processPreHZ", "Average Speed: " + avgSpeed);
            Log.d("processPreHZ", "Time Cost: " + (midLocation.getTime() - startTime));
            LocPerHZ.clear();
            locDataList.add(midLocation);
        }
    }
}

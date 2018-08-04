package com.example.johnb.motionsensor3in1;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.util.Log;

import java.util.Random;


public class TheService extends Service implements SensorEventListener {

    public static final String TAG = TheService.class.getName();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;
    private SensorManager mSensorManager = null;
    NotificationManager notificationManger;
    String temp = ""; //holds sensor output data
    Random r; //used for the main

    /*
     * Register this as a sensor event listener. Rapid sampling to drain battery.
     */
    private void registerListener() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_FASTEST);
    }

    /*
     * Un-register this as a sensor event listener.
     */
    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive(" + intent + ")");

            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                return;
            }

            Runnable runnable = new Runnable() {
                public void run() {
                    Log.i(TAG, "Runnable executing.");
                    unregisterListener();
                    registerListener();
                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
        }
    };



    /*
     * Samples sensors at high rates to drain extra battery. Does not drain as much as flaoting point math.
     */
    public void onSensorChanged(SensorEvent event) {

        Log.i(TAG, "onSensorChanged().");

        int sensorType = event.sensor.getType();

        if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            temp = "TYPE_MAGNETIC_FIELD, x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2];
        } else if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            temp = "TYPE_ACCELEROMETER, x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2];
        } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
            temp = "TYPE_GYROSCOPE, x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2];
        } else if (sensorType == Sensor.TYPE_LIGHT) {
            //temp = "TYPE_LIGHT, x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2];
        } else if (sensorType == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            //temp = "TYPE_AMBIENT_TEMPERATURE, x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2];
        } else {
            temp = "None";
        }
    }

    /*
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    + Creates a thread that uses floating point math and acquires a wakelock with releasing. +
    + Launches persistent Notification to stay active  to drain power when screen sleeps.    +
    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("MAIN", "Pressed Button");

        @SuppressLint("WrongConstant") PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, TheService.class), Notification.FLAG_ONGOING_EVENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle("This is the title");
        builder.setContentText("This is the text");
        builder.setSubText("Some sub text");
        builder.setNumber(101);
        builder.setOngoing(true);

        builder.setContentIntent(contentIntent);
        builder.setTicker("Fancy Notification");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();
        notificationManger =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(01, notification);

        /*
         +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         + Drains the most battery. Uses floating point math and acquires a wakelock with releasing. +
         +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         */

        Thread thread = new Thread() {

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");

            @Override
            public void run() {

                mWakeLock.acquire();

                while (true) {

                    r = new Random(System.currentTimeMillis());
                    double i = Math.exp(r.nextDouble());
                }

            }
        };

        thread.start();


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

    }

    /*
     * Restarts service after ends
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterListener();
        //mWakeLock.release();
        notificationManger.cancel(01);
        stopForeground(true);


        Intent in = new Intent();
        in.setAction("immortal_service");
        sendBroadcast(in);

    }

    /*
     * Starts the service
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startForeground(Process.myPid(), new Notification());
        registerListener();
        //mWakeLock.acquire();

        return START_STICKY;
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "onAccuracyChanged().");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
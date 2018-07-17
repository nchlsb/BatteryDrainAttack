package com.example.johnb.motionsensor3in1;

import android.app.Notification;
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
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;


public class TheService extends Service implements SensorEventListener {

    public static final String TAG = TheService.class.getName();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;

    private SensorManager mSensorManager = null;
    private WakeLock mWakeLock = null;

    /*
     * Register this as a sensor event listener.
     */
    private void registerListener() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);



        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
        Sensor mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);





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
            Log.i(TAG, "onReceive("+intent+")");

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

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "onAccuracyChanged().");
    }

    public void onSensorChanged(SensorEvent event) {

        Log.i(TAG, "onSensorChanged().");

        int sensorType = event.sensor.getType();

        if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            //mToast.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            //AccelerometerView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
            //GyroscopeView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_LIGHT) {
            //LightView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_AMBIENT_TEMPERATURE) { //all temp no longer valid
            //TemperatureView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "No Sensors Detected",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterListener();
        mWakeLock.release();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startForeground(Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();

        return START_STICKY;
    }
}
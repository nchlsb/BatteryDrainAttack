package com.example.johnb.motionsensor3in1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class theCombination extends AppCompatActivity implements SensorEventListener {


    Sensor mMagnetic;
    SensorManager mMagnetMan;
    public TextView MagnetView;

    Sensor mAccelerometer;
    SensorManager mAccelerometerMan;
    TextView AccelerometerView;

    Sensor mGyroscope;
    SensorManager mGyroscopeMan;
    TextView GyroscopeView;

    Sensor mLight;
    SensorManager mLightMan;
    TextView LightView;

    Sensor mTemperature;
    SensorManager mTemperaturetMan;
    TextView TemperatureView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_combination);


        mMagnetMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        mMagnetic = mMagnetMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mMagnetMan.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_FASTEST); //continuous samppling rate
        MagnetView = (TextView) findViewById(R.id.MagneticData);

        mAccelerometerMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mAccelerometerMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometerMan.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST); //continuous samppling rate
        AccelerometerView = (TextView) findViewById(R.id.AccelerometerData);

        mGyroscopeMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroscope = mGyroscopeMan.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGyroscopeMan.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST); //continuous samppling rate
        GyroscopeView = (TextView) findViewById(R.id.GyroscopeData);

        mLightMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mGyroscopeMan.getDefaultSensor(Sensor.TYPE_LIGHT);
        mLightMan.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST); //continuous samppling rate
        LightView = (TextView) findViewById(R.id.LightData);

        mTemperaturetMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTemperature = mTemperaturetMan.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mTemperaturetMan.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_FASTEST); //continuous samppling rate
        TemperatureView = (TextView) findViewById(R.id.TempuratureData);

        //from Github
        startService(new Intent(this, TheService.class));

        TextView wakeLockStatus = (TextView) findViewById(R.id.WakeLockStatus);

        BatteryManager mBatteryManager = (BatteryManager) getApplicationContext().getSystemService(Context.BATTERY_SERVICE);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, //used just Wakelock at first. Couldn't resolve symbol. idk why
                "MyWakelockTag");

        wakeLock.acquire();

        try {
            int x = 1 / 0;
            wakeLock.release();
        } catch (Exception E) {
        } finally {
            wakeLockStatus.setText(wakeLock.isHeld() + "");
        }

        int n = 100;

        int[][] a = new int[n][n];
        int[][] b = new int[n][n];
        int[][] c = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = n;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = n;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] = c[i][j] + a[i][k] * b[k][j];
                }
            }

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            MagnetView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            AccelerometerView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
            GyroscopeView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_LIGHT) {
            LightView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_AMBIENT_TEMPERATURE) { //all temp no longer valid
            TemperatureView.setText("x: " + event.values[0] + "\n" + "y: " + event.values[1] + "\n" + "z: " + event.values[2]);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "No Sensors Detected",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

//look at persisent notication

/* how to do a service
button should shart/stop
then do persisent notication w/ new service
 */
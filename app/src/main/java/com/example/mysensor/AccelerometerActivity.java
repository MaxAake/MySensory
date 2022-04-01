package com.example.mysensor;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {


    // device sensor manager
    private SensorManager mSensorManager;

    TextView x;
    TextView y;
    TextView z;
    float[] gravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleratometer);

        // TextView that will tell the user what degree is he heading
        x = (TextView) findViewById(R.id.x);
        y = (TextView) findViewById(R.id.y);
        z = (TextView) findViewById(R.id.z);
        gravity = new float[3];
        gravity[2] = (float) 9.8;

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getStringType().equals(Sensor.STRING_TYPE_ACCELEROMETER)) {
            float[] fact = event.values;
            float division = (float) 10.0;
            x.setText("X acceleration: " + Float.toString(Math.round((fact[0] - gravity[0]) * 10) / division));
            y.setText("Y acceleration: " + Float.toString(Math.round((fact[1] - gravity[1]) * 10) / division));
            z.setText("Z acceleration: " + Float.toString(Math.round((fact[2] - gravity[2]) * 10) / division)
            );
        }
        else if(event.sensor.getStringType().equals(Sensor.STRING_TYPE_GRAVITY)) {
            gravity = event.values;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
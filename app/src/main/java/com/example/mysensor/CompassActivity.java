package com.example.mysensor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;


public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    // define the display assembly compass picture
    private int counter = 0;
    private final int averageCount = 7;
    private double[] angles = new double[averageCount];
    private double angle;
    Random rand = new Random();
    private ImageView image;
    float[] rot = new float[9];
    float[] mag = new float[3];
    float[] grav = new float[3];
    float[] orient = new float[3];
    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
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

        // get the angle around the z-axis rotated
        /*if(false ){ //event.sensor.getStringType().equals(Sensor.STRING_TYPE_ORIENTATION)) {
            float division = (float) 10.0;
            float degree = Math.round(event.values[0] * 10.0);
            degree = degree / division;


            tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            // how long the animation will take place
            ra.setDuration(210);

            // set the animation after the end of the reservation status
            ra.setFillAfter(true);

            // Start the animation
            image.startAnimation(ra);
            currentDegree = -degree;
        }

         */
        if ((event.sensor.getStringType().equals(Sensor.STRING_TYPE_GRAVITY))){
            grav = event.values;
        }

        else if ((event.sensor.getStringType().equals(Sensor.STRING_TYPE_MAGNETIC_FIELD))){
            mag = event.values;
        }

        SensorManager.getRotationMatrix(rot, null, grav, mag);
        SensorManager.getOrientation(rot, orient);
        angle = orient[0];
        angle = (Math.toDegrees(angle) + 360) % 360.0;
        image.setRotation((float) -angle);
        tvHeading.setText("Heading: " + Double.toString(angle) + " degrees");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
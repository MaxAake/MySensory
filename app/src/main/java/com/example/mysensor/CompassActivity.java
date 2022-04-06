package com.example.mysensor;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
import java.io.*;


public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    // define the display assembly compass picture
    private int averageLength = 15;
    private float[] angles = new float[averageLength];
    private float angle;
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
        if ((event.sensor.getStringType().equals(Sensor.STRING_TYPE_GRAVITY))){
            grav = event.values;
        }

        else if ((event.sensor.getStringType().equals(Sensor.STRING_TYPE_MAGNETIC_FIELD))){
            mag = event.values;
        }

        SensorManager.getRotationMatrix(rot, null, grav, mag);
        SensorManager.getOrientation(rot, orient);
        for(int i = 0; i < averageLength-1; i++){
            angles[i+1] = angles[i];
        }
        angles[0] = orient[0];
        angle = 0;
        int numberNegative = 0;
        int numberSmall = 0; //Detta hade varit lättare med vectors, men detta är mycket roligare
        for(float i : angles) {
            if(i < 0) {
                numberNegative++;
            }
            if(Math.abs(i) < Math.PI/2){
                numberSmall++;
            }
        }
        if(numberNegative == 0 || numberNegative == averageLength || numberSmall>averageLength/2){
            for(float i : angles) {
                angle += i;
            }
            angle = (float) (angle / ((float) averageLength));
        }
        else{ //calculates the average as though we are around 360/0 degrees, this removes the problem of calculating the average around 180.
            float[] flippedAngles = new float[averageLength];
            int counter = 0;
            for(float i : angles) {
                if(i > 0){
                    flippedAngles[counter] = (float) (Math.PI - i);
                }
                else {
                    flippedAngles[counter] = (float) (-Math.PI - i);
                }
                counter++;
            }
            for(float i: flippedAngles) {
                angle += i;
            }
            angle = (float) (angle / ((float) averageLength));
            if(angle > 0) {
                angle = (float) (Math.PI - angle);
            }
            else {
                angle = (float) (-Math.PI - angle);
            }

        }

        angle = (float) ((Math.toDegrees(angle) + 360) % 360.0);
        image.setRotation((float) -angle);
        String degreeText = Integer.toString((Math.round(angle)));

        while(degreeText.length() < 3) {
            degreeText = "0" + degreeText; //This stops the centered text in tvHeading from moving due to shifting length
        }
        tvHeading.setText("Heading: " + degreeText + " degrees");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
package com.example.sensorapp;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class compass extends AppCompatActivity implements SensorEventListener{

    private ImageView img_compass;
    private TextView txt_angle;
    private int mAngle;
    private SensorManager msensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    private float[] rMat = new float[9];
    private float[] orientation = new float[9];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean haveSensor = false, haveSensor2 = false;
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        msensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        img_compass = (ImageView) findViewById(R.id.image_compass);
        txt_angle = (TextView) findViewById(R.id.txt_angle);

        start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            SensorManager.getRotationMatrixFromVector(rMat,event.values);
            mAngle = (int) (Math.toDegrees(SensorManager.getOrientation(rMat,orientation)[0]+360)%360);
        }
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values,0,mLastAccelerometer,0,event.values.length);
            mLastAccelerometerSet = true;
        }
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(event.values,0,mLastMagnetometer,0,event.values.length);
            mLastMagnetometerSet = true;
        }
        if(mLastMagnetometerSet && mLastAccelerometerSet){
            SensorManager.getRotationMatrix(rMat,null,mLastAccelerometer,mLastMagnetometer);
            SensorManager.getOrientation(rMat,orientation);
            mAngle = (int) (Math.toDegrees(SensorManager.getOrientation(rMat,orientation)[0]+360)%360);
        }
        mAngle = Math.round(mAngle);
        img_compass.setRotation(-mAngle);

        String where = "NO";
        if(mAngle >= 350 || mAngle <= 10)
            where = "N";
        else if(mAngle < 350 && mAngle > 280)
            where = "NW";
        else if(mAngle <= 280 && mAngle > 260)
            where = "W";
        else if(mAngle <= 260 && mAngle > 190)
            where = "SW";
        else if(mAngle <= 190 && mAngle > 170)
            where = "S";
        else if(mAngle <= 170 && mAngle > 100)
            where = "SE";
        else if(mAngle <= 100 && mAngle > 80)
            where = "E";
        else
            where = "NE";

        txt_angle.setText(mAngle + " " + where);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start(){
        if(msensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null){
            if(msensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null ||
                    msensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
                noSensorAlert();
            }
            else{
                mAccelerometer = msensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = msensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = msensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = msensorManager.registerListener(this,mMagnetometer,SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = msensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = msensorManager.registerListener(this,mRotationV,SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the compass").
                setCancelable(false).
                setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
    }

    public void stop(){
        if(haveSensor && haveSensor2){
            msensorManager.unregisterListener(this,mAccelerometer);
            msensorManager.unregisterListener(this,mMagnetometer);
        }
        else{
            if(haveSensor){
                msensorManager.unregisterListener(this,mRotationV);
            }
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        stop();
    }
    @Override
    protected void onResume(){
        super.onResume();
        start();
    }
}
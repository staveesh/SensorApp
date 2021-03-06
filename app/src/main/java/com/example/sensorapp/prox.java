package com.example.sensorapp;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class prox extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor mSensor;
//    private ImageView imgviw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prox);
//        imgviw = (ImageView) findViewById(R.id.imgv);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(mSensor == null){
            Toast.makeText(this,"Proximity sensor is not available", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            sensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.values[0] < mSensor.getMaximumRange()){
            getWindow().getDecorView().setBackgroundColor(Color.RED);
//            imgviw.setImageResource(R.drawable.monkey);
        }
        else{
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);

//            imgviw.setImageResource(R.drawable.minku);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

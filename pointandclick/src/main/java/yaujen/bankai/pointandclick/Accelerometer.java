package yaujen.bankai.pointandclick;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer implements SensorEventListener {

    public SensorManager mSensorManager;
    public Sensor mSensor;
    private long lastUpdate;
    private float x, y, z;


    public Accelerometer(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {


            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 20) {

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                lastUpdate = curTime;
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}

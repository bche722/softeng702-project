package yaujen.bankai.pointandclick;

import android.content.Context;
import android.hardware.SensorManager;

import java.util.LinkedList;


public class ReverseControl {

    private Accelerometer accelerometer;
    private LinkedList<Double> xVals;
    private LinkedList<Double> yVals;

    private static final int SIZE = 20;

    private double lastX;
    private double lastY;

    private double xVelocity;
    private double yVelocity;

    public ReverseControl (Context context) {
        accelerometer = new Accelerometer(context);
        accelerometer.mSensorManager.registerListener(accelerometer, accelerometer.mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        xVelocity = 0;
        yVelocity = 0;
        xVals = new LinkedList<>();
        yVals = new LinkedList<>();
    }

    public double getXOffset() {

        double avg = updateList(accelerometer.getX(), xVals);
        double result = 100*(avg - lastX);
        lastX = avg;

        System.out.println("x: "+result);


        xVelocity += result*0.02;

        return xVelocity*20;
    }

    public double getYOffset() {

        double avg = updateList(accelerometer.getY(), xVals);
        double result = 100*(avg - lastY);
        lastY = avg;

        System.out.println("y: "+result);


        yVelocity += result*0.02;

        return yVelocity*20;
    }

    private double updateList(double val, LinkedList<Double> list) {
        if (list.size()==SIZE) {
            list.pop();
        }
        list.addLast(val);
        double avg = 0;
        for (Double d:list) {
            avg += d;
        }
        avg = avg/list.size();
        return avg;
    }




}

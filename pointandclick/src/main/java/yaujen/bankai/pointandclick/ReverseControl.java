package yaujen.bankai.pointandclick;

import android.content.Context;
import android.hardware.SensorManager;

import java.util.LinkedList;

/**
 * A class to calculate values for the reverse control method, which aims to keep the cursor
 * stationary in 2D space while the user moves the phone.
 */
public class ReverseControl {

    private Accelerometer accelerometer;
    private LinkedList<Double> xVals;
    private LinkedList<Double> yVals;

    private static final int SIZE = 20;

    private double lastX;
    private double lastY;

    private double xVelocity;
    private double yVelocity;

    private double xAccel;
    private double yAccel;

    private boolean UP;
    private boolean LEFT;


    /**
     * constructor
     * @param context
     */
    public ReverseControl (Context context) {
        accelerometer = new Accelerometer(context);
        accelerometer.mSensorManager.registerListener(accelerometer, accelerometer.mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        xVelocity = 0;
        yVelocity = 0;
        lastX = 0;
        lastY = 0;
        xVals = new LinkedList<>();
        yVals = new LinkedList<>();
        UP = true;
        LEFT = true;
        xAccel = 0;
        yAccel = 0;
    }

    /**
     * the x value to offset the cursor.
     * @return
     */
    public double getXOffset() {
        xAccel = accelerometer.getX();
        double avg = updateList(xAccel, xVals);

        if (avg > lastX) {
            if (LEFT = true) {
                xVelocity = 0;
            }
            LEFT = false;
        } else {
            if (LEFT = false) {
                xVelocity = 0;
            }
            LEFT = true;
        }


        lastX = avg;





        xVelocity += accelerometer.getX()*0.02;

        if (xVelocity > 2) {
            xVelocity = 2;
        } else if (xVelocity < -2) {
            xVelocity = -2;
        }



        return xVelocity*20;
    }

    /**
     * the y value to offset the cursor
     * @return
     */
    public double getYOffset() {

        yAccel = accelerometer.getY();
        double avg = updateList(yAccel, xVals);

        if (avg < lastY) {
            if (UP = true) {
                yVelocity = 0;
            }
            UP = false;
        } else {
            if (UP = false) {
                yVelocity = 0;
            }
            UP = true;
        }


        yVelocity += accelerometer.getY()*0.02;

        if (yVelocity > 2) {
            yVelocity = 2;
        } else if (yVelocity < -2) {
            yVelocity = -2;
        }


        print();
        return -yVelocity*20;
    }

    private void print() {

        System.out.println("Velocity:");
        System.out.println("x: "+xVelocity+ "   y: "+ -yVelocity);
        System.out.println("Acceleration:");
        System.out.println("x: "+xAccel+ "   y: "+ yAccel);
        System.out.println("\n\n");
    }

    /**
     * uses a linked list of past values to smooth out data.
     * @param val
     * @param list
     * @return
     */
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

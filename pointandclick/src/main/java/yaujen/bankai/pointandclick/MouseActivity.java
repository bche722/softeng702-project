package yaujen.bankai.pointandclick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

import static yaujen.bankai.pointandclick.Utility.aLog;

public abstract class MouseActivity extends AppCompatActivity implements SensorEventListener {


    private boolean keyDown;

    protected MovableFloatingActionButton buttonClicker;

    //Sensor Fields
    private SensorFusion sensorFusion;
    private SensorManager sensorManager;

    //Sensor values
    private int initialX;
    private int initialY;
    private double currentPitch;
    private double refPitch;
    private double currentRoll;
    private double refRoll;

    private CustomizedQueue xOffsetQueue;
    private CustomizedQueue yOffsetQueue;

    protected Bundle extras;


    private BackTapService backTapService;


    // Tilt configurations
    private int posTiltGain = 35; // step size of position tilt
    private int velTiltGain = 100;
    private final double SAMPLING_RATE = 0.02;
    private final float BEZEL_THRESHHOLD = 50.0f;

    private ControlMethod controlMethod = ControlMethod.POSITION_CONTROL;
    private ClickingMethod clickingMethod;

    protected Mouse mouse;
    private int mouseWidth, mouseHeight, mouseOffsetX, mouseOffsetY;
    private Bitmap mouseBitmap;

    protected static final String KEY_NAME_CONTROL_METHOD = "CONTROL_METHOD";
    protected static final String KEY_NAME_TILT_GAIN = "TILT_GAIN";
    protected static final String KEY_NAME_CLICKING_METHOD = "CLICKING_METHOD";
    protected static final String KEY_NAME_CURSOR = "CURSOR";
    protected static final String KEY_NAME_CURSOR_W = "CURSOR_W";
    protected static final String KEY_NAME_CURSOR_H = "CURSOR_H";
    protected static final String KEY_NAME_CURSOR_OFFSET_X = "CURSOR_OFFSET_X";
    protected static final String KEY_NAME_CURSOR_OFFSET_Y = "CURSOR__OFFSET_Y";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        // Sensor configs
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        registerSensorManagerListeners();

        sensorFusion = new SensorFusion();
        sensorFusion.setMode(SensorFusion.Mode.FUSION);

        initialX = this.getResources().getDisplayMetrics().widthPixels / 2;
        initialY = this.getResources().getDisplayMetrics().heightPixels / 2;


        refPitch = sensorFusion.getPitch();
        refRoll = sensorFusion.getRoll();

        Log.d("testInit", refPitch + " xxxx " + refRoll);

        xOffsetQueue = new CustomizedQueue (50);
        yOffsetQueue = new CustomizedQueue (50);

        keyDown = false;

        backTapService = new BackTapService(this);




        initialiseMouse();


    }

    private void registerSensorManagerListeners() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }


    private void update() {
        currentRoll = sensorFusion.getRoll();
        double roll = currentRoll - refRoll; // rotation along x-axis
        currentPitch = sensorFusion.getPitch();
        double pitch = currentPitch - refPitch; // rotation along y-axis

        double tiltMagnitude = Math.sqrt(roll * roll + pitch * pitch);
        double tiltDirection = Math.asin(roll / tiltMagnitude);
        double velocity = velTiltGain * tiltMagnitude;
        double displacementPOS = tiltMagnitude * posTiltGain;
        double displacementVEL = velocity * SAMPLING_RATE;


        if(controlMethod == ControlMethod.POSITION_CONTROL){
            int xOffSet = (int) (displacementPOS*Math.sin(tiltDirection));
            xOffsetQueue.add ( xOffSet ) ;
            xOffSet = xOffsetQueue.getAverage ();
            Log.d ( "xOffsetAverageTest", xOffsetQueue.toString ());

            int yOffSet = (int) (displacementPOS*Math.cos(tiltDirection));
            if(pitch > 0){
                yOffSet = -yOffSet; // extra stuff that wasn't in original equation from paper ... hmmm
            }
            yOffsetQueue.add ( yOffSet );
            yOffSet = yOffsetQueue.getAverage();

            mouse.updateLocation(initialX + xOffSet,
                    initialY + yOffSet);
        } else {
            int xOffSet = (int) (displacementVEL * Math.sin(tiltDirection));
            int yOffSet = (int) (displacementVEL * Math.cos(tiltDirection));
            if (pitch > 0) {
                yOffSet = -yOffSet; // extra stuff that wasn't in original equation from paper ... hmmm
            }

            mouse.displace(xOffSet,
                    yOffSet);
        }

        if (keyDown) {
            simulateTouchMove();
        }


    }


    private void initialiseMouse() {
        Drawable drawableArrow = Objects.requireNonNull(ContextCompat.
                getDrawable(getBaseContext(), R.drawable.cursor));
        mouseWidth = 40;
        mouseHeight = 60;
        mouseOffsetX = 0;
        mouseOffsetY = 0;
        Mouse arrow = new Mouse(drawableArrow, initialX, initialY, mouseWidth, mouseHeight, mouseOffsetX, mouseOffsetY);
//        Mouse smallArrow = new Mouse(drawableArrow, getRealWidth(STANDARD_CURSOR_WIDTH / 2), getRealHeight(STANDARD_CURSOR_WIDTH / 2), 0, 0);
//        Mouse crosshair = new Mouse(drawableCrosshair, getRealWidth(60), getRealHeight(60), getRealWidth(30), getRealHeight(30));
//        Mouse smallCrosshair = new Mouse(drawableCrosshair, getRealWidth(30), getRealHeight(30), getRealWidth(15), getRealHeight(15));


        mouse = arrow;

    }

    protected void setupMouse(Bitmap m, int width, int height, int offsetX, int offsetY) {

        Drawable drawable = new BitmapDrawable(getResources(), m);
        mouseBitmap = m;
        mouse.setIcon(drawable);
        mouse.setWidth(width);
        mouse.setHeight(height);
        mouse.setOffsetX(offsetX);
        mouse.setOffsetY(offsetY);
        findViewById(android.R.id.content).getOverlay().add(mouse.getDrawable());
        mouse.updateLocation(initialX, initialY);


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorFusion.setAccel(event.values);
                sensorFusion.calculateAccMagOrientation();
                break;

            case Sensor.TYPE_GYROSCOPE:
                sensorFusion.gyroFunction(event);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorFusion.setMagnet(event.values);
                break;
        }
        update();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        Log.d("testKey", "keycode " + keyCode);
        if (keyCode == keyEvent.KEYCODE_BACK) {
            finish();
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            calibratePointer();
            Toast.makeText(this, "Calibrated pointer, pitch: " + getRefPitch() + ", roll: " + getRefRoll(), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (clickingMethod == ClickingMethod.VOLUME_DOWN) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                if (!keyDown) {

                    Log.d("testKey", "key down");
                    simulateTouchDown();
                    return true;
                } else {
                    simulateTouchMove();
                    return true;
                }
            }

        } else if (clickingMethod == ClickingMethod.BACK_TAP) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {

            }
        }


        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (clickingMethod == ClickingMethod.VOLUME_DOWN && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                Log.d("testKey", "key up");
                simulateTouchUp();
                return true;
            }
        }
        return true;
    }


    /**
     * Simulates touch down at current cursor location
     */
    public void simulateTouchDown() {
        long upTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
//        Log.d("testlay", findViewById(android.R.id.content).toString());


//        MotionEvent downEvent = MotionEvent.
//                obtain(upTime, eventTime, MotionEvent.ACTION_DOWN, (float) mouse.get_x(), (float) mouse.get_y(), 0);
        MotionEvent downEvent = MotionEvent.
                obtain(upTime, eventTime, MotionEvent.ACTION_DOWN, (float) mouse.getAverageX (), (float) mouse.getAverageY (), 0);
        findViewById(android.R.id.content).dispatchTouchEvent(downEvent);

        downEvent.setSource(420);
        keyDown = true;
        downEvent.recycle();
    }

    /**
     * Simulates touch up at current cursor location
     */
    public void simulateTouchUp() {
        long upTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent downEvent = MotionEvent.
                obtain(upTime, eventTime, MotionEvent.ACTION_UP, (float) mouse.getAverageX (), (float) mouse.getAverageY (), 0);
        findViewById(android.R.id.content).dispatchTouchEvent(downEvent);

        downEvent.setSource(420);
        keyDown = false;
        downEvent.recycle();
    }

    /**
     * Simulates touch move at current cursor location
     */
    public void simulateTouchMove() {
        long upTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        MotionEvent downEvent = MotionEvent.
                obtain(upTime, eventTime, MotionEvent.ACTION_MOVE, (float) mouse.getAverageX (), (float) mouse.getAverageY (), 0);
        findViewById(android.R.id.content).dispatchTouchEvent(downEvent);
        downEvent.setSource(420);

        downEvent.recycle();
    }

    /**
     * The movable button is hidden at the start, so please call the method {@link MouseView#setClickingMethod(ClickingMethod)}
     *
     * @param mFab
     */
    public void setMovableFloatingActionButton(MovableFloatingActionButton mFab) {
        buttonClicker = mFab;
        buttonClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateTouchDown();
            }
        });
        this.setVisbilityMovableFloatingActionButton(false);
    }


    public void setTiltGain(int tiltGain) {
        velTiltGain = tiltGain;
        posTiltGain = tiltGain;
    }

//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//        Log.d("testEdge", clickingMethod.toString());
//        if (event.getSource() == 420) {
//            Log.d("Bezel", "own source");
//            return super.onTouchEvent(event);
//        }
//
//        if (clickingMethod == ClickingMethod.BEZEL_SWIPE) {
//            Log.d("Bezel", "bezel");
//            Log.d("Bezel", event.getX() + " " + event.getY());
//            Log.d("Bezel", MotionEvent.actionToString(event.getAction()));
//
//            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//            Point size = new Point();
//            Display display = wm.getDefaultDisplay();
//            display.getSize(size);
//            int width = size.x;
//
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                if (event.getX() < BEZEL_THRESHHOLD) {
//                    Log.d("Bezel", "Touched left");
//                    simulateTouchDown();
//                    return true;
//                } else if (event.getX() > width - BEZEL_THRESHHOLD) {
//                    Log.d("Bezel", "Touched right");
//                    simulateTouchDown();
//                    return true;
//                }   else {
//                    Log.d("Bezel", "Didn't touch bezel");
//                    return false;
//                }
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                if (event.getX() < BEZEL_THRESHHOLD) {
//                    Log.d("Bezel", "Release left");
//                    simulateTouchUp();
//                    return true;
//                } else if (event.getX() > width - BEZEL_THRESHHOLD) {
//                    Log.d("Bezel", "Release right");
//                    simulateTouchUp();
//                    return true;
//                }   else {
//                    Log.d("Bezel", "Didn't touch bezel");
//                    return false;
//                }
//            }
//
//        }
//        return false;
//    }

    /**
     * Hides or shows the movable button
     *
     * @param visible
     */
    public void setVisbilityMovableFloatingActionButton(boolean visible) {
        if (buttonClicker != null) {
            buttonClicker.setVisibilityButton(visible);
        }
    }

    public ClickingMethod getClickingMethod() {
        return clickingMethod;
    }

    public void setClickingMethod(ClickingMethod clickingMethod) {
        this.clickingMethod = clickingMethod;
        this.setVisbilityMovableFloatingActionButton(false);
        backTapService.stopService();

        switch (clickingMethod) {
            case BACK_TAP:
                backTapService.startService();
                break;
            case BEZEL_SWIPE:
                break;
            case VOLUME_DOWN:
                break;
            case FLOATING_BUTTON:
                this.setVisbilityMovableFloatingActionButton(true);
                break;
            default:
                break;
        }
    }

    public void setControlMethod(ControlMethod controlMethod) {
        this.controlMethod = controlMethod;
    }


    //pausing the mouse view when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //running the mouse view when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        registerSensorManagerListeners();
    }

    /**
     * Convenience method to return the ConstraintLayout.LayoutParams for a wrapped view
     *
     * @param topMargin
     * @param rightMargin
     * @return Customised ConstraintLayout.LayoutParams for a wrapped view
     */
    public static ConstraintLayout.LayoutParams getFabConstraintLayoutParams(int topMargin, int rightMargin) {
        ConstraintLayout.LayoutParams fabParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        fabParams.rightToRight = rightMargin;
        fabParams.topToTop = topMargin;
        return fabParams;
    }

    /**
     * Calibrate the accelerometer based pointer to consider the current pitch and roll as the reference point, resting position
     */
    public void calibratePointer() {
        setRefPitch(currentPitch);
        setRefRoll(currentRoll);
//        if (controlMethod == ControlMethod.VELOCITY_CONTROL) {
        mouse.updateLocation(initialX, initialY);
//        }

        Toast.makeText(this, "Calibrated pointer, pitch: " + getRefPitch() + ", roll: " + getRefRoll(), Toast.LENGTH_SHORT).show();

    }


    protected Bundle packExtras() {
        extras = new Bundle();
        extras.putInt(KEY_NAME_CURSOR_W, mouseWidth);
        extras.putInt(KEY_NAME_CURSOR_H, mouseHeight);
        extras.putInt(KEY_NAME_CURSOR_OFFSET_X, mouseOffsetX);
        extras.putInt(KEY_NAME_CURSOR_OFFSET_Y, mouseOffsetY);
        extras.putParcelable(KEY_NAME_CURSOR, mouseBitmap);
        return extras;

    }



    public double getRefPitch() {
        return refPitch;
    }

    public void setRefPitch(double refPitch) {
        this.refPitch = refPitch;
    }

    public double getRefRoll() {
        return refRoll;
    }

    public void setRefRoll(double refRoll) {
        this.refRoll = refRoll;
    }

    public boolean getKeyDown() {
        return keyDown;
    }


}

package yaujen.bankai.pointandclick;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class MouseActivity extends AppCompatActivity implements SensorEventListener {



    private MovableFloatingActionButton buttonClicker;

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

    // Tilt configurations
    private int posTiltGain = 35; // step size of position tilt
    private int velTiltGain = 100;
    private final double SAMPLING_RATE = 0.02;
    private final float BEZEL_THRESHHOLD = 50.0f;

    private ControlMethod controlMethod = ControlMethod.POSITION_CONTROL;
    private ClickingMethod clickingMethod = ClickingMethod.VOLUME_DOWN;

    private List<Mouse> mice;
    private Mouse mouse;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Sensor configs
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        registerSensorManagerListeners();

        sensorFusion = new SensorFusion();
        sensorFusion.setMode(SensorFusion.Mode.FUSION);

        initialX = this.getResources().getDisplayMetrics().widthPixels/2;
        initialY = this.getResources().getDisplayMetrics().heightPixels/2;

        mice = new ArrayList<>();

        refPitch = 0;
        refRoll = 0;

        initialiseMice();
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
        double roll =  currentRoll - refRoll; // rotation along x-axis
        currentPitch = sensorFusion.getPitch();
        double pitch =  currentPitch - refPitch; // rotation along y-axis

        double tiltMagnitude = Math.sqrt(roll*roll + pitch*pitch);
        double tiltDirection = Math.asin(roll/tiltMagnitude);
        double velocity = velTiltGain *tiltMagnitude;
        double displacementPOS = tiltMagnitude* posTiltGain;
        double displacementVEL = velocity*SAMPLING_RATE;



        if(controlMethod == ControlMethod.POSITION_CONTROL){
            int xOffSet = (int) (displacementPOS*Math.sin(tiltDirection));
            int yOffSet = (int) (displacementPOS*Math.cos(tiltDirection));
            if(pitch > 0){
                yOffSet = -yOffSet; // extra stuff that wasn't in original equation from paper ... hmmm
            }

            mouse.displace((initialX + xOffSet),
                    (initialY + yOffSet));
        } else {
            int xOffSet = (int) (displacementVEL*Math.sin(tiltDirection));
            int yOffSet = (int) (displacementVEL*Math.cos(tiltDirection));
            if(pitch > 0){
                yOffSet = -yOffSet; // extra stuff that wasn't in original equation from paper ... hmmm
            }

            mouse.displace(xOffSet,
                    yOffSet);
        }
    }


    private void initialiseMice() {
        Drawable drawableArrow = Objects.requireNonNull(ContextCompat.
                getDrawable(getBaseContext(), R.drawable.cursor));
//        Drawable drawableCrosshair = Objects.requireNonNull(ContextCompat.
//                getDrawable(getBaseContext(), R.drawable.cursor2));

        Mouse arrow = new Mouse(drawableArrow, initialX, initialY, 40, 40);
//        Mouse smallArrow = new Mouse(drawableArrow, getRealWidth(STANDARD_CURSOR_WIDTH / 2), getRealHeight(STANDARD_CURSOR_WIDTH / 2), 0, 0);
//        Mouse crosshair = new Mouse(drawableCrosshair, getRealWidth(60), getRealHeight(60), getRealWidth(30), getRealHeight(30));
//        Mouse smallCrosshair = new Mouse(drawableCrosshair, getRealWidth(30), getRealHeight(30), getRealWidth(15), getRealHeight(15));

        mice.add(arrow);
//        mice.add(smallArrow);
//        mice.add(crosshair);
//        mice.add(smallCrosshair);

        mouse = mice.get(0);
        Log.d("testlay", findViewById(android.R.id.content).toString());
        findViewById(android.R.id.content).getOverlay().add(mouse.getDrawable());
        mouse.displace(0,0);
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
            Toast.makeText(this,"Calibrated pointer, pitch: "+ getRefPitch() + ", roll: "+getRefRoll(),Toast.LENGTH_SHORT).show();
            return true;
        }

        if (clickingMethod == ClickingMethod.VOLUME_DOWN && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                Log.d("testKey", "key down");
                simulateTouchDown();
                return true;
            }
        }


        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (clickingMethod == ClickingMethod.VOLUME_DOWN && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                Log.d("testing", "key up");
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

        MotionEvent downEvent = MotionEvent.
                obtain(upTime, eventTime, MotionEvent.ACTION_DOWN, (float) mouse.get_x(), (float) mouse.get_y(), 0);
        findViewById(android.R.id.content).dispatchTouchEvent(downEvent);
        downEvent.recycle();
    }

    /**
     * Simulates touch up at current cursor location
     */
    public void simulateTouchUp() {
        long upTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent downEvent = MotionEvent.
                obtain(upTime, eventTime, MotionEvent.ACTION_UP, (float) mouse.get_x(), (float) mouse.get_y(), 0);
        findViewById(android.R.id.content).dispatchTouchEvent(downEvent);
        downEvent.recycle();
    }

    /**
     * Simulates touch move at current cursor location
     */
    public void simulateTouchMove() {
        long upTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        MotionEvent downEvent = MotionEvent.
                obtain(upTime, eventTime, MotionEvent.ACTION_MOVE, (float) mouse.get_x(), (float) mouse.get_y(), 0);
        findViewById(android.R.id.content).dispatchTouchEvent(downEvent);
        downEvent.recycle();
    }

    /**
     * The movable button is hidden at the start, so please call the method {@link MouseView#setClickingMethod(ClickingMethod)}
     * @param mFab
     */
    public void setMovableFloatingActionButton(MovableFloatingActionButton mFab){
        buttonClicker = mFab;
        buttonClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                simulateTouchDown();
            }
        });
        this.setVisbilityMovableFloatingActionButton(false);
    }

    /**
     * Hides or shows the movable button
     * @param visible
     */
    public void setVisbilityMovableFloatingActionButton(boolean visible){
        if(buttonClicker != null){
            buttonClicker.setVisibilityButton(visible);
        }
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
     * Calibrate the accelerometer based pointer to consider the current pitch and roll as the reference point, resting position
     */
    public void calibratePointer(){
        setRefPitch(currentPitch);
        setRefRoll(currentRoll);
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


}

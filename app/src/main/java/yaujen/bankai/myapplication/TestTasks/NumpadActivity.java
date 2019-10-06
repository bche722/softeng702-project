package yaujen.bankai.myapplication.TestTasks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import yaujen.bankai.myapplication.AppUtility;
import yaujen.bankai.myapplication.ButtonClickTime;
import yaujen.bankai.myapplication.R;
import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;
import yaujen.bankai.pointandclick.Utility;


import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CURSOR;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CURSOR_H;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CURSOR_OFFSET_X;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CURSOR_OFFSET_Y;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CURSOR_W;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_DELAY;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_SMOOTH;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.TestTasks.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.TestTasks.ResultsActivity.KEY_NAME_TIME_TAKEN;

public class NumpadActivity extends MouseActivity {
    private TextView numberField;
    private ConstraintLayout constraintLayout;

    private ArrayList<Character> numList;
    private String numberToEnter = "";
    private int errorCount = 0;

    private boolean start = true;
    private boolean finish = false;
    private long startTime = 0;

    private Handler timer;


    String controlMethod;
    String clickingMethod;
    int tiltGain;

    private HashMap<String, Integer> keyboard_map = new HashMap<>();
    private AppUtility singleton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numpad);
        constraintLayout = findViewById(R.id.layout);
        numberField = findViewById(R.id.numField);

        singleton = AppUtility.getInstance();

        initAndShuffle();

        TextView number = findViewById(R.id.query);
        number.setText("Enter " + numberToEnter);


        // How to add fab clicking
        buttonClicker = new MovableFloatingActionButton(this);
        constraintLayout.addView(buttonClicker, constraintLayout.getChildCount(), getFabConstraintLayoutParams(100, 0));
        setMovableFloatingActionButton(buttonClicker);

        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));


        int smooth = Integer.parseInt(extras.getString(KEY_NAME_SMOOTH));
        int delay = Integer.parseInt(extras.getString(KEY_NAME_DELAY));

        setSmooth(smooth);
        setDelay(delay);
        setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        setControlMethod(ControlMethod.valueOf(controlMethod));
        setTiltGain(tiltGain);


        Bitmap mouseBitmap = getIntent().getParcelableExtra(KEY_NAME_CURSOR);
        setupMouse(mouseBitmap, extras.getInt(KEY_NAME_CURSOR_W), extras.getInt(KEY_NAME_CURSOR_H),
                extras.getInt(KEY_NAME_CURSOR_OFFSET_X), extras.getInt(KEY_NAME_CURSOR_OFFSET_Y));

        setup_keyboard_map();


    }

    private void initAndShuffle() {
        numList = new ArrayList<>();
        numList.add('0');
        numList.add('1');
        numList.add('2');
        numList.add('3');
        numList.add('4');
        numList.add('5');
        numList.add('6');
        numList.add('7');
        numList.add('8');
        numList.add('9');
        Collections.shuffle(numList);
        for (char c : numList) {
            numberToEnter = numberToEnter + c;
        }
    }


    //pausing the mouse view when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
    }

    //running the mouse view when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onStartClicku(View view){
        timer = new Handler();
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNext();
            }
        }, singleton.getTestTime());
        if(start){
            start = false;
            numberField.setText(" • • • • • • • • • •");
            findViewById(R.id.StartBtn).setVisibility(View.INVISIBLE);
            startTime = System.currentTimeMillis();
            change_keyboard_colour(numberToEnter.substring(0, 1), ButtonClickTime.NextClick);

        } else {
            incrementErrorCount();
        }
    }

    public void onFinish(){

        long timeTaken = System.currentTimeMillis() - startTime;


        if(finish){
            timer.removeCallbacksAndMessages(null);

            Utility.aLog("Time taken",timeTaken+"");
            goToNext();
//            Intent intent = new Intent(this, ResultsActivity.class);
//            intent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
//            intent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
//            intent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);
//
//            intent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken)/1000 + "s");
//            intent.putExtra(KEY_NAME_ERR_COUNT, errorCount);
//
//            startActivity(intent);
        } else {
            incrementErrorCount();
        }
    }

    public void onClicku(View view) {
        if (!start && !finish) {
            if (!numberToEnter.equals("")) {
                char clickuChar = ((Button) view).getText().charAt(0);
                Utility.aLog("Key clicked", clickuChar + "");
                if (numberToEnter.charAt(0) == clickuChar) {
                    change_keyboard_colour(numberToEnter.substring(0, 1), ButtonClickTime.CurrentClick);
                    if (numberToEnter.length() != 1) {
                        numberField.setText(numberField.getText().toString().replaceFirst(" •", clickuChar + ""));
                        numberToEnter = numberToEnter.substring(1);
                        change_keyboard_colour(numberToEnter.substring(0, 1), ButtonClickTime.NextClick);
                    } else {
                        numberField.setText(numberField.getText().toString().replaceFirst(" •", clickuChar + ""));
                        finish = true;
                        onFinish();
                    }
                } else {
                    incrementErrorCount();
                }
            }
        } else if (finish) {
            incrementErrorCount();
        }
    }

    public void onBadClicku(View view) {
        incrementErrorCount();
    }

    private void incrementErrorCount() {
        if (!start) {
            errorCount++;
            Utility.aLog("Err count", errorCount + "");
        }
    }

    private void setup_keyboard_map() {
        keyboard_map.put("0", new Integer(R.id.btn0));
        keyboard_map.put("1", new Integer(R.id.btn1));
        keyboard_map.put("2", new Integer(R.id.btn2));
        keyboard_map.put("3", new Integer(R.id.btn3));
        keyboard_map.put("4", new Integer(R.id.btn4));
        keyboard_map.put("5", new Integer(R.id.btn5));
        keyboard_map.put("6", new Integer(R.id.btn6));
        keyboard_map.put("7", new Integer(R.id.btn7));
        keyboard_map.put("8", new Integer(R.id.btn8));
        keyboard_map.put("9", new Integer(R.id.btn9));


        for (String s : keyboard_map.keySet()) {
            Button button = findViewById(keyboard_map.get(s));
            Drawable unwrappedDrawable = button.getBackground();
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#D7D7D7"));


        }
    }

    private void change_keyboard_colour(String s, ButtonClickTime b) {

        Button button = findViewById(keyboard_map.get(s));
        if (b == ButtonClickTime.CurrentClick) {
            Drawable unwrappedDrawable = button.getBackground();
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#D7D7D7"));

        } else {
            Log.d("testingButtonText", s);
            Drawable unwrappedDrawable = button.getBackground();
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#84D2FC"));

        }

    }

    private void goToNext() {
        long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("testexp", "NUM: "+timeTaken);
        singleton.incTimeTaken(timeTaken);
        singleton.setErrorCountNUM(errorCount);
        Intent intent = new Intent(this, NextActivity.class);
        extras = packExtras();
        intent.putExtra("BUNDLE", extras);
        startActivity(intent);
    }
}

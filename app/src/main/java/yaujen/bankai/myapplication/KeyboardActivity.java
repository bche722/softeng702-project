package yaujen.bankai.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_TIME_TAKEN;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;


import static yaujen.bankai.pointandclick.Utility.aLog;

public class KeyboardActivity extends MouseActivity {


    private ConstraintLayout constraintLayout;
    private TextView text;
    private TextView nextLetter;
    private String originalString = "the quick brown fox jumped over the lazy dog";
    private String textToWrite = "the quick brown fox jumped over the lazy dog";
    private int index = 0;
    private Button startButton;
    private long startTime;
    private int correctClicks;
    private int totalClicks;
    private boolean hasStarted = false;
    private Intent resultsIntent;
    private String controlMethod;
    private String clickingMethod;
    private int tiltGain;

    private HashMap<String, Integer> keyboard_map = new HashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        text = findViewById(R.id.textToWrite);
        constraintLayout = findViewById(R.id.layout);
        nextLetter = findViewById(R.id.nextLetter);
        nextLetter.setVisibility(View.INVISIBLE);


        // How to add fab clicking
        buttonClicker = new MovableFloatingActionButton(this);
        constraintLayout.addView(buttonClicker, constraintLayout.getChildCount(), getFabConstraintLayoutParams(100, 0));
        setMovableFloatingActionButton(buttonClicker);


        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));


        setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        setControlMethod(ControlMethod.valueOf(controlMethod));
        setTiltGain(tiltGain);

        setup_keyboard_map();


        aLog("Wikipedia", controlMethod);
        aLog("Wikipedia", clickingMethod);
        aLog("Wikipedia", tiltGain + "");


        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasStarted) {
                    hasStarted = true;
                    startTime = System.currentTimeMillis();
                    nextLetter.setVisibility(View.VISIBLE);
                    change_keyboard_colour("t",ButtonClickTime.NextClick);
                    colorString();
                    startButton.setVisibility(View.INVISIBLE);
                } else if (textToWrite.length() == 0) {
                    goToResults();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void colorString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<font color=\"#000000\">");
        sb.append(originalString.substring(0, index));
        sb.append("</font>");
        sb.append("<font color=\"#A9A9A9\">");
        sb.append(originalString.substring(index));
        sb.append("</font>");

        aLog("Keyboard", sb.toString());
        text.setText(android.text.Html.fromHtml(sb.toString()));
    }

    public void onClicku(View view) {
        String letter = (String) ((Button) view).getText();
        if (hasStarted) {
            totalClicks++;
        }

        if (hasStarted && textToWrite.length() > 0 && letter.equals(Character.toString(textToWrite.charAt(0)))) {
            index++;
            colorString();
            correctClicks++;

            textToWrite = textToWrite.substring(1);

            char nextChar = ' ';

//            findViewById(R.)

            if (textToWrite.length() > 0) {
                nextChar = textToWrite.charAt(0);
            }

            if (nextChar == ' ') {
                nextChar = '␣';
                change_keyboard_colour("space", ButtonClickTime.NextClick);
            } else {
                change_keyboard_colour(String.valueOf(nextChar), ButtonClickTime.NextClick);
            }

            if (letter.equals(" ")) {
                change_keyboard_colour("space", ButtonClickTime.CurrentClick);
            } else {
                change_keyboard_colour(letter, ButtonClickTime.CurrentClick);
            }

            nextLetter.setText("Next Letter: " + nextChar);

            if (textToWrite.length() == 0) {
                nextLetter.setText("Done!");
                startButton.setVisibility(View.VISIBLE);
                startButton.setText("View results");
                long timeTaken = System.currentTimeMillis() - startTime;

                resultsIntent = new Intent(this, ResultsActivity.class);
                resultsIntent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
                resultsIntent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
                resultsIntent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

                resultsIntent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken) / 1000 + "s");
                resultsIntent.putExtra(KEY_NAME_ERR_COUNT, totalClicks - correctClicks);
            }
        }
        aLog("errCount", totalClicks - correctClicks + "");

    }

    public void onBackClicku(View v) {
        if (hasStarted) {
            aLog("errCount", totalClicks - correctClicks + "");
            totalClicks++;
        }
    }

    private void goToResults() {
        aLog("Keyboard", "Task finished: " + correctClicks + "/" + totalClicks);
        startActivity(resultsIntent);
    }

//    @Override
//    public void onBackPressed() {
//        Log.d("testBck", "back pressed called");
//        Intent intent = new Intent(this, DemoActivity.class);
//        startActivity(intent);
//    }


    private void setup_keyboard_map() {
        keyboard_map.put("a", new Integer(R.id.a));
        keyboard_map.put("b", new Integer(R.id.b));
        keyboard_map.put("c", new Integer(R.id.c));
        keyboard_map.put("d", new Integer(R.id.d));
        keyboard_map.put("e", new Integer(R.id.e));
        keyboard_map.put("f", new Integer(R.id.f));
        keyboard_map.put("g", new Integer(R.id.g));
        keyboard_map.put("h", new Integer(R.id.h));
        keyboard_map.put("i", new Integer(R.id.i));
        keyboard_map.put("j", new Integer(R.id.j));
        keyboard_map.put("k", new Integer(R.id.k));
        keyboard_map.put("l", new Integer(R.id.l));
        keyboard_map.put("m", new Integer(R.id.m));
        keyboard_map.put("n", new Integer(R.id.n));
        keyboard_map.put("o", new Integer(R.id.o));
        keyboard_map.put("p", new Integer(R.id.p));
        keyboard_map.put("q", new Integer(R.id.q));
        keyboard_map.put("r", new Integer(R.id.r));
        keyboard_map.put("s", new Integer(R.id.s));
        keyboard_map.put("t", new Integer(R.id.t));
        keyboard_map.put("u", new Integer(R.id.u));
        keyboard_map.put("v", new Integer(R.id.v));
        keyboard_map.put("w", new Integer(R.id.w));
        keyboard_map.put("x", new Integer(R.id.x));
        keyboard_map.put("y", new Integer(R.id.y));
        keyboard_map.put("z", new Integer(R.id.z));
        keyboard_map.put("space", new Integer(R.id.space));
        for (String s : keyboard_map.keySet()){
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
}

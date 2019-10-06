package yaujen.bankai.myapplication.TestTasks;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.TestTasks.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.TestTasks.ResultsActivity.KEY_NAME_TIME_TAKEN;

import yaujen.bankai.myapplication.AppUtility;
import yaujen.bankai.myapplication.R;
import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
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


    private AppUtility singleton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        text = findViewById(R.id.textToWrite);
        constraintLayout = findViewById(R.id.layout);
        nextLetter = findViewById(R.id.nextLetter);

        singleton = AppUtility.getInstance();

        // How to add fab clicking
        buttonClicker = new MovableFloatingActionButton(this);
        constraintLayout.addView(buttonClicker, constraintLayout.getChildCount(),getFabConstraintLayoutParams(100,0));
        setMovableFloatingActionButton(buttonClicker);






         // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));


        setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        setControlMethod(ControlMethod.valueOf(controlMethod));
        setTiltGain(tiltGain);


        aLog("Wikipedia", controlMethod);
        aLog("Wikipedia", clickingMethod);
        aLog("Wikipedia", tiltGain + "");



        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            Handler timer = new Handler();
            @Override
            public void onClick(View view) {
                if (!hasStarted) {
                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToNext();
                        }
                    }, singleton.getTestTime());
                    hasStarted = true;
                    startTime = System.currentTimeMillis();
                    colorString();
                    startButton.setVisibility(View.INVISIBLE);
                } else if (textToWrite.length() == 0){
                    timer.removeCallbacksAndMessages(null);
                    goToNext();
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
        sb.append(originalString.substring(0,index));
        sb.append("</font>");
        sb.append("<font color=\"#A9A9A9\">");
        sb.append(originalString.substring(index));
        sb.append("</font>");

        aLog("Keyboard", sb.toString());
        text.setText(android.text.Html.fromHtml(sb.toString()));
    }

    public void onClicku(View view) {
        String letter = (String)((Button)view).getText();
        if (hasStarted) {
            totalClicks++;
        }

        if (hasStarted && textToWrite.length() > 0 && letter.equals(Character.toString(textToWrite.charAt(0)))) {
            index++;
            colorString();
            correctClicks++;

            textToWrite = textToWrite.substring(1);

            char nextChar = ' ';

            if(textToWrite.length() > 0) {
                nextChar = textToWrite.charAt(0);
            }

            if (nextChar == ' ') {
                nextChar = '␣';
            }

            nextLetter.setText("Next Letter: " + nextChar);

            long timeTaken = System.currentTimeMillis() - startTime;

            if (textToWrite.length() == 0) {
                nextLetter.setText("Done!");
                startButton.setVisibility(View.VISIBLE);
                startButton.setText("Continue");


                resultsIntent = new Intent(this, ResultsActivity.class);
                resultsIntent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
                resultsIntent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
                resultsIntent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

                resultsIntent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken)/1000 + "s");
                resultsIntent.putExtra(KEY_NAME_ERR_COUNT, totalClicks - correctClicks);
            }
        }
        aLog("errCount", totalClicks-correctClicks + "" );

    }

    public void onBackClicku(View v) {
        if (hasStarted) {
            aLog("errCount", totalClicks-correctClicks + "" );
            totalClicks++;
        }
    }

    private void goToResults() {
        aLog("Keyboard", "Task finished: " + correctClicks + "/" + totalClicks);
        startActivity(resultsIntent);
    }

    private void goToNext() {
        long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("testexp", "KEY: "+timeTaken);
        singleton.setErrorCountKEY(totalClicks - correctClicks);
        singleton.incTimeTaken(timeTaken);
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onBackPressed() {
//        Log.d("testBck", "back pressed called");
//        Intent intent = new Intent(this, DemoActivity.class);
//        startActivity(intent);
//    }
}
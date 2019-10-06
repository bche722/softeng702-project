package yaujen.bankai.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Random;

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
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_TIME_TAKEN;

public class RandomBtnActivity extends MouseActivity {

    private ConstraintLayout constraintLayout;

    String controlMethod;
    String clickingMethod;
    int tiltGain;

    private boolean start = true;
    private boolean finish = false;
    private long startTime = 0;

    private int errorCount = 0;

    private Random randomNum;

    private int screenX = 0;
    private int screenY = 0;
    private int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_btn);
        constraintLayout = findViewById(R.id.layout);

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

        Bitmap mouseBitmap = getIntent().getParcelableExtra(KEY_NAME_CURSOR);
        setupMouse(mouseBitmap, extras.getInt(KEY_NAME_CURSOR_W), extras.getInt(KEY_NAME_CURSOR_H),
                extras.getInt(KEY_NAME_CURSOR_OFFSET_X), extras.getInt(KEY_NAME_CURSOR_OFFSET_Y));


        findViewById(R.id.randomBtn).setVisibility(View.INVISIBLE);


        randomNum = new Random();

        screenX = this.getResources().getDisplayMetrics().widthPixels;
        screenY = this.getResources().getDisplayMetrics().heightPixels;


    }


    private int getNextXpos() {

        return randomNum.nextInt(screenX - findViewById(R.id.randomBtn).getWidth());
    }

    private int getNextYpos() {
        return randomNum.nextInt(screenY - findViewById(R.id.randomBtn).getHeight());
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


    public void onStartClicku(View view) {
        if (start) {
            start = false;
            findViewById(R.id.startBtn).setVisibility(View.INVISIBLE);
            findViewById(R.id.randomBtn).setVisibility(View.VISIBLE);

            startTime = System.currentTimeMillis();
        } else {
            incrementErrorCount();
        }
    }

    public void onClicku(View view) {
        if (!start && !finish) {
            findViewById(R.id.randomBtn).setX(getNextXpos());
            findViewById(R.id.randomBtn).setY(getNextYpos());
            count++;

            if (count == 10) {
                finish = true;
                long timeTaken = System.currentTimeMillis() - startTime;
                Utility.aLog("Time taken", timeTaken + "");

                Intent intent = new Intent(this, ResultsActivity.class);
                intent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
                intent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
                intent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

                intent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken) / 1000 + "s");
                intent.putExtra(KEY_NAME_ERR_COUNT, errorCount);

                startActivity(intent);
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


    public void onFinishClicku(View view) {
        if (finish) {
            long timeTaken = System.currentTimeMillis() - startTime;
            Utility.aLog("Time taken", timeTaken + "");

            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
            intent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
            intent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

            intent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken) / 1000 + "s");
            intent.putExtra(KEY_NAME_ERR_COUNT, errorCount);

            startActivity(intent);
        } else {
            incrementErrorCount();
        }
    }

}

package yaujen.bankai.myapplication.Draw;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;


import yaujen.bankai.myapplication.DemoActivity;
import yaujen.bankai.myapplication.R;
import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MouseView;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;

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

public class DrawActivity extends MouseActivity {

    private MouseView mouseView;
    private ConstraintLayout constraintLayout;

    private PaintView paintView;


    String controlMethod ;
    String clickingMethod;
    int tiltGain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        constraintLayout = findViewById(R.id.layout);


        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        // How to add fab clicking
        buttonClicker = new MovableFloatingActionButton(this);
        constraintLayout.addView(buttonClicker, constraintLayout.getChildCount(),getFabConstraintLayoutParams(100,0));
        setMovableFloatingActionButton(buttonClicker);

        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod  = extras.getString(KEY_NAME_CONTROL_METHOD);
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
                extras.getInt(KEY_NAME_CURSOR_OFFSET_X),extras.getInt(KEY_NAME_CURSOR_OFFSET_Y));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}



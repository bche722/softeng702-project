package yaujen.bankai.myapplication.Draw;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;


import yaujen.bankai.myapplication.DemoActivity;
import yaujen.bankai.myapplication.R;
import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MouseView;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
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

        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod  = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));




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



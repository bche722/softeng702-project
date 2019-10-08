package yaujen.bankai.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_DELAY;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_SMOOTH;

public class BigimageActivity extends MouseActivity {

    private ConstraintLayout constraintLayout;

    String controlMethod;
    String clickingMethod;
    int tiltGain;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
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

        int smooth = Integer.parseInt(extras.getString(KEY_NAME_SMOOTH));
        int delay = Integer.parseInt(extras.getString(KEY_NAME_DELAY));

        setSmooth(smooth);
        setDelay(delay);
        setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        setControlMethod(ControlMethod.valueOf(controlMethod));
        setTiltGain(tiltGain);

        setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        setControlMethod(ControlMethod.valueOf(controlMethod));
        setTiltGain(tiltGain);

        Bitmap mouseBitmap = getIntent().getParcelableExtra(KEY_NAME_CURSOR);
        setupMouse(mouseBitmap, extras.getInt(KEY_NAME_CURSOR_W), extras.getInt(KEY_NAME_CURSOR_H),
                extras.getInt(KEY_NAME_CURSOR_OFFSET_X), extras.getInt(KEY_NAME_CURSOR_OFFSET_Y));


    }
}

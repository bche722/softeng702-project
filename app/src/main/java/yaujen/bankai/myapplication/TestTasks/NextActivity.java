package yaujen.bankai.myapplication.TestTasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yaujen.bankai.myapplication.R;
import yaujen.bankai.myapplication.AppUtility;
import yaujen.bankai.pointandclick.MouseActivity;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_DELAY;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_SMOOTH;

public class NextActivity extends MouseActivity {

    private AppUtility singleton;

    public static final String KEY_NAME_CONTROL_METHOD = "CONTROL_METHOD";
    public static final String KEY_NAME_TILT_GAIN = "TILT_GAIN";
    public static final String KEY_NAME_CLICKING_METHOD = "CLICKING_METHOD";
    public static final String KEY_NAME_TIME_TAKEN = "TIME_TAKEN";
    public static final String KEY_NAME_ERR_COUNT = "ERR_COUNT";

    Button next;
    TextView textView;

    Class nextTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        extras = getIntent().getExtras().getBundle("BUNDLE");

        singleton = AppUtility.getInstance();

        nextTask = singleton.getTask();


        next = findViewById(R.id.next);
        textView = findViewById(R.id.nextText);

        if (nextTask == null) {
            goToResults();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNextTask();
            }
        });
    }


    private void goNextTask() {

        if(nextTask != null) {
            Intent intent = new Intent(this, nextTask);
            intent.putExtra(KEY_NAME_CONTROL_METHOD, singleton.getControlMethod());
            intent.putExtra(KEY_NAME_TILT_GAIN, singleton.getTiltGain());
            intent.putExtra(KEY_NAME_CLICKING_METHOD, singleton.getClickingMethod());
            intent.putExtra(KEY_NAME_SMOOTH, singleton.getSmooth());
            intent.putExtra(KEY_NAME_DELAY, singleton.getDelay());

            intent.putExtra(KEY_NAME_CURSOR_W, extras.getInt(KEY_NAME_CURSOR_W));
            intent.putExtra(KEY_NAME_CURSOR_H, extras.getInt(KEY_NAME_CURSOR_H));
            intent.putExtra(KEY_NAME_CURSOR_OFFSET_X, extras.getInt(KEY_NAME_CURSOR_OFFSET_X));
            intent.putExtra(KEY_NAME_CURSOR_OFFSET_Y, extras.getInt(KEY_NAME_CURSOR_OFFSET_Y));
            intent.putExtra(KEY_NAME_CURSOR, extras.getParcelable(KEY_NAME_CURSOR));



            try {
                this.startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void goToResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);

    }

}

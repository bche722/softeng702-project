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

public class NextActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            try {
                this.startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    private void goToResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);

    }

}

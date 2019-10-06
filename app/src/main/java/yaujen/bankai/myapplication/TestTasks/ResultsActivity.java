package yaujen.bankai.myapplication.TestTasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import yaujen.bankai.myapplication.AppUtility;
import yaujen.bankai.myapplication.DemoActivity;
import yaujen.bankai.myapplication.R;

public class ResultsActivity extends AppCompatActivity {

    public static final String KEY_NAME_TIME_TAKEN = "TIME_TAKEN";
    public static final String KEY_NAME_ERR_COUNT = "ERR_COUNT";

    private AppUtility singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        singleton = AppUtility.getInstance();


        ((TextView)findViewById(R.id.controlModeView)).setText("Control Mode: "+singleton.getControlMethod());
        ((TextView)findViewById(R.id.clickingMethodView)).setText("Clicking Method: "+singleton.getClickingMethod());
        ((TextView)findViewById(R.id.tiltGainView)).setText("Tilt Gain: "+ singleton.getTiltGain());
        ((TextView)findViewById(R.id.timeView)).setText("Time Taken: "+ (double)singleton.getTimeTaken()/1000+"s");
        ((TextView)findViewById(R.id.errView)).setText("Total Error Count: "+singleton.getTotalErrorCount());
        ((TextView)findViewById(R.id.errViewWIKI)).setText("Wiki Error Count: "+singleton.getErrorCountWIKI());
        ((TextView)findViewById(R.id.errViewRAND)).setText("RandBtn Error Count: "+singleton.getErrorCountRAND());
        ((TextView)findViewById(R.id.errViewNUM)).setText("Numpad Error Count: "+singleton.getErrorCountNUM());
        ((TextView)findViewById(R.id.errViewKEY)).setText("Keyboard Error Count: "+singleton.getErrorCountKEY());
    }

    public void onFinishClicked(View view){
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}

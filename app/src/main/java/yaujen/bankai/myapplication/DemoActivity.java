package yaujen.bankai.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import yaujen.bankai.myapplication.Draw.DrawActivity;
import yaujen.bankai.myapplication.TestTasks.RandomBtnActivity;
import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.Mouse;

public class DemoActivity extends AppCompatActivity {
    public enum Tasks {Keyboard, Numpad, Wikipedia, Draw, RandomBtn, BigImage}


    // Dropdown Options
    public static final String[] CONTROL_METHODS = new String[]{ControlMethod.POSITION_CONTROL.name(), ControlMethod.VELOCITY_CONTROL.name()};
    public static final String[] TILT_GAINS = new String[]{"10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100", "105", "110", "115", "120", "125", "130", "135", "140", "145", "150", "160", "170", "180", "190", "200", "225", "250", "275", "300", "325", "350", "375", "400"};
    public String[] CLICKING_METHODS = new String[]{ClickingMethod.VOLUME_DOWN.name(), ClickingMethod.FLOATING_BUTTON.name(), ClickingMethod.BACK_TAP.name(), ClickingMethod.BEZEL_SWIPE.name()};
    public static final String[] TASKS = new String[]{/*Tasks.Keyboard.name(), Tasks.Numpad.name(), Tasks.Wikipedia.name(), */Tasks.Draw.name(), /*Tasks.RandomBtn.name(), */Tasks.BigImage.name()};

    // KEY 
    public static final String KEY_NAME_CONTROL_METHOD = "CONTROL_METHOD";
    public static final String KEY_NAME_TILT_GAIN = "TILT_GAIN";
    public static final String KEY_NAME_CLICKING_METHOD = "CLICKING_METHOD";
    public static final String KEY_NAME_CURSOR = "CURSOR";
    public static final String KEY_NAME_CURSOR_W = "CURSOR_W";
    public static final String KEY_NAME_CURSOR_H = "CURSOR_H";
    public static final String KEY_NAME_CURSOR_OFFSET_X = "CURSOR_OFFSET_X";
    public static final String KEY_NAME_CURSOR_OFFSET_Y = "CURSOR__OFFSET_Y";

    private HashMap<String, Object[]> cursorMap = new HashMap<>();

    Button experiment;
    Button startButton;
    Switch experimentSwitch;

    private AppUtility singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Accel World");
        setSupportActionBar(toolbar);

        initialiseCursors();
        Set<String> c = cursorMap.keySet();
        String[] CURSORS = c.toArray(new String[cursorMap.size()]);
        singleton = AppUtility.getInstance();


        // Dropdown
        Spinner dropdownControlMethod = findViewById(R.id.control_method);
        Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);
        Spinner dropdownClickingMethod = findViewById(R.id.clicking_method);
        Spinner dropdownTask = findViewById(R.id.task);
        Spinner dropdownCursor = findViewById(R.id.cursor);

        // Adapters to describe how it is displayed
        ArrayAdapter<String> adapterControlMethod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CONTROL_METHODS);
        ArrayAdapter<String> adapterTiltGain = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TILT_GAINS);
        ArrayAdapter<String> adapterClickingMethod = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CLICKING_METHODS);
        ArrayAdapter<String> adapterTask = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TASKS);
        ArrayAdapter<String> adapterCursor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CURSORS);

        //set the spinners adapter to the previously created one.
        dropdownControlMethod.setAdapter(adapterControlMethod);
        dropdownTiltGain.setAdapter(adapterTiltGain);
        dropdownClickingMethod.setAdapter(adapterClickingMethod);
        dropdownTask.setAdapter(adapterTask);
        dropdownCursor.setAdapter(adapterCursor);

        dropdownControlMethod.setOnItemSelectedListener(new ChangeSelectedTiltGainBasedOnControlMethod());
        dropdownClickingMethod.setOnItemSelectedListener(new BacktapWarningMessage());

        experiment = findViewById(R.id.experiment);
        experiment.setVisibility(View.INVISIBLE);

        startButton = findViewById(R.id.button_start);

        experimentSwitch = findViewById(R.id.experimentSwitch);


        experimentSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                experimentSwitcher();
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onStartButton();
            }
        });


        experiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartExperiment();
            }
        });
    }


    /**
     * Toast Message being shown
     *
     * @param message
     */
    public void outputMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Changing recommended tilt gain for different control method
     */
    public class ChangeSelectedTiltGainBasedOnControlMethod implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();

            if (selected != null && !selected.isEmpty()) {
                Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);
                TextView backtapReminder = findViewById(R.id.warning_backtap);

                if (selected.equals(ControlMethod.POSITION_CONTROL.toString())) {
                    dropdownTiltGain.setSelection(5);           // Tilt = 35
                } else if (selected.equals(ControlMethod.VELOCITY_CONTROL.toString())) {
                    dropdownTiltGain.setSelection(18);          // Velocity = 100
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    /**
     * Backtap warning for user
     */
    public class BacktapWarningMessage implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();

            TextView backtapReminder = findViewById(R.id.warning_backtap);
            backtapReminder.setVisibility(View.INVISIBLE);

            if (selected != null && !selected.isEmpty()) {
                if (selected.equals(ClickingMethod.BACK_TAP.name())) {
                    backtapReminder.setVisibility(View.VISIBLE);
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }


    private void initialiseCursors() {
        Drawable drawableArrow = Objects.requireNonNull(ContextCompat.
                getDrawable(getBaseContext(), yaujen.bankai.pointandclick.R.drawable.cursor));

        Bitmap bitmapCursor = ((BitmapDrawable) drawableArrow).getBitmap();
        Object[] cursor1 = {bitmapCursor, bitmapCursor.getWidth() / 2, bitmapCursor.getHeight() / 2, 0, 0};
        cursorMap.put("cursor", cursor1);

        Object[] cursor2 = {bitmapCursor, bitmapCursor.getWidth(), bitmapCursor.getHeight(), 0, 0};
        cursorMap.put("cursor_large", cursor2);

        Drawable drawableCross = Objects.requireNonNull(ContextCompat.
                getDrawable(getBaseContext(), yaujen.bankai.pointandclick.R.drawable.cross));

        Bitmap bitmapCross = ((BitmapDrawable) drawableCross).getBitmap();
        Object[] cross1 = {bitmapCross, 50, 50, 25, 25};
        cursorMap.put("cross", cross1);

        Object[] cross2 = {bitmapCross, 80, 80, 40, 40};
        cursorMap.put("cross_large", cross2);

        Drawable fancyArrow = Objects.requireNonNull(ContextCompat.
                getDrawable(getBaseContext(), yaujen.bankai.pointandclick.R.drawable.seibacur));

        Bitmap bitmapFancy = ((BitmapDrawable) fancyArrow).getBitmap();
        Object[] fancy1 = {bitmapFancy, bitmapFancy.getWidth(), bitmapFancy.getHeight(), 0, 0};
        cursorMap.put("fancy cursor", fancy1);
    }


    private void experimentSwitcher() {
        TextView taskText = findViewById(R.id.text_task);
        Spinner task = findViewById(R.id.task);
        if (experimentSwitch.isChecked()) {


            taskText.setVisibility(View.INVISIBLE);
            task.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.INVISIBLE);
            experiment.setVisibility(View.VISIBLE);
        } else {
            task.setVisibility(View.VISIBLE);
            taskText.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.VISIBLE);
            experiment.setVisibility(View.INVISIBLE);
        }
    }


    private void onStartButton() {
        Spinner dropdownControlMethod = findViewById(R.id.control_method);
        Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);
        Spinner dropdownClickingMethod = findViewById(R.id.clicking_method);
        Spinner dropdownTask = findViewById(R.id.task);
        Spinner dropdownCursor = findViewById(R.id.cursor);

        String controlMethod = dropdownControlMethod.getSelectedItem().toString();
        String tiltGain = dropdownTiltGain.getSelectedItem().toString();
        String clickingMethod = dropdownClickingMethod.getSelectedItem().toString();
        String task = dropdownTask.getSelectedItem().toString();

        String cursorString = dropdownCursor.getSelectedItem().toString();
        Bitmap cursor = (Bitmap) cursorMap.get(cursorString)[0];

        // String message = "You chose: "+controlMethod+", "+tiltGain+", "+clickingMethod+", "+task;
        // DemoActivity.this.outputMessage(message);

        // SWITCHING TO DIFFERENT APP
        Intent myIntent = null;

        if (task.equals(Tasks.Draw.name())) {
            myIntent = new Intent(DemoActivity.this, DrawActivity.class);
        } else if (task.equals(Tasks.RandomBtn.name())) {
            myIntent = new Intent(DemoActivity.this, RandomBtnActivity.class);
        } else if (task.equals(Tasks.BigImage.name())) {
            myIntent = new Intent(DemoActivity.this, BigimageActivity.class);
        }

        if (myIntent != null) {
            myIntent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
            myIntent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
            myIntent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

            myIntent.putExtra(KEY_NAME_CURSOR, (Bitmap) cursorMap.get(cursorString)[0]);
            myIntent.putExtra(KEY_NAME_CURSOR_W, (int) cursorMap.get(cursorString)[1]);
            myIntent.putExtra(KEY_NAME_CURSOR_H, (int) cursorMap.get(cursorString)[2]);
            myIntent.putExtra(KEY_NAME_CURSOR_OFFSET_X, (int) cursorMap.get(cursorString)[3]);
            myIntent.putExtra(KEY_NAME_CURSOR_OFFSET_Y, (int) cursorMap.get(cursorString)[4]);
            try {
                DemoActivity.this.startActivity(myIntent);

            } catch (Exception e) {
                DemoActivity.this.outputMessage(e.getMessage());
            }
        } else {
            DemoActivity.this.outputMessage("Intent is null!");
        }
    }


    private void StartExperiment() {
        Class nextTask = singleton.getTask();
        Intent intent = new Intent(this, nextTask);


        Spinner dropdownControlMethod = findViewById(R.id.control_method);
        Spinner dropdownTiltGain = findViewById(R.id.tilt_gain);
        Spinner dropdownClickingMethod = findViewById(R.id.clicking_method);
        Spinner dropdownCursor = findViewById(R.id.cursor);

        String controlMethod = dropdownControlMethod.getSelectedItem().toString();
        String tiltGain = dropdownTiltGain.getSelectedItem().toString();
        String clickingMethod = dropdownClickingMethod.getSelectedItem().toString();

        String cursorString = dropdownCursor.getSelectedItem().toString();
        Bitmap cursor = (Bitmap) cursorMap.get(cursorString)[0];

        singleton.setExtras(controlMethod, clickingMethod, tiltGain);


        if (intent != null) {
            intent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
            intent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
            intent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

            intent.putExtra(KEY_NAME_CURSOR, (Bitmap) cursorMap.get(cursorString)[0]);
            intent.putExtra(KEY_NAME_CURSOR_W, (int) cursorMap.get(cursorString)[1]);
            intent.putExtra(KEY_NAME_CURSOR_H, (int) cursorMap.get(cursorString)[2]);
            intent.putExtra(KEY_NAME_CURSOR_OFFSET_X, (int) cursorMap.get(cursorString)[3]);
            intent.putExtra(KEY_NAME_CURSOR_OFFSET_Y, (int) cursorMap.get(cursorString)[4]);
            try {
                DemoActivity.this.startActivity(intent);

            } catch (Exception e) {
                DemoActivity.this.outputMessage(e.getMessage());
            }
        } else {
            DemoActivity.this.outputMessage("Intent is null!");
        }
    }

}

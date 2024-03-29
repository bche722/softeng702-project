package yaujen.bankai.myapplication.TestTasks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yaujen.bankai.myapplication.AppUtility;
import yaujen.bankai.myapplication.R;
import yaujen.bankai.myapplication.TestTasks.ResultsActivity;
import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
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
import static yaujen.bankai.myapplication.TestTasks.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.TestTasks.ResultsActivity.KEY_NAME_TIME_TAKEN;
import static yaujen.bankai.pointandclick.Utility.aLog;

public class WikipediaActivity extends MouseActivity {
    private ConstraintLayout constraintLayout;
    private TextView linksLeft;
    private TextView bodyText;

    private Button startButton;

    private List<String> links;
    private long startTime;
    private int correctClicks;
    private int totalClicks;
    private boolean hasStarted;

    private String controlMethod;
    private String clickingMethod;
    private int tiltGain;

    private AppUtility singleton;

    private Intent resultsIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wikipedia);
        constraintLayout = findViewById(R.id.layout);

        singleton = AppUtility.getInstance();

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

        Bitmap mouseBitmap = getIntent().getParcelableExtra(KEY_NAME_CURSOR);
        setupMouse(mouseBitmap, extras.getInt(KEY_NAME_CURSOR_W), extras.getInt(KEY_NAME_CURSOR_H),
                extras.getInt(KEY_NAME_CURSOR_OFFSET_X), extras.getInt(KEY_NAME_CURSOR_OFFSET_Y));


        linksLeft = findViewById(R.id.links);

        // Keeps track of clicks made by the user
        bodyText = findViewById(R.id.bodyText);
        bodyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasStarted && !taskFinished()) {
                    totalClicks++;
                    aLog("Wikipedia", "Clicked");
                }
            }
        });

        createWikipediaText();



        hasStarted = false;
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                Handler timer = new Handler();
                if (!hasStarted) {
                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToNext();
                        }
                    }, singleton.getTestTime());
                    hasStarted = true;
                    startTime = System.currentTimeMillis();
                    updateText();
                } else if (taskFinished() || (System.currentTimeMillis() - startTime) > singleton.getTestTime()){
                    timer.removeCallbacksAndMessages(null);
                    goToNext();
                }
            }
        });
    }

    private void createWikipediaText() {
        // Converts sample wikipedia text file into a string
        InputStream is = getResources().openRawResource(R.raw.wikipedia_text);
        String text = "hello";
        try {
            byte[] buffer = new byte[is.available()];
            while (is.read(buffer) != -1) ;
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Code adapted from:
        // https://stackoverflow.com/questions/12418279/android-textview-with-clickable-links-how-to-capture-clicks/19989677#19989677
        Spanned spanned = Html.fromHtml(text);
        final Spannable spannable = new SpannableStringBuilder(spanned);

        links = new ArrayList<>();
        URLSpan[] urlSpans = spanned.getSpans(0, spanned.length(), URLSpan.class);

        // Adds a click listener to every url
        for (final URLSpan urlSpan : urlSpans) {
            final String url = urlSpan.getURL();

            // Only add links we want to the task
            if (!url.equals("-1")) {
                links.add(url);
            }


            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (hasStarted && links.contains(url)) {
                        links.remove(url);
                        correctClicks++;
                        updateText();
                    }
                    aLog("Wikipedia", "Clicked " + urlSpan.getURL());
                }
            };

            spannable.setSpan(clickableSpan,
                    spannable.getSpanStart(urlSpan),
                    spannable.getSpanEnd(urlSpan),
                    spannable.getSpanFlags(urlSpan));
            spannable.removeSpan(urlSpan);
        }

        bodyText.setText(spannable);
        bodyText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void updateText() {
        // Update list of links to click
        if (!taskFinished()) {
            Iterator<String> iterator = links.iterator();
            String linksRemaining = "<b>Click these links:</b><br>";
            while (iterator.hasNext()) {
                linksRemaining += iterator.next() + "<br>";
            }
            linksLeft.setText(Html.fromHtml(linksRemaining));
        }

        // Handle when task is finished
        else {
            totalClicks++; // increment because last link clicked doesn't increment total clicks
            long timeTaken = System.currentTimeMillis() - startTime;

            linksLeft.setText((Html.fromHtml("<b>Done!</b>")));
            startButton.setVisibility(View.VISIBLE);
            startButton.setText("Continue");

            resultsIntent = new Intent(this, ResultsActivity.class);
            resultsIntent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
            resultsIntent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
            resultsIntent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

            resultsIntent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken) / 1000 + "s");
            resultsIntent.putExtra(KEY_NAME_ERR_COUNT, totalClicks - correctClicks);
        }
    }

    private void goToNext() {

        long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("testexp", "WIKI: "+timeTaken);
        singleton.setErrorCountWIKI(totalClicks-correctClicks);
        singleton.incTimeTaken(timeTaken);
        Intent intent = new Intent(this, NextActivity.class);
        extras = packExtras();
        intent.putExtra("BUNDLE", extras);
        startActivity(intent);
    }

    private void goToResults() {
        aLog("Wikipedia", "Task finished: " + correctClicks + "/" + totalClicks);
        startActivity(resultsIntent);
    }

    private boolean taskFinished() {
        return links.size() == 0;
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
}

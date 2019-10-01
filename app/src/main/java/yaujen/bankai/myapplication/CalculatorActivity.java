package yaujen.bankai.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;
import yaujen.bankai.pointandclick.Utility;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_ERR_COUNT;
import static yaujen.bankai.myapplication.ResultsActivity.KEY_NAME_TIME_TAKEN;

public class CalculatorActivity extends MouseActivity {

    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, badd, bsub, bmul, bdiv, bdot, bequal;
    TextView ans;
    double var1, var2;
    boolean add, sub, mul, div;

    private ConstraintLayout constraintLayout;

    private String controlMethod;
    private String clickingMethod;
    private int tiltGain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        constraintLayout = findViewById(R.id.layout);
        //numberField = findViewById(R.id.numField);



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



        b1 = (Button) findViewById(R.id.btn1);
        b2 = (Button) findViewById(R.id.btn2);
        b3 = (Button) findViewById(R.id.btn3);
        b4 = (Button) findViewById(R.id.btn4);
        b5 = (Button) findViewById(R.id.btn5);
        b6 = (Button) findViewById(R.id.btn6);
        b7 = (Button) findViewById(R.id.btn7);
        b8 = (Button) findViewById(R.id.btn8);
        b9 = (Button) findViewById(R.id.btn9);
        b0 = (Button) findViewById(R.id.btn0);
        badd = (Button) findViewById(R.id.btnadd);
        bsub = (Button) findViewById(R.id.btnsub);
        bdiv = (Button) findViewById(R.id.btndiv);
        bmul = (Button) findViewById(R.id.btnmul);
        bdot = (Button) findViewById(R.id.btndot);
        bequal = (Button) findViewById(R.id.btnequal);

        ans = (TextView) findViewById(R.id.Answer);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "0");
            }
        });
        bdot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + ".");
            }
        });
        badd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ans.setText(ans.getText() + "");
                var1 = Double.parseDouble(ans.getText() + "");
                add = true;
                ans.setText(ans.getText() + " + ");
            }
        });
        bsub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                var1 = Double.parseDouble(ans.getText() + "");
                sub = true;
                ans.setText(ans.getText() + " - ");
            }
        });
        bmul.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                var1 = Double.parseDouble(ans.getText() + "");
                mul = true;
                ans.setText(ans.getText() + " * ");
            }
        });
        bdiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                var1 = Double.parseDouble(ans.getText() + "");
                div = true;
                ans.setText(ans.getText() + " / ");
            }
        });
        bequal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s = ans.getText() + "";
                String[] s1 = s.split(" ");


                var2 = Double.parseDouble(s1[2]);
                if (add == true) {
                    ans.setText(var1 + var2 + "");
                    add = false;
                }
                if (sub == true) {
                    ans.setText(var1 - var2 + "");
                    sub = false;
                }
                if (mul == true) {
                    ans.setText(var1 * var2 + "");
                    mul = false;
                }
                if (div == true) {
                    ans.setText(var1 / var2 + "");
                    div = false;
                }
            }
        });

    }
}










/*
        // How to add fab clicking
        buttonClicker = new MovableFloatingActionButton(this);
        constraintLayout.addView(buttonClicker, constraintLayout.getChildCount(),getFabConstraintLayoutParams(100,0));
        setMovableFloatingActionButton(buttonClicker);

        // Set mouse view configuration
        Bundle extras = getIntent().getExtras();
        controlMethod  = extras.getString(KEY_NAME_CONTROL_METHOD);
        clickingMethod = extras.getString(KEY_NAME_CLICKING_METHOD);
        tiltGain = Integer.parseInt(extras.getString(KEY_NAME_TILT_GAIN));

        setClickingMethod(ClickingMethod.valueOf(clickingMethod));
        setControlMethod(ControlMethod.valueOf(controlMethod));
        setTiltGain(tiltGain);

    }}


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

    public void onStartClicku(View view){
        if(start){
            start = false;
            numberField.setText(" • • • • • • • • • •");
            findViewById(R.id.goBtn).setVisibility(View.INVISIBLE);
            startTime = System.currentTimeMillis();
        } else {
            incrementErrorCount();
        }
    }

    public void onFinishClicku(View view){
        if(finish){
            long timeTaken = System.currentTimeMillis() - startTime;
            Utility.aLog("Time taken",timeTaken+"");

            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra(KEY_NAME_CONTROL_METHOD, controlMethod);
            intent.putExtra(KEY_NAME_TILT_GAIN, tiltGain);
            intent.putExtra(KEY_NAME_CLICKING_METHOD, clickingMethod);

            intent.putExtra(KEY_NAME_TIME_TAKEN, ((double) timeTaken)/1000 + "s");
            intent.putExtra(KEY_NAME_ERR_COUNT, errorCount);

            startActivity(intent);
        } else {
            incrementErrorCount();
        }
    }

    public void onClicku(View view){
        if(!start && !finish){
            if(!numberToEnter.equals("")){
                char clickuChar = ((Button)view).getText().charAt(0);
                Utility.aLog("Key clicked",clickuChar+"");
                if(numberToEnter.charAt(0) == clickuChar){
                    if(numberToEnter.length() != 1){
                        numberField.setText(numberField.getText().toString().replaceFirst(" •",clickuChar+""));
                        numberToEnter = numberToEnter.substring(1);
                    } else {
                        numberField.setText(numberField.getText().toString().replaceFirst(" •",clickuChar+""));
                        finish = true;
                        findViewById(R.id.endBtn).setVisibility(View.VISIBLE);
                    }
                } else {
                    incrementErrorCount();
                }
            }
        } else if (finish){
            incrementErrorCount();
        }
    }

    public void onBadClicku(View view){
        incrementErrorCount();
    }

    private void incrementErrorCount(){
        if(!start){
            errorCount++;
            Utility.aLog("Err count",errorCount+"");
        }
    }
}*/


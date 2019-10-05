package yaujen.bankai.myapplication;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;

public class CalculatorActivity extends MouseActivity {

    Button badd, bsub, bmul, bdiv, bequal;
    TextView input, output;
    double var1, var2;
    boolean add, sub, mul, div;
    boolean isNum = false;


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


        badd = (Button) findViewById(R.id.btnadd);
        bsub = (Button) findViewById(R.id.btnsub);
        bdiv = (Button) findViewById(R.id.btndiv);
        bmul = (Button) findViewById(R.id.btnmul);
        bequal = (Button) findViewById(R.id.btnequal);

        input = (TextView) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);

        badd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean prev = isNum;
                try {
                    if (isNum) {
                        isNum = false;
                        input.setText(input.getText() + "");
                        var1 = Double.parseDouble(input.getText() + "");
                        add = true;
                        input.setText(input.getText() + " + ");
                    }
                } catch (NumberFormatException e) {
                    isNum = prev;
                    e.printStackTrace();
                }
            }
        });
        bsub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean prev = isNum;

                try {
                    if (isNum) {
                        isNum = false;
                        var1 = Double.parseDouble(input.getText() + "");
                        sub = true;
                        input.setText(input.getText() + " - ");
                    }
                } catch (NumberFormatException e) {
                    isNum = prev;
                    e.printStackTrace();
                }
            }
        });
        bmul.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean prev = isNum;
                try {
                    if (isNum) {
                        isNum = false;
                        var1 = Double.parseDouble(input.getText() + "");
                        mul = true;
                        input.setText(input.getText() + " * ");
                    }
                } catch (NumberFormatException e) {
                    isNum = prev;
                    e.printStackTrace();
                }
            }
        });
        bdiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean prev = isNum;
                try {
                    if (isNum) {
                        isNum = false;
                        var1 = Double.parseDouble(input.getText() + "");
                        div = true;
                        input.setText(input.getText() + " / ");
                    }
                } catch (NumberFormatException e) {
                    isNum = prev;
                    e.printStackTrace();
                }
            }
        });
        bequal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isNum) {
                    isNum = false;
                    String s = input.getText() + "";
                    String[] s1 = s.split(" ");

                    input.setText("");

                    if (add || sub || mul || div) {
                        var2 = Double.parseDouble(s1[2]);
                        if (add == true) {
                            output.setText(var1 + var2 + "");
                            add = false;
                        } else if (sub == true) {
                            output.setText(var1 - var2 + "");
                            sub = false;
                        } else if (mul == true) {
                            output.setText(var1 * var2 + "");
                            mul = false;
                        } else if (div == true) {
                            output.setText(var1 / var2 + "");
                            div = false;
                        }
                    } else {
                        var1 = Double.parseDouble(s1[0]);
                        output.setText(var1 + "");
                    }
                }
            }
        });

    }

    public void onClickNum(View btn) {
        isNum = true;

        CharSequence btnText = ((Button)btn).getText();
        input.setText(input.getText() + btnText.toString());
    }
    public void onClickPt(View btn) {
        if (isNum) {
            CharSequence btnText = ((Button) btn).getText();
            input.setText(input.getText() + btnText.toString());
            isNum = false;
        }

    }

}



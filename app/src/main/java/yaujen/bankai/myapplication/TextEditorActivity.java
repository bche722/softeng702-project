package yaujen.bankai.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.util.ArrayList;

import yaujen.bankai.pointandclick.ClickingMethod;
import yaujen.bankai.pointandclick.ControlMethod;
import yaujen.bankai.pointandclick.MouseActivity;
import yaujen.bankai.pointandclick.MovableFloatingActionButton;

import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CLICKING_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_CONTROL_METHOD;
import static yaujen.bankai.myapplication.DemoActivity.KEY_NAME_TILT_GAIN;
import static yaujen.bankai.pointandclick.Utility.aLog;

public class TextEditorActivity extends MouseActivity {

    private TextEditorActivity thisActivity;
    private ConstraintLayout constraintLayout;
    private TextView text;
    private Button save;
    private Button newDocument;
    private Button deleteAll;
    private Button open;

    private String controlMethod;
    private String clickingMethod;
    private int tiltGain;
    private int fileCount;
    private String currentFile;

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_texteditor );

        text = findViewById ( R.id.text );
        constraintLayout = findViewById ( R.id.layout );
        thisActivity = this;
        // How to add fab clicking
        buttonClicker = new MovableFloatingActionButton ( this );
        constraintLayout.addView ( buttonClicker, constraintLayout.getChildCount ( ), getFabConstraintLayoutParams ( 100, 0 ) );
        setMovableFloatingActionButton ( buttonClicker );

        // Set mouse view configuration
        Bundle extras = getIntent ( ).getExtras ( );
        controlMethod = extras.getString ( KEY_NAME_CONTROL_METHOD );
        clickingMethod = extras.getString ( KEY_NAME_CLICKING_METHOD );
        tiltGain = Integer.parseInt ( extras.getString ( KEY_NAME_TILT_GAIN ) );

        setClickingMethod ( ClickingMethod.valueOf ( clickingMethod ) );
        setControlMethod ( ControlMethod.valueOf ( controlMethod ) );
        setTiltGain ( tiltGain );

        aLog ( "Wikipedia", controlMethod );
        aLog ( "Wikipedia", clickingMethod );
        aLog ( "Wikipedia", tiltGain + "" );

        save = findViewById ( R.id.save );
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile ();
            }
        });

        newDocument = findViewById ( R.id.newDocument );
        newDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFile = null;
                AlertDialog ad = new AlertDialog.Builder(thisActivity)
                        .setMessage("Do you want to save this file?")
                        .setTitle("")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int whichButton) {
                                saveFile ();
                                text.setText ( "" );
                            }
                        })
                        .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                text.setText ( "" );
                            }
                        })
                        .show();
            }
        });

        deleteAll = findViewById ( R.id.deleteAll );
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFile = null;
                AlertDialog ad = new AlertDialog.Builder(thisActivity)
                        .setMessage("Do you want to delete all files?")
                        .setTitle("")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int whichButton) {
                                for(int i = 1; i <= fileCount ; i++){
                                    deleteFile ( i+".txt" );
                                }
                                fileCount = 0;
                                currentFile = null;

                            }
                        })
                        .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
        });

        open = findViewById ( R.id.open );
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFile = null;
                AlertDialog ad = new AlertDialog.Builder(thisActivity)
                        .setMessage("Do you want to save this file?")
                        .setTitle("")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int whichButton) {
                                saveFile ();
                                AlertdialogWithSpinner();
                            }
                        })
                        .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                AlertdialogWithSpinner();
                            }
                        })
                        .show();
            }
        });

        try{
            fileCount = Integer.parseInt ( loadText ( "count.txt" ) );
        }catch(Exception e){
            fileCount = 0;
        }
    }

    @Override
    protected void onDestroy ( ) {
        super.onDestroy ( );
        aLog ( "event", "Destroy" );
    }

    //pausing the mouse view when activity is paused
    @Override
    protected void onPause ( ) {
        super.onPause ( );
        aLog ( "event", "Pause" );
    }

    //running the mouse view when activity is resumed
    @Override
    protected void onResume ( ) {
        super.onResume ( );
        aLog ( "Exception", "Resume" );
    }

    public void onClicku ( View view ) {
        String letter = (String) ((Button) view).getText ( );
        String context = text.getText ( ).toString ( );
        text.setText ( context + letter );
    }

    public void onBackClicku ( View v ) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            aLog ( "back", "back" );
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setMessage("Do you want to save changes?")
                    .setTitle("")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int whichButton) {
                            saveText ( text.getText ().toString () ,fileCount+".txt");
                            finish();
                        }
                    })
                    .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void saveFile(){
        if(currentFile == null){
            fileCount += 1;
            currentFile = fileCount+".txt";
            saveText ( text.getText ().toString () ,currentFile);
            saveText ( fileCount+"", "count.txt" );
        }else{
            saveText ( text.getText ().toString () ,currentFile);
        }
    }

    public void saveText(String content, String path){
        try {
            FileOutputStream fos = openFileOutput(path, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(content);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public String loadText(String path) throws Exception{
        String content = "";
            FileInputStream fis = openFileInput(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bf = new BufferedReader(isr);
            String line;
            while ((line = bf.readLine()) != null) {
                content += line;
            }
            isr.close ();
            return content;
    }

    private void AlertdialogWithSpinner() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Which file do you want to open");
        title.setPadding(10, 10, 10, 10);
        title.setGravity( Gravity.CENTER);
        title.setTextSize(30);
        builder.setCustomTitle(title);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View spinner_view = inflater.inflate(R.layout.spinner,null);
        Spinner spinner = (Spinner) spinner_view.findViewById(R.id.spinner);

        builder.setView(spinner_view);
        builder.setCancelable(true);
        builder.show();


        ArrayList <String> list = new ArrayList<String>();
        for(int i = 1; i <= fileCount; i++){
            list.add ( i+".txt" );
        }

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView <?> parent, View view, int position, long id) {
                try {
                    text.setText ( loadText ( parent.getItemAtPosition ( position ).toString ( ) ) );
                }catch (Exception e){
                    e.printStackTrace ();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

package yaujen.bankai.myapplication;

import java.util.ArrayList;
import java.util.Collections;

import yaujen.bankai.myapplication.TestTasks.KeyboardActivity;
import yaujen.bankai.myapplication.TestTasks.NumpadActivity;
import yaujen.bankai.myapplication.TestTasks.RandomBtnActivity;
import yaujen.bankai.myapplication.TestTasks.WikipediaActivity;

public class AppUtility {

    private static AppUtility _singleton = null; // Singleton Instance

    private final static int TEST_TIME = 120000;


    private String controlMethod;
    private String tiltGain;
    private String clickingMethod;



    long timeTaken;
    int errorCountKEY;
    int errorCountWIKI;
    int errorCountRAND;
    int errorCountNUM;



    private ArrayList<Class> tasks;



    private AppUtility() {
        tasks = new ArrayList<>();
        tasks.add(KeyboardActivity.class);
        tasks.add(NumpadActivity.class);
        tasks.add(WikipediaActivity.class);
        tasks.add(RandomBtnActivity.class);
        Collections.shuffle(tasks);

        timeTaken = 0;
        errorCountNUM = 0;
        errorCountWIKI = 0;
        errorCountRAND = 0;
        errorCountKEY = 0;

    }

    public static AppUtility getInstance() {
        if (_singleton == null) {
            _singleton = new AppUtility();
        }
        return _singleton;
    }


    public int getTestTime() {
        return TEST_TIME;
    }

    public Class getTask() {
        if (tasks.size() > 0) {
            return tasks.remove(0);
        }
        return null;
    }

    public void setExtras(String ctrlM, String clikM, String tiltG) {
        controlMethod = ctrlM;
        clickingMethod = clikM;
        tiltGain = tiltG;
    }

    public String getControlMethod() {
        return controlMethod;
    }

    public String getTiltGain() {
        return tiltGain;
    }

    public String getClickingMethod() {
        return clickingMethod;
    }


    public long getTimeTaken() {
        return timeTaken;
    }

    public void incTimeTaken(long timeTaken) {
        this.timeTaken += timeTaken;
    }

    public int getErrorCountKEY() {
        return errorCountKEY;
    }

    public void setErrorCountKEY(int errorCountKEY) {
        this.errorCountKEY = errorCountKEY;
    }

    public int getErrorCountWIKI() {
        return errorCountWIKI;
    }

    public void setErrorCountWIKI(int errorCountWIKI) {
        this.errorCountWIKI = errorCountWIKI;
    }

    public int getErrorCountRAND() {
        return errorCountRAND;
    }

    public void setErrorCountRAND(int errorCountRAND) {
        this.errorCountRAND = errorCountRAND;
    }

    public int getErrorCountNUM() {
        return errorCountNUM;
    }

    public void setErrorCountNUM(int errorCountNUM) {
        this.errorCountNUM = errorCountNUM;
    }

    public int getTotalErrorCount() {
        return errorCountKEY+errorCountRAND+errorCountWIKI+errorCountNUM;
    }

}

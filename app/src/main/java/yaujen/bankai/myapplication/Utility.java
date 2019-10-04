package yaujen.bankai.myapplication;

import java.util.ArrayList;
import java.util.Collections;

import yaujen.bankai.myapplication.TestTasks.KeyboardActivity;
import yaujen.bankai.myapplication.TestTasks.NumpadActivity;
import yaujen.bankai.myapplication.TestTasks.WikipediaActivity;

public class Utility {

    private static Utility _singleton = null; // Singleton Instance

    private final static int PLAY_TIME = 300000;
    private final static int TEST_TIME = 120000;


    private String controlMethod;
    private String tiltGain;
    private String clickingMethod;



    private ArrayList<Class> tasks;



    private Utility() {
        tasks = new ArrayList<>();
        tasks.add(KeyboardActivity.class);
        tasks.add(NumpadActivity.class);
        tasks.add(WikipediaActivity.class);
        Collections.shuffle(tasks);
    }

    public static Utility getInstance() {
        if (_singleton == null) {
            _singleton = new Utility();
        }
        return _singleton;
    }


    public static int getPlayTime() {
        return PLAY_TIME;
    }

    public static int getTestTime() {
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
}

package yaujen.bankai.pointandclick;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler extends Handler {
    private Clicker clicker;
    private String TAG = "TAG";

    public MessageHandler(Clicker clicker) {
        this.clicker = clicker;
    }

    @Override
    public void handleMessage(Message message) {
        JSONObject info = null;

        try {
            info = new JSONObject(message.getData().getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int tap = 0;
        try {
            tap = info.getInt("tap");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (tap) {
            case 0:

                //Log.d(TAG, "No BTAP !");
// No BTap action.
                break;
            case 1:
                Log.d(TAG, " BTAP_SINGLE !");
                clicker.click();
                clicker.release();
// Single BTap action.
                break;
            case 2:
                Log.d(TAG, " BTAP_DOUBLE !");
                clicker.click();
// Dobule BTap action.
                break;
            default:
                Log.e(TAG, " BTAP not recognised !");
                break;
        }


    }
}

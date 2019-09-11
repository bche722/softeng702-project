package yaujen.bankai.pointandclick;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler extends Handler {
    private MouseActivity mouseActivity;

    public MessageHandler(MouseActivity mouseActivity) {
        this.mouseActivity = mouseActivity;
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
                //Log.d("testTap", "No BTAP !");
                // No BTap action.
                break;
            case 1:
                Log.d("testTap", " BTAP_SINGLE !");
                if (!mouseActivity.getKeyDown()) {
                    mouseActivity.simulateTouchDown();
                }
                mouseActivity.simulateTouchUp();
                break;
            case 2:
                Log.d("testTap", " BTAP_DOUBLE !");
                mouseActivity.simulateTouchDown();
                break;
            default:
                Log.e("testTap", " BTAP not recognised !");
                break;
        }
    }
}

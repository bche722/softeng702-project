package yaujen.bankai.pointandclick;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

public class Utility {

    public static void aLog(String tag, String msg){
        Log.println(Log.ASSERT,tag,msg);
    }

    public static String dF2(double dou){
        DecimalFormat df2 = new DecimalFormat(".##");
        return df2.format(dou);
    }

    public static View findChildByPosition(ViewGroup parent, float x, float y) {
        int count = parent.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);
            //Log.d("testb",child.toString());

            if (child.getVisibility() == View.VISIBLE && !(child instanceof MouseView)) {
                if (child instanceof ViewGroup && ((ViewGroup) child).getChildCount()>0){
                    View returnedView = findChildByPosition((ViewGroup) child,x,y);
                    if (returnedView != null) {
                        Log.d("testb", returnedView.toString());

                        return returnedView;
                    }
                }else {
                    if (isPositionInChildView(parent, child, x, y)) {
                        Log.d("testb",child.toString());
                        return child;
                    }
                }
            }
        }

        return null;
    }

    private static boolean isPositionInChildView(ViewGroup parent, View child, float x, float y) {
        sPoint[0] = x + parent.getScrollX() - child.getLeft();
        sPoint[1] = y + parent.getScrollY() - child.getTop();

        Matrix childMatrix = child.getMatrix();
        if (!childMatrix.isIdentity()) {
            childMatrix.invert(sInvMatrix);
            sInvMatrix.mapPoints(sPoint);
        }

        x = sPoint[0];
        y = sPoint[1];

        return x >= 0 && y >= 0 && x < child.getWidth() && y < child.getHeight();
    }

    private static Matrix sInvMatrix = new Matrix();
    private static float[] sPoint = new float[2];


}

package yaujen.bankai.pointandclick;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Mouse object to keep track of and update the coordinates of the on screen pointer
 */
public class Mouse {
    private Drawable _icon;



    //coordinates
    private int _x;
    private int _y;
    private CustomizedQueue xQueue;
    private CustomizedQueue yQueue;
    private int width;
    private int height;
    private int xDims;
    private int yDims;

    public Mouse(Drawable icon, int initialX, int initialY, int width, int height){
        _icon = icon;
        _x = initialX;
        _y = initialY;
        xDims = initialX*2;
        yDims = initialY*2;
        this.width = width;
        this.height = height;
        xQueue = new CustomizedQueue ( 100 );
        yQueue = new CustomizedQueue ( 100 );
    }

    public int get_x() {
        return _x;
    }

    public int get_y() {
        return _y;
    }

    public int getAverageX(){
        return xQueue.getAverage ();
    }

    public int getAverageY(){
        return yQueue.getAverage ();
    }



    public void displace(int x, int y) {
        x += _x;
        y += _y;
        updateLocation(x,y);
    }



    public void updateLocation(int x, int y) {

        if (x < 0) {
            x = 0;
        } else if (x > xDims-10) {
            x = xDims-10;
        }
        if (y < 0) {
            y = 0;
        } else if (y > yDims-10) {
            y = yDims-10;
        }

        Log.d("testMouse", "x coord: " + x + " y coord: " + y);

        _x = x;
        _y = y;

        xQueue.add ( x );
        yQueue.add ( y );

        Rect bounds = _icon.copyBounds();
        bounds.left = x;
        bounds.top = y;
        bounds.right = x + width;
        bounds.bottom = y + height;
        _icon.setBounds(bounds);
        _icon.invalidateSelf();
    }

    public Drawable getDrawable() {
        return _icon;
    }



}

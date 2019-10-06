package yaujen.bankai.pointandclick;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Mouse object to keep track of and update the coordinates of the on screen pointer
 */
public class Mouse implements Serializable {
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
    private int offsetX;
    private int offsetY;

    private Bitmap mouseBitmap;

    public Mouse(Drawable icon, int initialX, int initialY, int width, int height, int offsetx, int offsety) {
        _icon = icon;
        _x = initialX;
        _y = initialY;
        xDims = initialX * 2;
        yDims = initialY * 2;
        this.width = width;
        this.height = height;
        offsetX = offsetx;
        offsetY = offsety;

        xQueue = new CustomizedQueue ( 100 );
        yQueue = new CustomizedQueue ( 100 );
    }

//    public Mouse(Drawable icon, int initialX, int initialY, int width, int height, int offsetX, int offsetY){
//        _icon = icon;
//        _x = initialX + offsetX;
//        _y = initialY+ offsetY;
//        xDims = initialX*2;
//        yDims = initialY*2;
//        this.width = width;
//        this.height = height;
//    }

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
        updateLocation(x, y);
    }


    public void updateLocation(int x, int y) {

        if (x < 0) {
            x = 0;
        } else if (x > xDims - 10) {
            x = xDims - 10;
        }
        if (y < 0) {
            y = 0;
        } else if (y > yDims - 10) {
            y = yDims - 10;
        }

//        Log.d("testMouse", "x coord: " + x + " y coord: " + y);

        _x = x;
        _y = y;

//        Log.d("testMouse", "_x coord: " + _x + " _y coord: " + _y);

        xQueue.add ( x );
        yQueue.add ( y );

        Rect bounds = _icon.copyBounds();
        bounds.left = x - offsetX;
        bounds.top = y - offsetY;
        bounds.right = x + width - offsetX;
        bounds.bottom = y + height - offsetY;
        _icon.setBounds(bounds);
        _icon.invalidateSelf();
    }

    public Drawable getDrawable() {
        return _icon;
    }

    public void setIcon(Drawable _icon) {
        this._icon = _icon;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public Bitmap getMouseBitmap() {
        return mouseBitmap;
    }

}

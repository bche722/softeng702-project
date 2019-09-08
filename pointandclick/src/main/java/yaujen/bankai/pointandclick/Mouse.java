package yaujen.bankai.pointandclick;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Mouse object to keep track of and update the coordinates of the on screen pointer
 */
public class Mouse {
    private Drawable _icon;



    //coordinates
    private int _x;
    private int _y;
    private int width;
    private int height;

    public Mouse(Drawable icon, int initialX, int initialY, int width, int height){
        _icon = icon;
        _x = initialX;
        _y = initialY;
        this.width = width;
        this.height = height;
    }

    public int get_x() {
        return _x;
    }

    public int get_y() {
        return _y;
    }


    public void displace(int xVal, int yVal){
        updateLocation(_x+xVal,_y+yVal);
    }

    private void updateLocation(int x, int y) {
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

package com.divanstudio.spaceTrouble;

import android.graphics.Canvas;

/**
 * Created by aaivanov on 04/19/17.
 */

public abstract class Control {
    protected int canvX;  //convas coords
    protected int canvY;  //convas coords

    protected int width;
    protected int height;

    public Function callBack;

    public Control () {}

    public boolean isCollision(float touchEventX, float touchEventY) {
        boolean isCollision = touchEventX > canvX
                && touchEventX < canvX + width
                && touchEventY > canvY
                && touchEventY < canvY + height;
        return isCollision ;
    }
    protected abstract void onDraw(Canvas canvas);
}

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

    protected Function callBack;

    protected String existedState;

    protected State state = State.getInstance();

    public Control () {}

    public boolean isCollision(float touchEventX, float touchEventY) {
        if ( state.getState() != existedState && existedState != null ) return false;
        boolean isCollision = touchEventX > canvX
                && touchEventX < canvX + width
                && touchEventY > canvY
                && touchEventY < canvY + height;
        return isCollision ;
    }
    protected abstract void onDraw(Canvas canvas);

    public void runCallBack () {
        if ( callBack != null ) { callBack.run(null); }
    }

}

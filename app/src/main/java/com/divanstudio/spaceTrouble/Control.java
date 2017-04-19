package com.divanstudio.spaceTrouble;

/**
 * Created by aaivanov on 04/19/17.
 */

public class Control {
    protected int canvX;  //convas coords
    protected int canvY;  //convas coords

    protected int width;
    protected int height;

    public Function callBack;

    public Control () {
        this.width = 0;
        this.height = 0;
        this.canvX = 0;
        this.canvY = 0;
    }

    public Control( int canvX, int canvY, int width, int height ) {
        this.width = width;
        this.height = height;
        this.canvX = canvX;
        this.canvY = canvY;
    }

    public boolean isCollision(float touchEventX, float touchEventY) {
        boolean isCollision = touchEventX > canvX
                && touchEventX < canvX + width
                && touchEventY > canvY
                && touchEventY < canvY + height;
        return isCollision ;
    }
}

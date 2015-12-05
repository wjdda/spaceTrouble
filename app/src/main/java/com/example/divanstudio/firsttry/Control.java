package com.example.divanstudio.firsttry;

import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by aaivanov on 11/29/15.
 */

public class Control  {
    private static final int BMP_ROWS = 1;
    private static final int BMP_COLUMNS = 4;

    private int frameCount = 0;

    private mainView gameView;
    private Bitmap control;

    private int canvX;  //convas coords
    private int canvY;  //convas coords

    private int width;
    private int height;

    public Control(mainView gameView, Bitmap control, int frameCount, int canvX, int canvY) {
        this.gameView = gameView;
        this.control = control;
        this.frameCount = frameCount;
        this.width = control.getWidth() / BMP_COLUMNS;
        this.height = control.getHeight() / BMP_ROWS;
        this.canvX = canvX;
        this.canvY = canvY;
    }

    public void onDraw(Canvas canvas) {


        Rect src = new Rect(width * frameCount, 0, width * ( frameCount + 1), height); //part of src bitmap
        Rect dst = new Rect(canvX, canvY, canvX + width, canvY + height); // screen area
        canvas.drawBitmap(control, src, dst, null);
    }

    public boolean isCollision(float touchEventX, float touchEventY) {
        boolean isCollision = touchEventX > canvX
                           && touchEventX < canvX + width
                           && touchEventY > canvY
                           && touchEventY < canvY + height;
        return isCollision ;
    }
}

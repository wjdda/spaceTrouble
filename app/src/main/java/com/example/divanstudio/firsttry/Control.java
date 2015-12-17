package com.example.divanstudio.firsttry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by aaivanov on 11/29/15.
 */

public class Control {
    private static final int BMP_ROWS = 1;
    private static final int BMP_COLUMNS = 4;

    private int frameCount = 0;

    private Bitmap control;
    private Paint paint;
    private String text;

    private int canvX;  //convas coords
    private int canvY;  //convas coords

    private int width;
    private int height;

    public Control(mainView gameView, Bitmap control, int frameCount, int canvX, int canvY) {
        this.control = control;
        this.frameCount = frameCount;
        this.width = control.getWidth() / BMP_COLUMNS;
        this.height = control.getHeight() / BMP_ROWS;
        this.canvX = canvX;
        this.canvY = canvY + gameView.getHeight() - height * 5;
    }

    public Control (int width, int height, int canvX, int canvY, String text) {
        this.width = width;
        this.height = height;
        this.canvX = canvX;
        this.canvY = canvY;

        this.paint = new Paint();
        this.text = text;
        this.control = null;

        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(0);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public void onDraw(Canvas canvas) {
        Rect src = new Rect(width * frameCount, 0, width * ( frameCount + 1), height); //part of src bitmap
        Rect dst = new Rect(canvX, canvY, canvX + width, canvY + height); // screen area
        if (control !=  null) {
            canvas.drawBitmap(control, src, dst, null);
        } else {
            canvas.drawRect(canvX, canvY, canvX + width, canvY + height, paint);
            canvas.drawText(text, canvX+width/2, canvY+height/2, paint);
        }
    }

    public boolean isCollision(float touchEventX, float touchEventY) {
        boolean isCollision = touchEventX > canvX
                           && touchEventX < canvX + width
                           && touchEventY > canvY
                           && touchEventY < canvY + height;
        return isCollision ;
    }
}

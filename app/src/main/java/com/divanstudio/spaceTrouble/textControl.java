package com.divanstudio.spaceTrouble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by aaivanov on 19.04.2017.
 */
public class textControl extends Control {
    private Paint paint;

    private String text;

    public textControl (int width, int height, int canvX, int canvY, String text, Function callBack, String existedState) {
        this.width = width;
        this.height = height;
        this.canvX = canvX;
        this.canvY = canvY;
        this.callBack = callBack;
        this.existedState = existedState;

        this.paint = new Paint();
        this.text = text;

        this.paint.setColor(Color.RED);
        this.paint.setStrokeWidth(0);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public textControl (int width, int height, int canvX, int canvY, String text, Function callBack) {
        this(width, height, canvX, canvY, text, callBack, null);
    }

    public textControl (int width, int height, int canvX, int canvY, String text) {
        this (width, height, canvX, canvY, text, null);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if ( state.getState() == existedState || existedState == null ) {
            canvas.drawRect(canvX, canvY, canvX + width, canvY + height, paint);
            canvas.drawText(text, canvX + width / 2, canvY + height / 2, paint);
        }
    }
}

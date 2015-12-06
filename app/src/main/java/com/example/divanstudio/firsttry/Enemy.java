package com.example.divanstudio.firsttry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by aaivanov on 12/5/15.
 */
public class Enemy {
    /**Рядков в спрайте = 4*/
    private static final int BMP_ROWS = 4;
    /**Колонок в спрайте = 3*/
    private static final int BMP_COLUMNS = 1;

    private int frameCount = 0;

    private mainView gameView;
    private Bitmap control;
    private Player player;

    private int xSpeed = 0;
    private int ySpeed = 0;

    private int canvX;  //convas coords
    private int canvY;  //convas coords

    private int width;
    private int height;

    private int renderWidth;
    private int renderHeight;

    private Random rnd;

    public Enemy (mainView gameView, Bitmap control, int frameCount, Player player) {
        this.gameView = gameView;
        this.control = control;
        this.frameCount = frameCount;
        this.width = control.getWidth() / BMP_COLUMNS;
        this.height = control.getHeight() / BMP_ROWS;
        this.rnd = new Random();
        this.player = player;
        this.renderWidth = this.width/2;
        this.renderHeight = this.height/2;

        this.canvX = -this.renderWidth;
    }

        /**Перемещение объекта, его направление*/
        private void update()
        {
            if (canvX <= -renderWidth) {
                canvX = gameView.getWidth();
                canvY = rnd.nextInt(gameView.getHeight() - renderHeight);
                xSpeed = -(rnd.nextInt(5) + 4);
                int someCrop = (rnd.nextInt(3)+1);
                renderWidth = this.width/someCrop;
                renderHeight = this.height/someCrop;
            }
            canvX = canvX + xSpeed;
            canvY = canvY + ySpeed;
        }

        /**Рисуем наши спрайты*/
        public void onDraw(Canvas canvas) {
            update();
            Rect src = new Rect(0, height * frameCount , width, height * ( frameCount + 1)); //part of src bitmap
            Rect dst = new Rect(canvX , canvY , canvX + renderWidth, canvY + renderHeight); // screen area
            canvas.drawBitmap(control, src, dst, null);
            player.hideIfCollision(canvX, canvY, canvX + renderWidth, canvY + renderHeight);
        }
}

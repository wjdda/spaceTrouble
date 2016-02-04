package com.divanstudio.firsttry.ST;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by aaivanov on 12/5/15.
 */
public class Enemy extends Sprite {
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 1;
    private static final int IMG_SIZE_COEFFICIENT = 25;

    private Player player;

    private int xSpeed = 0;
    private int ySpeed = 0;

    private int canvX;  //convas coords
    private int canvY;  //convas coords

    private Random rnd;

    public Enemy (mainView gameView, Bitmap origBmp, int frameCount) {
        super(gameView, origBmp, frameCount, IMG_SIZE_COEFFICIENT, BMP_ROWS, BMP_COLUMNS);
        this.rnd = new Random();
        this.player = Player.getInstance();
        this.canvX = - renderWidth;
    }

        /**Перемещение объекта, его направление*/
        private void update()
        {
            if (canvX <= -renderWidth) {
                canvX = gameView.getWidth();
                canvY = rnd.nextInt(gameView.getHeight() - renderHeight);
                xSpeed = -(rnd.nextInt(3) + 1) * gameView.getHeight() / 100 ;
            }
            canvX = canvX + xSpeed;
            canvY = canvY + ySpeed;
        }

        public boolean isCollision () {
            return player.checkCollision(getHitBox());
        }

        public void onDraw(Canvas canvas) {
            update();
            super.onDraw(canvas, canvX, canvY);
        }
}

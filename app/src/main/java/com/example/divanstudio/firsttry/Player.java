package com.example.divanstudio.firsttry;

/**
 * Created by aaivanov on 12/5/15.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player {
    public static final Player  INSTANCE = new Player();
    private static final int BMP_ROWS = 1;
    private static final int BMP_COLUMNS = 1;
    private static final int PLAYER_SPEED = 10;

    private mainView gameView;
    private Bitmap bmp;

    private int x = 0;
    private int y = 0;

    private int xSpeed = 0;
    private int ySpeed = 0;

    private int currentFrame = 0;

    private int width;
    private int height;

    private boolean isHide = false;

    private int renderWidth;
    private int renderHeight;

    public Player () {}

    public Player (mainView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.renderHeight = this.height / 3;
        this.renderWidth = this.width / 3;
    }

    /**Перемещение объекта, его направление*/
    private void update() {
        if (x <= 0) {
            x = gameView.getWidth() / 3;
            y = ( gameView.getHeight() - renderHeight) / 2;
        }

        if (y >= gameView.getHeight() - renderHeight - ySpeed || y + ySpeed < 0)
        {
            ySpeed = 0;
        }

        y = y + ySpeed;
    }

    /**Рисуем наши спрайты*/
    public void onDraw(Canvas canvas) {
        if (!isHide) {
            update();
            Rect src = new Rect(0, 0, width, height);
            Rect dst = new Rect(x, y, x + renderWidth, y + renderHeight);
            canvas.drawBitmap(bmp, src, dst, null);
        }
    }

    public boolean isCollision(float x1, float y1, float x2, float y2) {
        boolean isCollision = x1 > x && x1 < x + renderWidth && y1 > y && y1 < y + renderHeight;
        if (!isCollision) { isCollision = x1 > x && x1 < x + renderWidth && y2 > y && y2 < y + renderHeight; }
        if (!isCollision) { isCollision = x2 > x && x2 < x + renderWidth && y1 > y && y1 < y + renderHeight; }
        if (!isCollision) { isCollision = x2 > x && x2 < x + renderWidth && y2 > y && y2 < y + renderHeight; }
        if (isCollision) {
            isHide = true;
        }
        return isCollision ;
    }

    public void moveUp () { ySpeed = PLAYER_SPEED; }

    public void moveDown () { ySpeed = -PLAYER_SPEED; }

    public void moveStop () { ySpeed = 0; }

}

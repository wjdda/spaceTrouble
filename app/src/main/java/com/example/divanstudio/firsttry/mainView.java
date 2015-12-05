package com.example.divanstudio.firsttry;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
* Created by aaivanov on 11/29/15.
*/

public class mainView extends SurfaceView  {
    private SurfaceHolder holder;
    private Bitmap background;
    private mainManager gameLoopThread;

    private Controls controls;
    private Enemys meteors;
    private Player player;
    private Rect src, dst;

    public mainView(Context context) {
        super(context);
        gameLoopThread = new mainManager(this);
        holder = getHolder();

      /*Рисуем все наши объекты и все все все*/
        holder.addCallback(new SurfaceHolder.Callback() {
            /*** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
        player = new Player(this, BitmapFactory.decodeResource(getResources(), R.drawable.player));
        meteors = new Enemys(this, BitmapFactory.decodeResource(getResources(), R.drawable.cut_map_pixelize), player);
        controls = new Controls(this, BitmapFactory.decodeResource(getResources(), R.drawable.arrows), player);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bckgrnd_1280_720_pixelize);
        src = new Rect(0, 0, background.getWidth(), background.getHeight());


    }

    /**
     * Функция рисующая все спрайты и фон
     */
    protected void onDraw(Canvas canvas) {
        dst = new Rect(0, 0, this.getWidth(), this.getHeight());
        canvas.drawBitmap(background, src, dst, null);

        meteors.onDraw(canvas);
        player.onDraw(canvas);

        controls.onDraw(canvas);
    }

    public void setTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            controls.isCollision(event);
        }
    }
}

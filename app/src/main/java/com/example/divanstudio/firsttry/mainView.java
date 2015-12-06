package com.example.divanstudio.firsttry;

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
    private mainManager gameLoopThread;

    private Controls controls;
    private Enemies meteors;
    private Player player;
    private Background background;

    public mainView(Context context) {
        super(context);
        gameLoopThread = new mainManager (this);
        holder = getHolder();

        /*** Рисуем все наши объекты и все все все*/
        this.holder.addCallback (new SurfaceHolder.Callback() {
            /*** Уничтожение области рисования */
            public void surfaceDestroyed (SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning (false);
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
                initMainViewRes();
            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    /** Фунция создающая все объекты */
    public void initMainViewRes () {
        player = new Player(this, BitmapFactory.decodeResource(getResources(), R.drawable.player));
        controls = new Controls(this, BitmapFactory.decodeResource(getResources(), R.drawable.arrows), player);
        meteors = new Enemies(this, BitmapFactory.decodeResource(getResources(), R.drawable.cut_map_pixelize), player);
        background = new Background(this, BitmapFactory.decodeResource(getResources(), R.drawable.bckgrnd_1280_720_pixelize));
    }

    /*** Функция рисующая все спрайты и фон */
    protected void onDraw(Canvas canvas) {
       if (background != null) background.onDraw(canvas);
       if (meteors != null) meteors.onDraw(canvas);
       if (player != null) player.onDraw(canvas);
       if (controls != null) controls.onDraw(canvas);
    }

    /*** Фунция принимаюшая все прикосновения к экрану */
    public void setTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            controls.isCollision(event);
        }
    }
}

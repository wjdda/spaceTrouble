package com.divanstudio.firsttry.ST;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.divanstudio.firsttry.R;

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
        player = Player.getInstance();
        holder = getHolder();

        this.holder.addCallback (new SurfaceHolder.Callback() {
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

            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                initMainViewRes();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
    }

    public void initMainViewRes () {
        player.setPlayerData(this, BitmapFactory.decodeResource(getResources(), R.drawable.player));
        controls = new Controls(this, BitmapFactory.decodeResource(getResources(), R.drawable.arrows));
        meteors = new Enemies(this, BitmapFactory.decodeResource(getResources(), R.drawable.cut_map_pixelize));
        background = new Background(this, BitmapFactory.decodeResource(getResources(), R.drawable.bckgrnd_1280_720_pixelize));
    }

    protected void onDraw(Canvas canvas) {
        if (background != null) background.onDraw(canvas);
        if (meteors != null) meteors.onDraw(canvas);
        if (player != null) player.onDraw(canvas);
        if (controls != null) controls.onDraw(canvas);
    }

    public void setTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            controls.isCollision(event);
        }
    }
}

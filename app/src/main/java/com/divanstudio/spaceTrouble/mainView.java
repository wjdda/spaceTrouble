package com.divanstudio.spaceTrouble;

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

    private StateManager State;
    private SoundManager soundDirector;

    private int sBackground;

    public mainView(Context context) {
        super(context);
        gameLoopThread = new mainManager (this);
        player = Player.getInstance();
        holder = getHolder();

        this.soundDirector = SoundManager.getInstance();

        this.State = StateManager.getInstance();

        this.holder.addCallback (new SurfaceHolder.Callback() {
            public void surfaceDestroyed (SurfaceHolder holder) {
                // Убиваем тред
                gameLoopThread.kill();

                // Удаляем менеджер
                soundDirector.allClear();
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

    public void initMainViewRes() {
        // Инициализация SFX меню
        this.soundDirector.addSFX(this.getContext(), "opt_start",  R.raw.menu_select_start);
        this.soundDirector.addSFX(this.getContext(), "opt_change", R.raw.menu_change_parameter);
        this.soundDirector.addSFX(this.getContext(), "opt_select", R.raw.menu_select_option);
        this.soundDirector.addSFX(this.getContext(), "tap_up",  R.raw.menu_push_up);
        this.soundDirector.addSFX(this.getContext(), "tap_down", R.raw.menu_push_down);
        this.soundDirector.addSFX(this.getContext(), "submenu_up", R.raw.menu_open_submenu);
        this.soundDirector.addSFX(this.getContext(), "submenu_down", R.raw.menu_close_submenu);

        // Инициализация SFX игрока
        this.soundDirector.addSFX(this.getContext(), "explosion",  R.raw.ship_explosion);

        // Инициализация SFX метеоритов
        this.soundDirector.addSFX(this.getContext(), "ship_punch",  R.raw.medium_punch_ship);

        // Инициализация Ambience SFX игрока
        this.soundDirector.addAmbienceSFX(this.getContext(), "ship_engine", R.raw.ship_eng_idling_big_loop);

        // Инициализация Музыки
        this.soundDirector.addMusic(this.getContext(), "main_menu", R.raw.main_menu);
        this.soundDirector.addMusic(this.getContext(), "asteroids", R.raw.asteroids);

        // Инициализация игрока
        player.setPlayerData(this, BitmapFactory.decodeResource(getResources(), R.drawable.player), "ship_engine");

        // Инициализация вражеских объектов
        meteors = new Enemies(this, BitmapFactory.decodeResource(getResources(), R.drawable.cut_map_pixelize));

        // Инициализация контролов
        controls = new Controls(this, BitmapFactory.decodeResource(getResources(), R.drawable.arrows ), meteors);

        // Инициализация бэкграунда
        background = new Background(this, BitmapFactory.decodeResource(getResources(), R.drawable.bckgrnd_1280_720_pixelize));
    }

    protected void onDraw(Canvas canvas) {
        if (background != null) background.onDraw(canvas);
        if (meteors != null) meteors.onDraw(canvas);
        if (player != null) player.onDraw(canvas);
        if (controls != null) controls.onDraw(canvas);
    }

    protected void playMusic() {
        if (this.State.getState() == StateManager.States.MENU) {
            this.soundDirector.play("main_menu");
        }

        if (this.State.getState() == StateManager.States.PLAY) {
            this.soundDirector.play("asteroids");
        }

        if (this.State.getState() == StateManager.States.GAMEOVER) {
            // TODO this.soundDirector.play("GameOver")
            this.soundDirector.stopMusic();
            this.soundDirector.stopAllAmbienceSFX();
        }

        if (this.State.getState() == StateManager.States.PAUSE) {
            this.soundDirector.pauseMusic();
            this.soundDirector.stopAllAmbienceSFX();
        }
    }

    public void setTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            controls.checkTapAction(event);
        }
    }
}
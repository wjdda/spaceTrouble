package com.divanstudio.firsttry.ST;

/**
 * Created by aaivanov on 11/29/15.
 */
import android.graphics.Canvas;
public class mainManager extends Thread {
    /** Наша скорость в мс = 10*/
    static final long FPS = 10;

    /** Объект класса GameView*/
    private mainView view;

    /** Задаем состояние потока*/
    private boolean running = false;

    /** Конструктор класса*/
    public mainManager( mainView view ) {
        this.view = view;
    }

    /** Задание состояния потока*/
    public void setRunning( boolean run ) {
        running = run;
    }

    /** Действия, выполняемые в потоке */
    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while ( running ) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized ( view.getHolder() ) {
                    view.onDraw( c );
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost( c );
                }
            }
            sleepTime = ticksPS - ( System.currentTimeMillis() - startTime );
            try {
                if ( sleepTime > 0 )
                    sleep( sleepTime );
                else
                    sleep( 10 );
            } catch ( Exception e ) {}
        }
    }
}

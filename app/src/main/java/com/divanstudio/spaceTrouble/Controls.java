package com.divanstudio.spaceTrouble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/1/15.
 */


public class Controls {

    private List<Control> Controls = new ArrayList<Control>();
    private Enemies meteors;
    private StateManager state ;

    private Player player;

    Function<Object> moveUp = new Function<Object>() {
        @Override public void run(Object object) {
            meteors.moveDown();
        }
    };
    Function<Object> moveDown = new Function<Object>() {
        @Override public void run(Object object) {
            meteors.moveUp();
        }
    };
    Function<Object> startGame = new Function<Object>() {
        @Override public void run(Object object) {
            state.setState(StateManager.States.PLAY);
        }
    };

    Function<Object> pauseGame = new Function<Object>() {
        @Override public void run(Object object) {
            state.setState(StateManager.States.PAUSE);
        }
    };

    Function<Object> goToMenu = new Function<Object>() {
        @Override public void run(Object object) {
            state.setState(StateManager.States.MENU);
        }
    };

    Function<Object> _emptyFunc = new Function<Object>() {
        @Override public void run(Object Object) {
        }
    };

    Function<Object> exitGame = new Function<Object>() {
        @Override public void run(Object Object) {
            System.exit(0);
        }
    };


        public Controls (mainView GameView, Bitmap bmp, Enemies meteors) {
            this.player = Player.getInstance();
            this.state = StateManager.getInstance();
            this.meteors = meteors;

            Controls.add(new imgControl(GameView, bmp, 0, 30, 50, moveUp, StateManager.States.PLAY));     //ub move button
            Controls.add(new imgControl(GameView, bmp, 1, 30, 150, moveDown, StateManager.States.PLAY));  //down move button

            Controls.add(new textControl(30, 30, 300, 5, "P", pauseGame, StateManager.States.PLAY));      //Pause
            Controls.add(new textControl(30, 30, 335, 5, "M", goToMenu, StateManager.States.PLAY));       // go to menu

            Controls.add(new textControl(200, 50, 100, 100, "START", startGame, StateManager.States.MENU));
            Controls.add(new textControl(200, 50, 100, 160, "MUTE", _emptyFunc, StateManager.States.MENU));
            Controls.add(new textControl(200, 50, 100, 220, "EXIT", exitGame, StateManager.States.MENU));

            Controls.add(new textControl(200, 50, 100, 100, "RESUME", startGame, StateManager.States.PAUSE));
            Controls.add(new textControl(200, 50, 100, 100, "CAME OVER", goToMenu, StateManager.States.GAMEOVER));

    }

    public void onDraw(Canvas canvas) {
        for (Control imgControl : Controls) {
            imgControl.onDraw(canvas);
        }
    }

    public void checkTapAction( MotionEvent event ) {
        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                for (Control menuControl : Controls) {
                    if (menuControl.isCollision(event.getX(), event.getY())) {
                        menuControl.runCallBack();
                    }
                }
                break;
        }
    }
}

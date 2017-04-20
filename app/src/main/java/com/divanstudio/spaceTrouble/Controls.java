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

    private List<Control> navControls = new ArrayList<Control>();
    private List<textControl> Menu = new ArrayList<textControl>();
    private Enemies meteors;
    private State state ;

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
            state.setState("Play");
        }
    };

    Function<Object> goToMenu = new Function<Object>() {
        @Override public void run(Object object) {
            state.setState("Menu");
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
            this.state = State.getInstance();
            this.meteors = meteors;

            navControls.add(new imgControl(GameView, bmp, 0, 30, 50, moveUp));  //ub move button
            navControls.add(new imgControl(GameView, bmp, 1, 30, 150, moveDown)); //down move button
            navControls.add(new textControl(30, 30, 300, 5, "P", _emptyFunc)); //Pause
            navControls.add(new textControl(30, 30, 335, 5, "M", goToMenu)); // go to menu

            Menu.add(new textControl(200, 50, 100, 100, "START", startGame));
            Menu.add(new textControl(200, 50, 100, 160, "MUTE", _emptyFunc));
            Menu.add(new textControl(200, 50, 100, 220, "EXIT", exitGame));

    }

    public void onDraw(Canvas canvas) {
        if (state.getState() == "Menu") {
            for (textControl menuItem : Menu) {
                menuItem.onDraw(canvas);
            }
        } else {
            for (Control imgControl : navControls) {
                imgControl.onDraw(canvas);
            }
        }
    }

    public void isCollision( MotionEvent event ) {
        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                navControlEventHandler(event);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(state.getState() == "Menu") {
                    menuControlEventHandler(event);
                } else {
                    meteors.moveStop();
                }
                break;
        }
    }

    private void navControlEventHandler ( MotionEvent event) {
        for (Control imgControl : navControls) {
            if (imgControl.isCollision(event.getX(), event.getY())) {
                imgControl.callBack.run(null);
            }
        }
    }

    private void menuControlEventHandler ( MotionEvent event) {
        for (textControl menuControl : Menu) {
            if (menuControl.isCollision(event.getX(), event.getY())) {
                menuControl.callBack.run(null);
            }
        }
    }
}

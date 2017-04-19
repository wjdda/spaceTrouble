package com.divanstudio.spaceTrouble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/1/15.
 */


public class Controls {

    private List<imgControl> navControls = new ArrayList<imgControl>();
    private List<textControl> Menu = new ArrayList<textControl>();
    private Enemies meteors;

    private State state ;

    private Player player;

    Function<Enemies> moveUp = new Function<Enemies>() {
        @Override public void run(Enemies enemies) {
//            enemies.moveUp();
            enemies.moveDown();

        }
    };
    Function<Enemies> moveDown = new Function<Enemies>() {
        @Override public void run(Enemies enemies) {
//            enemies.moveDown();
            enemies.moveUp();
        }
    };

    public Controls (mainView GameView, Bitmap bmp, Enemies meteors) {
        this.player = Player.getInstance();
        this.state = State.getInstance();
        this.meteors = meteors;

        navControls.add(new imgControl(GameView, bmp, 0, 30, 50, moveUp));  //ub move button
        navControls.add(new imgControl(GameView, bmp, 1, 30, 150, moveDown)); //down move button

        Menu.add(new textControl(200, 50, 100, 100, "START"));
        Menu.add(new textControl(200, 50, 100, 160, "MUTE"));
        Menu.add(new textControl(200, 50, 100, 220, "EXIT"));

    }

    public void onDraw(Canvas canvas) {
        if (state.getState() == "Menu") {
            for (textControl menuItem : Menu) {
                menuItem.onDraw(canvas);
            }
        } else {
            for (imgControl imgControl : navControls) {
                imgControl.onDraw(canvas);
            }
        }
    }

    public void isCollision( MotionEvent event ) {
        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                mouseEventHandler(event);
                break;

            case MotionEvent.ACTION_MOVE:
                mouseEventHandler(event);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(state.getState() == "Menu") {
                    state.setState("Play");
                } else {
                    meteors.moveStop();
                }
                break;
        }
    }

    private void mouseEventHandler ( MotionEvent event) {
        for (imgControl imgControl : navControls) {
            if (imgControl.isCollision(event.getX(), event.getY())) {
                imgControl.callBack.run(meteors);
            }
        }
    }
}

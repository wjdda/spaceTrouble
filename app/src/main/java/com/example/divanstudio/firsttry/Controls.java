package com.example.divanstudio.firsttry;

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
    Player player;

    public Controls (mainView GameView, Bitmap bmp, Player player) {
        Controls.add(new Control(GameView, bmp, 0, 30, 50));
        Controls.add(new Control(GameView, bmp, 1, 30, 150));
        this.player = player;
    }

    public void onDraw(Canvas canvas) {
        for(Control control : Controls) {
            control.onDraw(canvas);
        }
    }

    public void isCollision( MotionEvent event ) {
        switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN: // нажатие
                for (int i = Controls.size() - 1; i >= 0; i--) {
                    Control control = Controls.get(i);
                    if (control.isCollision(event.getX(), event.getY())) {
                        if (i == 0){
                            player.moveDown();
                        }
                        if (i == 1) {
                            player.moveUp();
                        }
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE: //движение
                for (int i = Controls.size() - 1; i >= 0; i--) {
                    Control control = Controls.get(i);
                    if (control.isCollision(event.getX(), event.getY())) {
                        if (i == 0){
                            player.moveDown();
                        }
                        if (i == 1) {
                            player.moveUp();
                        }
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP: //отпускание
            case MotionEvent.ACTION_CANCEL:
                player.moveStop();
                break;
        }
    }
}

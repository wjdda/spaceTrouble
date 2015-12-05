package com.example.divanstudio.firsttry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/5/15.
 */
public class Enemys {
    private List<Enemy> metiors = new ArrayList<Enemy>();
    Player player;

    public Enemys (mainView GameView, Bitmap bmp, Player player) {
        metiors.add(new Enemy(GameView, bmp, 0, player));
        metiors.add(new Enemy(GameView, bmp, 1, player));
        metiors.add(new Enemy(GameView, bmp, 2, player));
        metiors.add(new Enemy(GameView, bmp, 3, player));
        this.player = player;
    }

    public void onDraw(Canvas canvas) {
        for(Enemy metior : metiors) {
            metior.onDraw(canvas);
        }
    }

    public void isCollision(MotionEvent event) {

    }



}

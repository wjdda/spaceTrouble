package com.example.divanstudio.firsttry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaivanov on 12/5/15.
 */
public class Enemies {
    private List<Enemy> meteors = new ArrayList<Enemy>();


    public Enemies(mainView GameView, Bitmap bmp, Player player) {
        meteors.add(new Enemy(GameView, bmp, 0, player));
        meteors.add(new Enemy(GameView, bmp, 1, player));
        meteors.add(new Enemy(GameView, bmp, 2, player));
        meteors.add(new Enemy(GameView, bmp, 3, player));
    }

    public void onDraw(Canvas canvas) {
        for(Enemy meteor : meteors) {
            meteor.onDraw(canvas);
        }
    }
}

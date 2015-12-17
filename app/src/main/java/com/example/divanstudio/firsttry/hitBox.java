package com.example.divanstudio.firsttry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RUINIVAN on 17.12.2015.
 */
public class hitBox {
    private ArrayList<Point> apexList = new ArrayList<Point>();
    private int apexShift = 10;
    private int renderWidth;
    private int renderHeight;
    Paint paint = new Paint();

    public hitBox ( int renderWidth, int renderHeight ) {

        this.renderWidth = renderWidth;
        this.renderHeight  = renderHeight;
        apexList.add(new Point(apexShift, apexShift));
        apexList.add(new Point(this.renderWidth - apexShift, apexShift));
        apexList.add(new Point(this.renderWidth - apexShift, this.renderHeight - apexShift));
        apexList.add(new Point(apexShift, this.renderHeight - apexShift));

        paint.setColor(Color.RED);
    }

    public ArrayList<Point> getHitBox () {
        return apexList;
    }

    public void updateHitBox (int x, int y){
        apexList.set(0, new Point(x + apexShift, y + apexShift));
        apexList.set(1, new Point(x + renderWidth - apexShift, y + apexShift));
        apexList.set(2, new Point(x + renderWidth - apexShift, y + renderHeight - apexShift));
        apexList.set(3, new Point(x + apexShift, y + renderHeight - apexShift));

    }

    public void onDraw(Canvas canvas) {
        for ( int i = 0; i < apexList.size(); i++ ) {
            Point point = apexList.get(i);
            if (i == apexList.size() - 1 ){
                canvas.drawLine(apexList.get(i).x, apexList.get(i).y, apexList.get(0).x, apexList.get(0).y, paint);
            } else {
                canvas.drawLine(apexList.get(i).x, apexList.get(i).y, apexList.get(i+1).x, apexList.get(i+1).y, paint);
            }
        }
    }

    public boolean checkCollision (ArrayList<Point> hitBox) {
        Point a = new Point();
        Point b = new Point();
        boolean isCollision = false;

        for (int j = 0; j < hitBox.size(); j++) {
            if ( j == hitBox.size() - 1 ) {
                a = hitBox.get(j);
                b = hitBox.get(0);
            } else {
                a = hitBox.get(j);
                b = hitBox.get(j + 1);
            }
            for (int i = 0; i < apexList.size(); i++) {
                if ( i == apexList.size() - 1 ) {
                    isCollision = check(apexList.get(i), apexList.get(0), a, b);
                } else {
                    isCollision = check(apexList.get(i), apexList.get(i + 1), a, b);
                }
                if (isCollision == true) {
                    return isCollision;
                }
            }
        }
        return isCollision;
    }

    /*public int check(Point a, Point b, Point middle) {
        long ax = a.x - middle.x;
        long ay = a.y - middle.y;
        long bx = b.x - middle.x;
        long by = b.y - middle.y;
        int s = Long.signum(ax * by - ay * bx);
        if (s == 0 && (ay == 0 || by == 0) && ax * bx <= 0)
            return 0;
        if (ay < 0 ^ by < 0)
        {
            if (by < 0)
                return s;
            return -s;
        }
        return 1;
    }*/

    public boolean check(Point a1n, Point a2n, Point b1n, Point b2n) {
        Point a1, a2, b1;
        a1 = new Point( a1n.x - b2n.x, a1n.y - b2n.y );
        a2 = new Point( a2n.x - b2n.x, a2n.y - b2n.y );
        b1 = new Point( b1n.x - b2n.x, b1n.y - b2n.y );

        /*long v1 = (b2.x-b1.x)*(a1.y-b1.y)-(b2.y-b1.y)*(a1.x-b1.x);
          long v2 = (b2.x-b1.x)*(a2.y-b1.y)-(b2.y-b1.y)*(a2.x-b1.x);
          long v3 = (a2.x-a1.x)*(b1.y-a1.y)-(a2.y-a1.y)*(b1.x-a1.x);
          long v4 = (a2.x-a1.x)*(b2.y-a1.y)-(a2.y-a1.y)*(b2.x-a1.x);*/

        long v1 = b1.y * a1.x - b1.x * a1.y;
        long v2 = b1.y * a2.x - b1.x * a2.y;
        long v3 = (a2.x - a1.x)*(b1.y - a1.y) - (a2.y - a1.y)*(b1.x - a1.x);
        long v4 = a2.y * a1.x - a2.x * a1.y;

        if ( v1*v2 < 0 &&  v3*v4 < 0) {
            return true;
        }
        return false;
    }
}

package com.lfk.justweengine.Anim;

import android.graphics.Point;

/**
 * Created by liufengkai on 15/11/29.
 */
public class CircleMoveAnimation extends BaseAnim {
    private int radius;
    private Point center;
    private double angle;
    private float velocity;

    public CircleMoveAnimation(int centerX, int centerY,
                               int radius, double angle,
                               float velocity) {
        animating = true;
        animType = AnimType.POSITION;
        this.center = new Point(centerX, centerY);
        this.radius = radius;
        this.angle = angle;
        this.velocity = velocity;
    }

    @Override
    public Point adjustPosition(Point ori) {
        angle += velocity;
        ori.x = (int) (center.x + (float) (Math.cos(angle) * radius));
        ori.y = (int) (center.y + (float) (Math.sin(angle) * radius));
        return ori;
    }
}

package com.lfk.justweengine.Anim;

import android.graphics.Point;
import android.renderscript.Float2;

/**
 * Created by liufengkai on 15/12/3.
 */
public class MoveAnimation extends BaseAnim {
    private Float2 velocity;
    private float toX;
    private float toY;

    public MoveAnimation(
            float toX, float toY,
            Float2 velocity) {
        this.toX = toX;
        this.toY = toY;
        this.velocity = velocity;
        animating = true;
        animType = AnimType.POSITION;
    }

    @Override
    public Point adjustPosition(Point ori) {
        if (ori.x != toX) {
            ori.x += velocity.x;
        } else if (ori.y == toY) {
            animating = false;
        }
        if (ori.y != toY) {
            ori.y += velocity.y;
        } else if (ori.x == toX) {
            animating = false;
        }
        return ori;
    }
}

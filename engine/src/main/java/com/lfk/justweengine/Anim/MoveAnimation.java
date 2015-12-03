package com.lfk.justweengine.Anim;

import android.graphics.Point;
import android.renderscript.Float2;

/**
 * Created by liufengkai on 15/12/3.
 */
public class MoveAnimation extends BaseAnim {
    private Float2 velocity;
    private float from;
    private float to;

    public MoveAnimation(float from, float to, Float2 velocity) {
        this.from = from;
        this.to = to;
        this.velocity = velocity;
    }

    @Override
    public Point adjustPosition(Point ori) {

        return ori;
    }
}

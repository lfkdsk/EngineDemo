package com.lfk.justweengine.Anim;

import android.renderscript.Float2;

/**
 * 跳跃动画
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/29.
 */
public class ThrobAnimation extends BaseAnim {
    private float startScale, endScale, speed;
    private boolean started;

    public ThrobAnimation(float startScale, float endScale, float speed) {
        this.startScale = startScale;
        this.endScale = endScale;
        this.speed = speed;
        started = false;
        animating = true;
    }

    @Override
    public Float2 adjustScale(Float2 ori) {
        Float2 modified = ori;
        if (!started) {
            modified.x = startScale;
            modified.y = endScale;
            started = true;
        }
        modified.x += speed;
        modified.y += speed;
        if (modified.x >= endScale) {
            speed *= -1;
        } else if (modified.x <= startScale) {
            animating = false;
        }
        return modified;
    }
}

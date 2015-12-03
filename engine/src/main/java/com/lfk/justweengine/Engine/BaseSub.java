package com.lfk.justweengine.Engine;

import android.graphics.Rect;

import com.lfk.justweengine.Sprite.BaseSprite;

/**
 * Created by liufengkai on 15/12/4.
 */
public abstract class BaseSub {

    public abstract void drawWithFrame();

    public abstract void animation();

    public abstract boolean isCollidable();

    public abstract boolean isCollided();

    public abstract void setCollidable(boolean s_collidable);

    public abstract void setCollided(boolean s_collided);

    public abstract BaseSub getOffender();

    public abstract void setOffender(BaseSprite e_offender);

    public abstract Rect getBounds();
}

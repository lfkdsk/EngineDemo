package com.lfk.justweengine.Sprite;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.renderscript.Float2;

import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Info.UIdefaultData;

/**
 * 精灵基类
 * 包含基础的加载图片和位置
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/27.
 */
public class BaseSprite {
    private Engine s_engine;
    private Canvas s_canvas;
    private GameTexture s_texture;
    private Paint s_paint;
    private Point s_position;
    private int s_width, s_height;
    private int s_columns;
    private int s_alpha;
    private Float2 s_scale;
    private float s_rotation;

    public BaseSprite(Engine engine) {
        this.s_engine = engine;
        s_position.set(0, 0);
        init();
    }

    public BaseSprite(Engine engine, int x, int y) {
        this.s_engine = engine;
        this.s_position = new Point(x, y);
        init();
    }

    private void init() {
        s_canvas = null;
        s_texture = new GameTexture(s_engine);
        s_paint = new Paint();
        s_paint.setColor(UIdefaultData.sprite_default_color_paint);
    }

    public void setColor(int color) {
        s_paint.setColor(color);
    }

    public void draw() {
        s_canvas = s_engine.getCanvas();
        s_canvas.drawBitmap(s_texture.getBitmap(),
                s_position.x,
                s_position.y,
                s_paint);
    }

    public void setPaint(Paint paint) {
        s_paint = paint;
    }

    public void setTexture(GameTexture s_texture) {
        this.s_texture = s_texture;
    }

    public GameTexture getTexture() {
        return s_texture;
    }

    public void setPosition(Point s_position) {
        this.s_position = s_position;
    }

    public Point getPosition() {
        return s_position;
    }
}

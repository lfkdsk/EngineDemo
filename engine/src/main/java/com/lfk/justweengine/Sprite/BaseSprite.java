package com.lfk.justweengine.Sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;
import android.util.Log;

import com.lfk.justweengine.Anim.BaseAnim;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Info.UIdefaultData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

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
    public Point s_position;
    private int s_width, s_height;
    private int s_columns;
    private int s_alpha;
    private int s_frame;
    private Float2 s_scale;
    private float s_rotation;
    private HashMap<String, BaseAnim> animMap;
    private LinkedList<BaseAnim> animList;
    private Matrix s_mat_translation;
    private Matrix s_mat_scale;
    private Matrix s_mat_rotate;
    private Matrix s_matrix;
    private Bitmap s_frameBitmap;
    private Canvas s_frameCanvas;

    /**
     * easy init
     *
     * @param engine
     */
    public BaseSprite(Engine engine) {
        this(engine, 0, 0, 1);
    }

    /**
     * init with long pic
     *
     * @param engine
     * @param w
     * @param h
     * @param columns
     */
    public BaseSprite(Engine engine, int w, int h, int columns) {
        this.s_engine = engine;
        this.s_width = w;
        this.s_height = h;
        this.s_columns = columns;
        init();
    }

    private void init() {
        s_alpha = 255;
        s_canvas = null;
        s_texture = new GameTexture(s_engine);
        s_paint = new Paint();
        s_position = new Point(0, 0);
        s_frame = 0;
        animMap = new HashMap<>();
        animList = new LinkedList<>();
        s_scale = new Float2(1.0f, 1.0f);
        s_rotation = 0.0f;
        s_mat_translation = new Matrix();
        s_mat_scale = new Matrix();
        s_mat_rotate = new Matrix();
        s_matrix = new Matrix();
        s_frameBitmap = null;
        s_frameCanvas = null;
        s_paint.setColor(UIdefaultData.sprite_default_color_paint);
    }

    public void setColor(int color) {
        s_paint.setColor(color);
    }

    /**
     * draw
     */
    public void draw() {
        s_canvas = s_engine.getCanvas();

        // init width and height
        if (s_width == 0 || s_height == 0) {
            s_width = s_texture.getBitmap().getWidth();
            s_height = s_texture.getBitmap().getHeight();
        }

        // scratch bitmap
        if (s_frameBitmap == null) {
            s_frameBitmap = Bitmap.createBitmap(s_width, s_height, Bitmap.Config.ARGB_8888);
            s_frameCanvas = new Canvas(s_frameBitmap);
            s_frameCanvas.drawColor(Color.WHITE);
        }

        // calculate w/h in each frame
        int u = (s_frame % s_columns) * s_width;
        int v = (s_frame / s_columns) * s_height;

        // set rect
        Rect src = new Rect(u, v, u + s_width, v + s_height);

        // scale
        int x = s_position.x;
        int y = s_position.y;
        int w = (int) (s_width * s_scale.x);
        int h = (int) (s_height * s_scale.y);

        Rect dst = new Rect(x, y, x + w, y + h);

        // draw the frame
        s_paint.setAlpha(s_alpha);
        s_frameCanvas.drawBitmap(s_texture.getBitmap(), src, dst, s_paint);

        // update transform
        s_mat_scale = new Matrix();
        s_mat_scale.setScale(s_scale.x, s_scale.y);

        s_mat_rotate = new Matrix();
        s_mat_rotate.setRotate((float) Math.toDegrees(s_rotation));

        s_mat_translation = new Matrix();
        s_mat_translation.setTranslate(s_position.x, s_position.y);

        s_matrix = new Matrix(); //set to identity
        s_matrix.postConcat(s_mat_scale);
        s_matrix.postConcat(s_mat_rotate);
        s_matrix.postConcat(s_mat_translation);

        s_canvas.drawBitmap(s_frameBitmap, s_matrix, s_paint);
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


    public void setPosition(int x, int y) {
        s_position.set(x, y);
    }

    public Point getPostion() {
        return s_position;
    }


    public Point getPosition() {
        return s_position;
    }

    public void setFrame(int s_frame) {
        this.s_frame = s_frame;
    }

    public int getFrame() {
        return s_frame;
    }

    public void setAlpha(int s_alpha) {
        this.s_alpha = s_alpha;
    }

    public int getAlpha() {
        return s_alpha;
    }

    public int getHeight() {
        return s_height;
    }

    public int getWidth() {
        return s_width;
    }

    public void setHeight(int h) {
        s_height = h;
    }

    public void setWidth(int w) {
        s_width = w;
    }

    public Point getSize() {
        return new Point(s_width, s_height);
    }

    public Float2 getScale() {
        return s_scale;
    }

    public void setScale(Float2 scale) {
        s_scale = scale;
    }

    public void setScale(float scale) {
        s_scale = new Float2(scale, scale);
    }

    public float getRotation() {
        return s_rotation;
    }

    public void setRotation(float s_rotation) {
        this.s_rotation = s_rotation;
    }

    /**
     * add anim to list
     *
     * @param anim
     */
    public void addAnimation(BaseAnim anim) {
        animList.add(anim);
    }

    public void addfixedAnimation(String name, BaseAnim anim) {
        animMap.put(name, anim);
    }

    public void fixedAnimation(String name) {
        if (animMap.isEmpty()) return;
        BaseAnim anim = animMap.get(name);
        anim.animating = true;
        doAnimation(anim);
    }

    private void doAnimation(BaseAnim anim) {
        switch (anim.animType) {
            case FRAME:
                s_frame = anim.adjustFrame(s_frame);
                break;
            case ALPHA:
                s_alpha = anim.adjustAlpha(s_alpha);
                break;
            case SCALE:
                s_scale = anim.adjustScale(s_scale);
                break;
            case ROTATION:
                s_rotation = anim.adjustRotation(s_rotation);
                break;
            case POSITION:
                Log.e("first_alpha", s_position + "");
                s_position = anim.adjustPosition(s_position);
                Log.e("alpha", s_position + "");
                break;
        }
    }

    /**
     * list animation
     */
    public void animation() {
        if (animList.isEmpty()) return;
        ListIterator<BaseAnim> iterator = animList.listIterator();
        while (iterator.hasNext()) {
            BaseAnim anim = iterator.next();
            if (anim.animating) {
                doAnimation(anim);
            } else {
                animList.remove(anim);
                return;
            }
        }
    }
}

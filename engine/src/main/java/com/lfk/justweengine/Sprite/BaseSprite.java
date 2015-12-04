package com.lfk.justweengine.Sprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;

import com.lfk.justweengine.Anim.BaseAnim;
import com.lfk.justweengine.Anim.DoAfterAnimation;
import com.lfk.justweengine.Engine.BaseSub;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Utils.tools.DisplayUtils;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 精灵基类
 * 包含基础的加载图片和位置
 *
 * @author liufengkai
 *         Created by liufengkai on 15/11/27.
 */
public class BaseSprite extends BaseSub {
    // 名称
    private String s_name;
    // 死或生
    private boolean s_alive;
    // 是否可碰撞 / 是否检测过
    private boolean s_collidable, s_collided;
    private BaseSub e_offender;
    private int e_identifier;
    private FrameType frameType;
    // 传入的engine
    private Engine s_engine;
    private Canvas s_canvas;
    // 图片
    private GameTexture s_texture;
    // 画笔
    private Paint s_paint;
    // 位置 以左上角为准
    public Float2 s_position;
    // 宽度高度
    private int s_width, s_height;
    // 帧动画的列
    private int s_columns;
    // 透明度
    private int s_alpha;
    // 帧数
    private int s_frame;
    // 缩放
    private Float2 s_scale;
    // 旋转
    private float s_rotation;
    // 固定动画
    private ConcurrentHashMap<String, BaseAnim> animMap;
    // 流式动画
    private CopyOnWriteArrayList<BaseAnim> animList;
    // 动画结束的回调
    private DoAfterAnimation afterAnimation = null;
    private LinkedList<Rect> s_frame_rect;

    /**
     * easy init
     *
     * @param engine
     */
    public BaseSprite(Engine engine) {
        this(engine, 0, 0, 1);
        this.frameType = FrameType.SIMPLE;
    }

    public BaseSprite(Engine engine, int w, int h, FrameType type) {
        switch (type) {
            case SIMPLE:
                frameType = FrameType.SIMPLE;
                break;
            case COMMON:
                frameType = FrameType.COMMON;
                break;
        }
        this.s_engine = engine;
        this.s_width = w;
        this.s_height = h;
        init();
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
        this.frameType = FrameType.FIXED;
        init();
    }

    private void init() {
        s_alpha = 255;
        s_canvas = null;
        s_texture = new GameTexture(s_engine);
        s_paint = new Paint();
        s_position = new Float2(0, 0);
        s_frame = 0;
        animMap = new ConcurrentHashMap<>();
        animList = new CopyOnWriteArrayList<>();
        s_scale = new Float2(1.0f, 1.0f);
        s_rotation = 0.0f;
        s_collidable = true;
        s_collided = false;
        s_alive = true;

        if (frameType == FrameType.COMMON) {
            s_frame_rect = new LinkedList<>();
        } else if (frameType == FrameType.SIMPLE) {
            this.s_columns = 1;
        }

//        s_dst = new Rect();
//        s_mat_translation = new Matrix();
//        s_mat_scale = new Matrix();
//        s_mat_rotate = new Matrix();
//        s_matrix = new Matrix();
//        s_frameBitmap = null;
//        s_frameCanvas = null;
        s_paint.setColor(UIdefaultData.sprite_default_color_paint);
    }

    public void setColor(int color) {
        s_paint.setColor(color);
    }

    /**
     * draw
     */
    public void drawWithFixedFrame() {
        // init width and height
        if (s_width == 0 || s_height == 0) {
            s_width = s_texture.getBitmap().getWidth();
            s_height = s_texture.getBitmap().getHeight();
        }
//        // scratch bitmap
//        if (s_frameBitmap == null) {
//            s_frameBitmap = Bitmap.createBitmap(s_width, s_height, Bitmap.Config.ARGB_8888);
//            s_frameCanvas = new Canvas(s_frameBitmap);
//        }

        // calculate w/h in each frame
        int u = (s_frame % s_columns) * s_width;
        int v = (s_frame / s_columns) * s_height;

        // set rect
        Rect src = new Rect(u, v, u + s_width, v + s_height);

        // scale
        int x = (int) s_position.x;
        int y = (int) s_position.y;
        int w = (int) (s_width * s_scale.x);
        int h = (int) (s_height * s_scale.y);

        Rect s_dst = new Rect(x, y, x + w, y + h);

        // draw the frame
        s_paint.setAlpha(s_alpha);
        s_canvas.drawBitmap(s_texture.getBitmap(), src, s_dst, s_paint);

        // update transform
//        s_mat_scale = new Matrix();
//        s_mat_scale.setScale(s_scale.x, s_scale.y);
//
//        s_mat_rotate = new Matrix();
//        s_mat_rotate.setRotate((float) Math.toDegrees(s_rotation));
//
//        s_mat_translation = new Matrix();
//        s_mat_translation.setTranslate(s_position.x, s_position.y);
//
//        s_matrix = new Matrix(); //set to identity
//        s_matrix.postConcat(s_mat_scale);
//        s_matrix.postConcat(s_mat_rotate);
//        s_matrix.postConcat(s_mat_translation);

//        s_canvas.drawBitmap(s_frameBitmap, s_matrix, s_paint);
    }

    @Override
    public boolean getAlive() {
        return s_alive;
    }

    @Override
    public void draw() {
        s_canvas = s_engine.getCanvas();
        switch (frameType) {
            case FIXED:
            case SIMPLE:
                drawWithFixedFrame();
                break;
            case COMMON:
                drawWithFrame();
                break;
        }
    }

    public void drawWithFrame() {
        if (s_width == 0 || s_height == 0) {
            s_width = s_frame_rect.getFirst().width();
            s_height = s_frame_rect.getFirst().height();
        }

        if (!s_frame_rect.isEmpty()) {
            int x = (int) s_position.x;
            int y = (int) s_position.y;
            int w = (int) (s_width * s_scale.x);
            int h = (int) (s_height * s_scale.y);
            Rect s_dst = new Rect(x, y, x + w, y + h);
            s_paint.setAlpha(s_alpha);
            s_paint.setColor(Color.WHITE);
            s_paint.setStyle(Paint.Style.STROKE);
            s_canvas.drawRect(getBounds(), s_paint);
            s_canvas.drawBitmap(s_texture.getBitmap(),
                    s_frame_rect.get(s_frame),
                    s_dst, s_paint);
        }
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
        s_position.x = x;
        s_position.y = y;
    }

    public void setDipPosition(int x, int y) {
        s_position.x = DisplayUtils.dip2px(x);
        s_position.y = DisplayUtils.dip2px(y);
    }

    public Float2 getPosition() {
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

    public boolean isCollidable() {
        return s_collidable;
    }

    public boolean isCollided() {
        return s_collided;
    }

    public void setCollidable(boolean s_collidable) {
        this.s_collidable = s_collidable;
    }

    public void setCollided(boolean s_collided) {
        this.s_collided = s_collided;
    }

    public BaseSub getOffender() {
        return e_offender;
    }

    public void stopAllAnimation() {
        if (animList != null)
            animList.clear();
    }

    public void stopAllFixedAnimation() {
        if (animMap != null)
            animMap.clear();
    }

    public void stopAnimation(int position) {
        if (animList != null)
            animList.remove(position);
    }

    public void stopFixedAnimation(String name) {
        if (animMap != null)
            animMap.remove(name);
    }

    @Override
    public void setOffender(BaseSub e_offender) {
        this.e_offender = e_offender;
    }

    public Rect getBounds() {
        // scaled
        return new Rect((int) s_position.x, (int) s_position.y,
                (int) (s_position.x + s_width * s_scale.x),
                (int) (s_position.y + s_height * s_scale.y));
    }

    @Override
    public int getIdentifier() {
        return e_identifier;
    }

    public void setIdentifier(int e_identifier) {
        this.e_identifier = e_identifier;
    }

    public String getName() {
        return s_name;
    }

    public void setName(String s_name) {
        this.s_name = s_name;
    }

    public FrameType getFrameType() {
        return frameType;
    }

    public void setFrameType(FrameType frameType) {
        this.frameType = frameType;
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
//        anim.animating = true;
        doAnimation(anim);
    }

    public void addRectFrame(int x, int y, int w, int h) {
        if (s_frame_rect != null) {
            s_frame_rect.add(new Rect(x, y, x + w, y + h));
        }
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
                s_position = anim.adjustPosition(s_position);
                break;
            case ALIVE:
                s_alive = anim.adjustAlive(s_alive);
                break;
        }
        if (afterAnimation != null)
            afterAnimation.afterAnimation();
    }

    /**
     * list animation
     */
    public void animation() {
        if (animList.isEmpty()) return;
        for (BaseAnim anim : animList) {
            if (anim.animating) {
                doAnimation(anim);
            } else {
                animList.remove(anim);
                return;
            }
        }
    }

    /**
     * 设定放大值,相当于设置dp宽高
     *
     * @param dp
     */
    public void setDipWidth(int dp) {
        s_scale.x = DisplayUtils.dip2px(dp) / s_width;
    }

    public void setDipHeight(int dp) {
        s_scale.y = DisplayUtils.dip2px(dp) / s_height;
    }

    public void setDipScale(int dipWidth, int dipHeight) {
        if (s_width == 0 || s_height == 0) {
            s_width = s_texture.getBitmap().getWidth();
            s_height = s_texture.getBitmap().getHeight();
        }
        setScale(new Float2(DisplayUtils.dip2px(dipWidth) / s_width,
                DisplayUtils.dip2px(dipHeight) / s_height));
    }

    /**
     * 动画后的接口回调
     *
     * @param afterAnimation
     */
    public void setAfterAnimation(DoAfterAnimation afterAnimation) {
        this.afterAnimation = afterAnimation;
    }
}

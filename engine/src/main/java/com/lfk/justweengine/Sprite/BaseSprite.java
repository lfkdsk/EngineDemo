package com.lfk.justweengine.Sprite;

import android.graphics.Canvas;
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
    // 是否可碰撞 / 是否检测过
    private boolean s_collidable, s_collided;
    private BaseSub e_offender;
    private int e_identifier;
    // 传入的engine
    private Engine s_engine;
    private Canvas s_canvas;
    // 图片
    private GameTexture s_texture;
    // 画笔
    private Paint s_paint;
    // 位置 以左上角为准
    public Point s_position;
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
    //    private Matrix s_mat_translation;
//    private Matrix s_mat_scale;
//    private Matrix s_mat_rotate;
//    private Matrix s_matrix;
//    private Bitmap s_frameBitmap;
//    private Canvas s_frameCanvas;
    private Rect s_dst;
    // 动画结束的回调
    private DoAfterAnimation afterAnimation = null;

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
        animMap = new ConcurrentHashMap<>();
        animList = new CopyOnWriteArrayList<>();
        s_scale = new Float2(1.0f, 1.0f);
        s_rotation = 0.0f;
        s_collidable = true;
        s_collided = false;
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
    public void drawWithFrame() {
        s_canvas = s_engine.getCanvas();

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
        int x = s_position.x;
        int y = s_position.y;
        int w = (int) (s_width * s_scale.x);
        int h = (int) (s_height * s_scale.y);

        s_dst = new Rect(x, y, x + w, y + h);

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

    public void draw() {

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

    @Override
    public void setOffender(BaseSub e_offender) {
        this.e_offender = e_offender;
    }

    public Rect getBounds() {
        // scaled
        return new Rect(s_position.x, s_position.y,
                s_position.x + (int) (s_width * s_scale.x),
                s_position.y + (int) (s_height * s_scale.y));
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

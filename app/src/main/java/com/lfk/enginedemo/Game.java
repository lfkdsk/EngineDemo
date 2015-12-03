package com.lfk.enginedemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Drawable.Button.TextureButton;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Sprite.BaseSprite;

//import android.util.Log;

public class Game extends Engine {
    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    GameTimer timer;
    //    GameTexture texture;
    BaseSprite sprite;
    TextureButton button;
    float startX, startY, offsetX, offsetY;

    public Game() {
        super(false);
//        Log.d("game", " constructor");
        paint = new Paint();
        canvas = null;
//        texture = null;
        printer = new GameTextPrinter();
        printer.setTextColor(Color.WHITE);
        printer.setTextSize(24);
        printer.setLineSpaceing(28);
        timer = new GameTimer();
//        setTouchNum(5);
    }

    @Override
    public void init() {
//        Log.d("game", "init");
        super.setScreenOrientation(ScreenMode.PORTRAIT);
        UIdefaultData.init(this);
    }

    @Override
    public void load() {
        GameTexture texture = new GameTexture(this);
        texture.loadFromAsset("pic/zombie_walk.png");
        sprite = new BaseSprite(this, 96, 96, 8);
        sprite.setTexture(texture);
        sprite.setPosition(100, 300);
//        sprite.addAnimation(new ThrobAnimation(0.3f, 0.9f, 0.01f));
//        sprite.addAnimation(new CircleMoveAnimation(
//                300, 200, 200, 0, 0.05f));
        sprite.setDipScale(128, 128);
        sprite.addAnimation(new FrameAnimation(0, 63, 1));
//        sprite.setAfterAnimation(new DoAfterAnimation() {
//            @Override
//            public void afterAnimation() {
//                if (sprite.getScale().x <= 0.2f)
//                    sprite.addAnimation(new ThrobAnimation(0.2f, 0.8f, 0.01f));
////                float r = sprite.getRotation();
////                sprite.setRotation(r + 0.01f);
//            }
//        });
        addToSpriteGroup(sprite);
    }


    @Override
    public void draw() {
//        Log.d("game", " draw");

        paint.setColor(Color.WHITE);
        canvas = super.getCanvas();

        printer.setCanvas(canvas);
        printer.drawText("First engine demo", 10, 20);


//        sprite.drawWithFrame();

        if (super.getTouchPoints() > 0) {
            printer.drawText("Touch inputs: " + super.getTouchPoints());
            for (int i = 0; i < super.getTouchPoints(); i++) {
                printer.drawText(i + " :" + super.getTouchPoint(i).toString());
                Point p = super.getTouchPoint(i);
                if (p.x != 0 && p.y != 0) {
                    canvas.drawCircle(p.x, p.y, 50, paint);
                }
            }
        }

        if (timer.stopWatch(500)) {
            super.drawText("**TIMER**", super.getCanvas().getWidth() / 2, 20);
        }
    }

    @Override
    public void update() {
//        Log.d("engine", " update" + sprite.getAlpha());
        if (timer.stopWatch(20)) {

//            if (sprite.getScale().x <= 0.5f) {
//                sprite.addfixedAnimation("alpha", new ThrobAnimation(0.5f, 3.0f, 0.01f));
//            }
//        }
//            if (sprite.getAlpha() == 0) {
//                sprite.setAlpha(255);
//            }
//            sprite.animation();
            // manually update rotation
//            float r = sprite.getRotation();
//            sprite.setRotation(r + 0.01f);
//            // manually reset scaling

//            sprite.fixedAnimation("alpha");
        }
    }

    @Override
    public void touch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - startX;
                offsetY = event.getY() - startY;
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    if (sprite.s_position.x + offsetX > 0
                            && sprite.s_position.x + offsetX + sprite.getWidth() < UIdefaultData.screenWidth) {
                        sprite.s_position.x += offsetX;
                        restartEvent(event);
                    }
                } else {
                    if (sprite.s_position.y + offsetY > 0
                            && sprite.s_position.y + offsetY + sprite.getHeight() < UIdefaultData.screenHeight) {
//                        Log.e("position h:" + sprite.s_position.y, "h" + sprite.getHeight());
                        sprite.s_position.y += offsetY;
                        restartEvent(event);
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
        }
    }

    private void restartEvent(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();
    }
}

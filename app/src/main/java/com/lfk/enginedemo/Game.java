package com.lfk.enginedemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.renderscript.Float2;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Anim.MoveAnimation;
import com.lfk.justweengine.Drawable.Button.TextureButton;
import com.lfk.justweengine.Engine.BaseSub;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Sprite.BaseSprite;
import com.lfk.justweengine.Sprite.FrameType;

//import android.util.Log;

public class Game extends Engine {
    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    GameTimer timer;
    //    GameTexture texture;
    BaseSprite sprite, sprite2, ship;
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
        GameTexture texture1 = new GameTexture(this);
        texture1.loadFromAsset("pic/shoot.png");

        sprite = new BaseSprite(this, 96, 96, 8);
        sprite2 = new BaseSprite(this, 96, 96, 8);

        ship = new BaseSprite(this, FrameType.COMMON);

        sprite.setTexture(texture);
        sprite2.setTexture(texture);
        ship.setTexture(texture1);


        ship.addRectFrame(0, 100, 98, 124);

        ship.setDipPosition(200, 200);
        sprite.setPosition(100, 100);
        sprite.setPosition(200, 200);
//        sprite.addAnimation(new ThrobAnimation(0.3f, 0.9f, 0.01f));
//        sprite.addAnimation(new CircleMoveAnimation(
//                300, 200, 200, 0, 0.05f));
        sprite.setDipScale(128, 128);
        sprite2.setDipScale(128, 128);
        ship.setDipScale(96, 96);


        sprite.addAnimation(new FrameAnimation(0, 63, 1));
        sprite.addAnimation(new MoveAnimation(200, 1000, new Float2(1, 1)));
        sprite2.addAnimation(new FrameAnimation(0, 63, 1));
        sprite2.addAnimation(new MoveAnimation(200, 1000, new Float2(1, 1)));
//        sprite.setAfterAnimation(new DoAfterAnimation() {
//            @Override
//            public void afterAnimation() {
//                if (sprite.getScale().x <= 0.2f)
//                    sprite.addAnimation(new ThrobAnimation(0.2f, 0.8f, 0.01f));
////                float r = sprite.getRotation();
////                sprite.setRotation(r + 0.01f);
//            }
//        });
        sprite.setName("1");
        sprite2.setName("2");
        ship.setName("3");
        ship.setIdentifier(200);
        sprite.setIdentifier(100);
        sprite.setIdentifier(10);

        addToSpriteGroup(ship);
        addToSpriteGroup(sprite);
        addToSpriteGroup(sprite2);
    }


    @Override
    public void draw() {
//        Log.d("game", " draw");

        paint.setColor(Color.WHITE);
        canvas = super.getCanvas();

        printer.setCanvas(canvas);
        printer.drawText("Engine demo", 10, 20);


//        if (super.getTouchPoints() > 0) {
//            printer.drawText("Touch inputs: " + super.getTouchPoints());
//            for (int i = 0; i < super.getTouchPoints(); i++) {
//                printer.drawText(i + " :" + super.getTouchPoint(i).toString());
//                Point p = super.getTouchPoint(i);
//                if (p.x != 0 && p.y != 0) {
//                    canvas.drawCircle(p.x, p.y, 50, paint);
//                }
//            }
//        }

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

    @Override
    public void collision(BaseSub baseSub) {

    }

    private void restartEvent(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();
    }
}

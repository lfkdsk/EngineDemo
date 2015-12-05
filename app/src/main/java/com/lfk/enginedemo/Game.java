package com.lfk.enginedemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.renderscript.Float2;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Anim.MoveAnimation;
import com.lfk.justweengine.Engine.BaseSub;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Sprite.BaseSprite;
import com.lfk.justweengine.Sprite.FrameType;

public class Game extends Engine {
    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    GameTimer timer;
    //    GameTexture texture;
    BaseSprite ship;
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
    }

    @Override
    public void init() {
        super.setScreenOrientation(ScreenMode.PORTRAIT);
        super.setBackgroundColor(Color.WHITE);
        UIdefaultData.init(this);
    }

    @Override
    public void load() {
        GameTexture texture1 = new GameTexture(this);
        texture1.loadFromAsset("pic/shoot.png");
        ship = new BaseSprite(this, 100, 124, FrameType.COMMON);
        ship.setTexture(texture1);
        ship.addRectFrame(0, 100, 100, 124);
        ship.addRectFrame(167, 361, 100, 124);
        ship.addAnimation(new FrameAnimation(0, 1, 1));
        ship.setPosition(UIdefaultData.centerInHorizontalX - ship.getWidthWithScale() / 2,
                UIdefaultData.screenHeight + ship.getHeightWidthScale());
        ship.setDipScale(96, 96);
        ship.addfixedAnimation("start",
                new MoveAnimation(UIdefaultData.centerInHorizontalX - ship.getWidthWithScale() / 2,
                        UIdefaultData.screenHeight - 2 * ship.getHeightWidthScale(), new Float2(10, 10)));
        ship.setName("3");
        ship.setIdentifier(200);
        addToSpriteGroup(ship);
    }


    @Override
    public void draw() {

        paint.setColor(Color.WHITE);
        canvas = super.getCanvas();

        printer.setCanvas(canvas);
        printer.drawText("Engine demo", 10, 20);

        if (timer.stopWatch(500)) {
            super.drawText("**TIMER**", super.getCanvas().getWidth() / 2, 20);
        }
    }

    @Override
    public void update() {
        if (timer.stopWatch(20)) {
            if (ship.getFixedAnimation("start").animating) {
                ship.fixedAnimation("start");
            }
        }
    }

    @Override
    public void touch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - startX;
                offsetY = event.getY() - startY;
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    if (ship.s_position.x + offsetX > 0
                            && ship.s_position.x + offsetX +
                            ship.getHeightWidthScale() < UIdefaultData.screenWidth) {
                        ship.s_position.x += offsetX;
                        resetEvent(event);
                    }
                } else {
                    if (ship.s_position.y + offsetY > 0
                            && ship.s_position.y + offsetY +
                            ship.getHeightWidthScale() < UIdefaultData.screenHeight) {
                        ship.s_position.y += offsetY;
                        resetEvent(event);
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

    private void resetEvent(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();
    }
}

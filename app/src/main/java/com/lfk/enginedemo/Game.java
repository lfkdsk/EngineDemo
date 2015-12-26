package com.lfk.enginedemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Float2;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Anim.MoveAnimation;
import com.lfk.justweengine.Anim.VelocityAnimation;
import com.lfk.justweengine.Sprite.BaseSub;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Sprite.BaseSprite;
import com.lfk.justweengine.Sprite.FrameType;

import java.util.Random;

public class Game extends Engine {
    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    GameTimer timer, shoottimer, enemyTimer;
    Bitmap backGround2X;
    BaseSprite ship;
    float startX, startY, offsetX, offsetY;
    Rect bg_rect;
    Point bg_scroll;
    GameTexture shoot;
    GameTexture enemyPic;
    Random random;
    final int SHIP = 0;
    final int BULLET = 1;
    final int ENEMY = 2;
    int enemyNum = 0;

    public Game() {
        super(false);
        paint = new Paint();
        canvas = null;
        printer = new GameTextPrinter();
        printer.setTextColor(Color.BLACK);
        printer.setTextSize(24);
        printer.setLineSpaceing(28);
        shoottimer = new GameTimer();
        timer = new GameTimer();
        random = new Random();
        enemyTimer = new GameTimer();
    }

    @Override
    public void init() {
        super.setScreenOrientation(ScreenMode.PORTRAIT);
        UIdefaultData.init(this);
    }

    @Override
    public void load() {
        // load ship
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
        ship.setName("SHIP");
        ship.setIdentifier(SHIP);
        addToSpriteGroup(ship);

        // load bg
        GameTexture tex = new GameTexture(this);
        if (!tex.loadFromAsset("pic/background.png")) {
            fatalError("Error loading space");
        }
        backGround2X = Bitmap.createBitmap(
                UIdefaultData.screenWidth,
                UIdefaultData.screenHeight * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backGround2X);
        Rect dst = new Rect(0, 0, UIdefaultData.screenWidth - 1,
                UIdefaultData.screenHeight);
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);
        dst = new Rect(0, UIdefaultData.screenHeight,
                UIdefaultData.screenWidth,
                UIdefaultData.screenHeight * 2);
        canvas.drawBitmap(tex.getBitmap(), null, dst, null);

        shoot = new GameTexture(this);
        shoot.loadFromAsset("pic/flare.png");

        enemyPic = new GameTexture(this);
        enemyPic.loadFromAsset("pic/enemy.png");

        bg_rect = new Rect(0, 0, UIdefaultData.screenWidth, UIdefaultData.screenHeight);
        bg_scroll = new Point(0, 0);
    }


    @Override
    public void draw() {
        canvas = super.getCanvas();
        Rect dest = new Rect(0, 0, UIdefaultData.screenWidth, UIdefaultData.screenHeight);
        canvas.drawBitmap(backGround2X, bg_rect, dest, paint);
        printer.setCanvas(canvas);
        printer.drawText("Engine demo", 10, 20);
    }

    @Override
    public void update() {
        if (timer.stopWatch(20)) {
            scrollBackground();
        }
        if (ship.getFixedAnimation("start").animating) {
            ship.fixedAnimation("start");
        } else {
            fireBullet();
//            int size = getTypeSizeFromRecycleGroup(ENEMY);
//            if (size > 0)
//                enemyNum -= size;
            if (enemyTimer.stopWatch(200)) {
                addEnemy();
            }
        }
    }

    public void fireBullet() {
        if (!shoottimer.stopWatch(300)) return;
        BaseSprite bullet;
        if (getTypeSizeFromRecycleGroup(BULLET) > 0) {
            bullet = (BaseSprite) recycleSubFromGroup(BULLET);
            bullet.clearAllAnimation();
            removeFromRecycleGroup(bullet);
        } else {
            bullet = new BaseSprite(this);
            bullet.setTexture(shoot);
            bullet.setDipScale(8, 18);
            bullet.setIdentifier(BULLET);
        }
        double angle = 270.0;
        float speed = 20.0f;
        int lifetime = 2500;
        bullet.addAnimation(new VelocityAnimation(angle, speed,
                lifetime));
        bullet.setPosition(ship.s_position.x +
                        ship.getWidthWithScale() / 2
                        - bullet.getWidthWithScale() / 2,
                ship.s_position.y - 24);
        bullet.setAlive(true);
        addToSpriteGroup(bullet);
    }

    private void addEnemy() {
        BaseSprite enemy;
        if (getTypeSizeFromRecycleGroup(ENEMY) > 0) {
            enemy = (BaseSprite) recycleSubFromGroup(ENEMY);
            enemy.clearAllAnimation();
            removeFromRecycleGroup(enemy);
        } else {
            enemy = new BaseSprite(this);
            enemy.setTexture(enemyPic);
            enemy.setIdentifier(ENEMY);
            enemy.setDipScale(49, 36);
        }
        double angle = 90.0;
        float speed = 5.0f;
        int lifetime = 5000;
        enemy.addAnimation(new VelocityAnimation(angle, speed,
                lifetime));
        enemy.setPosition(random.nextInt(UIdefaultData.screenWidth),
                -enemy.getWidthWithScale());
        enemy.setAlive(true);
        addToSpriteGroup(enemy);
        enemyNum++;
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
        BaseSprite other = (BaseSprite) baseSub.getOffender();
        if (baseSub.getIdentifier() == BULLET &&
                other.getIdentifier() == ENEMY) {
            other.setAlive(false);
            removeFromSpriteGroup(other);
            addToRecycleGroup(other);
            enemyNum--;
        }
    }

    private void resetEvent(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();
    }

    public void scrollBackground() {
        bg_scroll.y += 10.0f;
        bg_rect.top = bg_scroll.y;
        bg_rect.bottom = bg_rect.top + UIdefaultData.screenHeight - 1;
        if (bg_scroll.y + bg_rect.height() > backGround2X.getHeight()) {
            bg_scroll.y = bg_scroll.y - bg_rect.height();
        }
    }

}

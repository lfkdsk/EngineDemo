package com.lfk.enginedemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;

public class Game extends Engine {
    GameTextPrinter printer;
    Paint paint;
    Canvas canvas;
    GameTimer timer;
    GameTexture texture;

    public Game() {
        Log.d("game", " constructor");

        paint = new Paint();
        canvas = null;
        texture = null;
        printer = new GameTextPrinter();
        printer.setTextColor(Color.WHITE);
        printer.setTextSize(24);
        printer.setLineSpaceing(28);
        timer = new GameTimer();
        setTouchNum(5);
    }

    @Override
    public void init() {
        Log.d("game", "init");
        super.setScreenOrientation(ScreenMode.PORTRAIT);
    }

    @Override
    public void load() {
        Log.d("game", " load");
        texture = new GameTexture(this);
        if (!texture.loadFromAsset("pic/image806.png")) {
            super.fatalError("error load pic");
        }
    }


    @Override
    public void draw() {
        Log.d("game", " draw");

        paint.setColor(Color.WHITE);
        canvas = super.getCanvas();

        //
        canvas.drawBitmap(texture.getBitmap(), 10, 300, paint);

        printer.setCanvas(canvas);
        printer.drawText("First engine demo", 10, 20);

        if (super.getTouchPoints() > 0) {
            printer.drawText("Touch inputs: " + super.getTouchPoints());
            for (int i = 0; i < super.getTouchPoints(); i++) {
                printer.drawText(i + " :" + super.getTouchPoint(i).toString());
                Point p = super.getTouchPoint(i);
                Log.d("point :", "x:" + p.x + " y:" + p.y);
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
        Log.d("engine", " update");
    }
}

package com.xcorp.appxsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.xcorp.appx.gui.xButton;
import com.xcorp.appx.gui.xPanel;
import com.xcorp.appx.objects.xFont;
import com.xcorp.appx.objects.xRect;
import com.xcorp.appx.xApp;

public class MainPanel extends xPanel {

    xRect rect;
    xButton button;
    float[] dir = {1, 1};

    public MainPanel(xApp app, Context context) {
        super("mainPanel", app, context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rect = new xRect(100, 100, 100, 200, this);
        button = new xButton(
            this,
            new xFont(app, R.font.arial, 64),
            "Proceed", Color.BLACK
        );
        button = new xButton(
            this,
            new xFont(app, R.font.arial, 70).render("Proceed", Color.BLACK, Color.BLUE, 45, 0.5f),
            null
//            new xFont(app, R.font.arial, 70).render("Proceed", Color.BLACK, Color.argb(255, 255, 255), 45, 0.5f)
        );
        button.setX(app.rect.centerX - button.getWidth()/2);
        button.setY(app.rect.h * 0.65f);
    }

    @Override
    public void update() {
        super.update();
        rect.moveX(30*dir[0] * app.dt);
        if (rect.right > app.rect.right) {
            rect.setRight(app.rect.right);
            dir[0] *= -1;
        }
        else if (rect.left < app.rect.left) {
            rect.setLeft(app.rect.left);
            dir[0] *= -1;
        }

        rect.moveY(30*dir[1] * app.dt);
        if (rect.bot > app.rect.bot) {
            rect.setBot(app.rect.bot);
            dir[1] *= -1;
        }
        else if (rect.top < app.rect.top) {
            rect.setTop(app.rect.top);
            dir[1] *= -1;
        }

        button.update(this.handler);
        if (button.isTouchedAndReleased()) {
            app.changePanel("subPanel");
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // background
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        app.rect.draw(canvas, paint);
        // rect
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        paint.setColor(Color.BLUE);
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bot, paint);

        // button
        button.draw(canvas);
    }
}

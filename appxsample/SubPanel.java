package com.xcorp.appxsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.text.LineBreaker;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.xcorp.appx.gui.xPanel;
import com.xcorp.appx.objects.Sprites;
import com.xcorp.appx.objects.xFont;
import com.xcorp.appx.objects.xRect;
import com.xcorp.appx.xApp;

public class SubPanel extends xPanel {

    int pointerID = -1;
    xRect rect;
    xFont font;
    TextView textView;

    public SubPanel(xApp app, Context context) {
        super("subPanel", app, context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rect = new xRect(20, 20, 100, 100, this);
        rect.x = app.rect.centerX - rect.w/2;
        font = new xFont(app, R.font.arial, 90);
        textView = new TextView(app.context);
        textView.setFocusable(true);
        String t = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        textView.setText("Hello World, Kamusta ka na dyan?");
        textView.setText(t);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setTextSize(24);
        textView.setX(200);
        textView.setBreakStrategy(LineBreaker.BREAK_STRATEGY_BALANCED);
        textView.setHeight(900);
        textView.setWidth(500);
        textView.setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
        app.layout.addView(textView);
    }

    @Override
    public void update() {
        super.update();

        // scroll mechanics
        handler.updateScrollListener();
        rect.moveY(handler.scrollVel);
        if (rect.bot < app.rect.top) {
            rect.setTop(app.rect.bot);
        }
        else if (rect.top > app.rect.bot) {
            rect.setTop(app.rect.top);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // background
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        app.rect.draw(canvas, paint);

        Bitmap test = font.render("Hello World", Color.GREEN);
        canvas.drawBitmap(test, 10, 10, null);
        canvas.drawBitmap(font.render("" + (int)app.thread.getAverageFPS(), Color.rgb(0, 255, 255)), 100, 100, null);

        canvas.drawRect(200, 0, 700, 900, Sprites.paintBounds);
        rect.draw(canvas);
    }
}

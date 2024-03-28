package com.xcorp.appx.gui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.text.LineBreaker;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.xcorp.appx.objects.xFont;
import com.xcorp.appx.objects.xRect;
import com.xcorp.appxsample.R;

@SuppressLint("AppCompatCustomView")
public class xMessageBox {

    private xPanel panel;
    //general
    private TextView textView;
    public xRect rect;
    public boolean active = true;
    private int maxScrollY;
    // ui
    private String title;
    public float padding = 0.11f, scrollBarHeight = 0.06f;
    public int bgColor, textColor;
    private xFont titleFont;
    private Bitmap titleBitmap;
    private int touchID = -1;

    public xMessageBox(xPanel parentPanel, String title, float y, float w, float h) {
        // constructor
        this.title = title;
        panel = parentPanel;
        float x = panel.app.rect.centerX - w/2;
        rect = new xRect(x, y, w, h);
        // initializations
        textView = new TextView(parentPanel.app.context);
        bgColor = Color.rgb(255, 255, 255);
        if (title != null) {
            titleFont = new xFont(panel.app, R.font.arial, textView.getTextSize()*2f);
            titleBitmap = titleFont.render(title, Color.BLACK);
        }
        // update textview pos and size
        textView.setX(x + w*padding/2);
        textView.setY(y + h*padding/2);
        if (title != null) textView.setY(textView.getY() + titleBitmap.getHeight() + h*padding/2);
        textView.setWidth( (int) (w - w*padding) );
        textView.setHeight( (int) (rect.bot - textView.getY() - h*padding/2) );
        // more textview initializations
        textView.setFocusable(true);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setBreakStrategy(LineBreaker.BREAK_STRATEGY_BALANCED);
        textView.setVisibility(panel.visible ? View.VISIBLE : View.INVISIBLE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        textView.setTypeface(panel.app.resources.getFont(R.font.arial));
        panel.app.layout.addView(textView);
    }

    private int getMaxScrollY() {
        return textView.getLayout().getLineTop(textView.getLineCount()) - textView.getHeight();
    }

    public xMessageBox(xPanel parentPanel, float y, float w, float h) {
        this(parentPanel, null, y, w, h);
    }

    public xMessageBox(xPanel parentPanel, xRect rect) {
        this(parentPanel, rect.y, rect.w, rect.h);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setTextSize(float size) {
        textView.setTextSize(size);
    }

    public void setTextAlignment(int alignment) {
        panel.app.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setTextAlignment(alignment);
                textView.setWidth(textView.getWidth());
            }
        });
    }

    public void draw(Canvas canvas) {
        if (!active) return;
        Paint paint;

        // dim the background
        paint = new Paint();
        paint.setColor(Color.argb(100, 0, 0, 0));
        panel.app.rect.draw(canvas, paint);

        // draw the container/background of the message box
        paint = new Paint();
        paint.setColor(bgColor);
        canvas.drawRoundRect(rect.left, rect.top, rect.right, rect.bot, 25, 25, paint);

        titleBitmap = titleFont.render("scroll: " + textView.getScrollY() + " / " + getMaxScrollY(), Color.BLACK);

        // render the title
        if (title != null) {
            canvas.drawBitmap(
                titleBitmap,
                rect.centerX - titleBitmap.getWidth()/2,
                rect.top + rect.h*padding/2, null
            );
        }

        // draw scroll bar
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(rect.w*0.01f);
        paint.setAlpha(100);
        paint.setStrokeCap(Paint.Cap.ROUND);
        float scrollBarH = textView.getHeight()*scrollBarHeight;
        float maxScrollH = textView.getHeight() - scrollBarH;
        float scrollVal = (float)textView.getScrollY()/(float)getMaxScrollY();
        canvas.drawLine(
        textView.getX() + textView.getWidth(),
        textView.getY() + scrollBarH/2 + maxScrollH*scrollVal - scrollBarH/2,
        textView.getX() + textView.getWidth(),
        textView.getY() + scrollBarH/2 + maxScrollH*scrollVal + scrollBarH/2,
              paint
        );

        // ensures that the textView's visibility is changed only once (save process time)
        if (panel.visible && textView.getVisibility() == View.VISIBLE) return;
        if (!panel.visible && textView.getVisibility() != View.VISIBLE) return;

        setTextViewVisibility(panel.visible ? true : false);
    }

    public void update(TouchHandler handler) {
        if (!active) return;

        switch (handler.type) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (touchID != -1) break;
                touchID = handler.pointerID;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                if (touchID == handler.pointerID) {
                    if (!rect.collides(handler.getPos(touchID))) {
                        setActive(false);
                    }
                }
                touchID = -1;
                break;
        }
    }

    public void setActive(boolean flag) {
        setTextViewVisibility(flag);
        this.active = flag;
    }

    private void setTextViewVisibility(boolean flag) {
        panel.app.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
                textView.setWidth(textView.getWidth());
            }
        });
    }
}


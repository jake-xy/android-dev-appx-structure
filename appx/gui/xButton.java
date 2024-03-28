package com.xcorp.appx.gui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.xcorp.appx.objects.xFont;
import com.xcorp.appx.objects.xRect;

public class xButton {

    // root attributes
    xRect rect;
    Bitmap focused, unfocused;
    xPanel panel;

    // ui attributes
    private int touchID = -1;
    private boolean touching = false, touchedAndReleased = false;

    // main constructor
    public xButton(xPanel parentPanel, Bitmap unfocused, Bitmap focused, float x, float y, float w, float h) {
        this.panel = parentPanel;
        this.focused = focused;
        this.unfocused = unfocused;
        rect = new xRect(x, y, w, h, parentPanel);
    }
    // alternative constructors
    public xButton(xPanel parentPanel, Bitmap unfocused, Bitmap focused) {
        this(parentPanel, unfocused, focused, 0, 0, unfocused.getWidth(), unfocused.getHeight());
    }
    public xButton(xPanel parentPanel, xFont font, String text, int textColor) {
        this(
            parentPanel,
            font.render(text, textColor), null,
            0, 0,
            font.render(text, textColor).getWidth(),
            font.render(text, textColor).getHeight()
        );
    }


    // gameLoop methods
    public void update(TouchHandler handler) {
        switch (handler.type) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (touchID != -1) break;
                if (!rect.collides(handler.getPos())) break;
                touching = true;
                touchID = handler.pointerID;
                touchedAndReleased = false;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (touching && touchID == handler.pointerID) {
                    touching = false;
                    if (rect.collides(handler.getPos())) {
                        touchedAndReleased = true;
                    }
                }
                touchID = -1;
                break;
        }
    }

    public void draw(Canvas canvas) {
        // render the focused bitmap if it's present
        if (touching && focused != null) {
            canvas.drawBitmap(focused, rect.x, rect.y, null);
            return;
        }

        // render the unfocused bitmap
        canvas.drawBitmap(unfocused, rect.x, rect.y, null);

        // draw a highlight of the button if there is no focused bitmap
        if (touching && focused == null) {
            Paint paint = new Paint();
            paint.setColor(Color.argb(50, 0, 0, 0));
            rect.draw(canvas, paint);
        }
    }


    // setters
    public void setX(float x) {
        rect.setX(x);
    }

    public void setY(float y) {
        rect.setY(y);
    }


    // getters
    public boolean isTouchedAndReleased() {
        if (touchedAndReleased) {
            touchedAndReleased = false;
            return true;
        }
        return false;
    }

    public float getWidth() {
        return rect.w;
    }

    public float getHeight() {
        return rect.h;
    }
}

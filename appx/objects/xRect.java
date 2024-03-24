package com.xcorp.appx.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.xcorp.appx.gui.xPanel;
import com.xcorp.appx.xApp;
import com.xcorp.appxsample.MainActivity;

public class xRect extends View {

    public Rect clipBounds;
    public float x, y, w, h; // public attribute to be accessed.. holds the copy of the actual value
    public float left, top, right, bot; // square-like attributes
    public float[] topLeft = new float[2], botRight = new float[2];
    public float centerX, centerY;
    private xPanel parentPanel;

    public xRect(float x, float y, float w, float h, xPanel panel) {
        this(x, y, w, h);
        this.parentPanel = panel;
    }

    public xRect(float x, float y, float w, float h) {
        super(MainActivity.app.context);
        this.x = x; this.y = y; this.w = w; this.h = h;
        updateValues();
    }

    public void moveX(float val) {
        this.x += val;
        updateValues();
    }

    public void moveY(float val) {
        this.y += val;
        updateValues();
    }

    public void setLeft(float newLeft) {
        this.x = newLeft;
        updateValues();
    }

    public void setRight(float newRight) {
        this.x = newRight - this.w;
        updateValues();
    }

    public void setTop(float newTop) {
        this.y = newTop;
        updateValues();
    }

    public void setBot(float newBot) {
        this.y = newBot - this.h;
        updateValues();
    }

    public void setTopLeft(float[] newTopLeft) {
        x = newTopLeft[0]; y = newTopLeft[1];
        updateValues();
    }

    public void updateValues() {
        left = x; right = x + w;
        top = y; bot = y + h;

        topLeft[0] = top; topLeft[1] = left;
        botRight[0] = bot; botRight[1] = right;

        centerX = x + w/2;
        centerY = y + h/2;
    }

    // collisions
    public boolean collides(xRect rect) {
        if (!(right > rect.left && left < rect.right)) return false;
        if (!(bot > rect.top && top < rect.bot)) return false;
        return true;
    }

    public boolean collides(float x, float y) {
        if (!(x > this.x && x < this.x + this.w)) return false;
        if (!(y > this.y && y < this.y + this.h)) return false;
        return true;
    }

    public boolean collides(float[] point) {
        return collides(point[0], point[1]);
    }

    public boolean isIn(xRect rect) {
        // returns true if this rect is inside the passed rect
        if (!(this.x > rect.left && this.x + this.w < rect.right)) return false;
        if (!(this.y > rect.top && this.y + this.h < rect.bot)) return false;
        return true;
    }

    public float[] depthCollision(Bitmap bitmap, xRect object) {
        float[] out = new float[] {0, 0};

        // early out
        if (!this.collides(object)) return out;

        float left, top, right, bot;

        left = xApp.Math.max(this.left, object.left);
        top = xApp.Math.max(this.top, object.top);
        right = xApp.Math.min(this.right, object.right);
        bot = xApp.Math.min(this.bot, object.bot);

        int cols = (int) (right - left);
        int rows = (int) (bot - top);

        float bx = left - object.left;
        float by = top - object.top;

        // get the y depth first
        float topMost = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int color = bitmap.getPixel((int) (bx + x), (int) (by + y));

                if (color != Color.TRANSPARENT) {
                    topMost = y;
                    y = (int)rows;
                    break;
                }
            }
        }

        float botMost = 0;
        for (int y = rows-1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                int color = bitmap.getPixel((int) (bx + x), (int) (by + y));

                if (color != Color.TRANSPARENT) {
                    botMost = y;
                    y = -1;
                    break;
                }
            }
        }

        out[1] = botMost - topMost + 1;


        // get the x depth
        float leftMost = 0;
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                int color = bitmap.getPixel((int) (bx + x), (int) (by + y));

                if (color != Color.TRANSPARENT) {
                    leftMost = x;
                    x = (int)cols;
                    break;
                }
            }
        }

        float rightMost = 0;
        for (int x = cols-1; x >= 0; x--) {
            for (int y = 0; y < rows; y++) {
                int color = bitmap.getPixel((int) (bx + x), (int) (by + y));

                if (color != Color.TRANSPARENT) {
                    rightMost = x;
                    x = -1;
                    break;
                }
            }
        }

        out[0] = rightMost - leftMost + 1;

        return out;
    }

    // -----
    @Override
    public String toString() {
        return "x: " + x + " y: " + y + " w: " + w + " h: " + h;
    }

    public String toString(boolean isBounds) {
        if (!isBounds) return toString();
        return "left: " + left + "\ntop: " + top + "\nright: " + right + "\nbot: " + bot;
    }


    // misc methods
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(left, top, right, bot, Sprites.paintBounds);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(left, top, right, bot, paint);
    }

    public void draw(float x, float y, Canvas canvas, Paint paint) {
        canvas.drawRect(x, y, x+w, y+h, paint);
    }

    public void draw(float x, float y, Canvas canvas) {
        this.draw(x, y, canvas, Sprites.paintBounds);
    }

    public xRect copy() {
        return new xRect(this.x, this.y, this.w, this.h, parentPanel);
    }

    public static xRect[] append(xRect item, xRect[] array) {
        xRect[] out = new xRect[array.length+1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }
}

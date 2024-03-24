package com.xcorp.appx.objects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.xcorp.appx.xApp;

public class xFont {

    public Paint paint;
    public Typeface typeface;
    private float size;

    public xFont(xApp app, int fontRes, float size) {
        this.size = size;
        typeface = app.resources.getFont(fontRes);
        paint = new Paint();
        paint.setTextSize(size);
        paint.setTypeface(typeface);
    }


    public Bitmap render(String text, int color) {
        Rect r = new Rect();
        paint.setColor(color);
        paint.getTextBounds(text, 0, text.length(), r);

        Bitmap out = Bitmap.createBitmap(r.width(), r.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        canvas.clipOutRect(r); // crops the canvas
        // -8% is there because somehow canvas.drawText() renders the text 8%(of size) pixels ahead of the given x value
        // and r.height() as y value because it canvas.drawText() renders the text above the given y value
        canvas.drawText(text, (float) (-size*0.08), r.height(), paint);

        return out;
    }
}

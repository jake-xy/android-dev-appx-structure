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
//        Rect r = new Rect();
//        paint.setColor(color);
//        paint.getTextBounds(text, 0, text.length(), r);
//
//        Bitmap out = Bitmap.createBitmap(r.width(), r.height(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(out);
//        canvas.clipOutRect(r); // crops the canvas
//        // -8% is there because somehow canvas.drawText() renders the text 8%(of size) pixels ahead of the given x value
//        // and r.height() as y value because it canvas.drawText() renders the text above the given y value
//        canvas.drawText(text, (float) (-size*0.08), r.height(), paint);
//
//        return out;
        return render(text, color, -1, -1, 0);
    }

    public Bitmap render(String text, int color, float paddingX) {
        return render(text, color, -1, -1, paddingX);
    }

    public Bitmap render(String text, int color, int bgColor, float bgRoundness, float padding) {
        // get the size of the output
        Rect r = new Rect();
        paint.setColor(color);
        paint.getTextBounds(text, 0, text.length(), r);

        Bitmap out = Bitmap.createBitmap(
        r.width() + (padding >= 0 ? (int)(padding*r.width()) : 0),
        r.height() + (padding >= 0 ? (int)(padding*r.width()) : 0),
            Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(out);
        canvas.clipOutRect(r); // crops the canvas

        // render the background if there is any
        if (bgColor != -1) {
            Paint bgPaint = new Paint();
            bgPaint.setColor(bgColor);
            canvas.drawRoundRect(
            0, 0,
            r.width()*(1+padding), r.height() + r.width()*padding,
                bgRoundness, bgRoundness,
                bgPaint
            );
            Log.d("debugNow", "nagdrawing ng background sa font");
        }

        // render the text
        canvas.drawText(
            text,
            -size*0.08f + (r.width()*padding/2),
            r.height() + (r.width()*padding/2),
            paint
        );
        // -8% is there because somehow canvas.drawText() renders the text 8%(of size) pixels ahead of the given x value
        // and r.height() as y value because it canvas.drawText() renders the text above the given y value


        return out;
    }
}

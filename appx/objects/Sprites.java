package com.xcorp.appx.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import androidx.core.content.ContextCompat;

import com.xcorp.appxsample.MainActivity;
import com.xcorp.appxsample.R;

import java.lang.reflect.Field;

public class Sprites {

    private static Bitmap[] bitmaps = new Bitmap[0];
    private static int[] ids = new int[0];

    public static Paint
            paintBounds, paintTransparent, paintEmpty
            ;

    public static void initialize() {
        for (Field f : R.drawable.class.getFields()) {
            try {
                add(f.getInt(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        paintBounds = new Paint();
        paintBounds.setStyle(Paint.Style.STROKE);
        paintBounds.setStrokeWidth(2);
        paintBounds.setColor(Color.RED);

        paintTransparent = new Paint();
        paintTransparent.setAlpha(150);

        paintEmpty = new Paint();
    }

    private static void add(int id) {
        Bitmap[] newBitmaps = new Bitmap[bitmaps.length+1];
        int[] newIds = new int[bitmaps.length+1];

        for (int i = 0; i < bitmaps.length; i++) {
            newBitmaps[i] = bitmaps[i];
            newIds[i] = ids[i];
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        newBitmaps[bitmaps.length] = BitmapFactory.decodeResource(MainActivity.app.resources, id, options);
        newIds[bitmaps.length] = id;

        bitmaps = newBitmaps;
        ids = newIds;
    }


    // getters
    public static Bitmap getBitmap(int id) {
        for (int i = 0; i < bitmaps.length; i++) {
            if (id == ids[i]) {
                return bitmaps[i];
            }
        }
        return null;
    }


    // misc
    public static void resize(int resId, int width, int height) {
        for (int i = 0; i < bitmaps.length; i++) {
            if (resId == ids[i]) {
                bitmaps[i] = Bitmap.createScaledBitmap(bitmaps[i], width, height, false);
                return;
            }
        }
    }

}

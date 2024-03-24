package com.xcorp.appx;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.xcorp.appx.gui.xPanel;
import com.xcorp.appx.objects.Sprites;
import com.xcorp.appx.objects.xRect;

import java.util.Random;

public class xApp extends SurfaceView implements SurfaceHolder.Callback {

    // public attributes
    public RelativeLayout layout;
    public Resources resources;
    public Context context;
    public Rect bounds;
    public xRect rect;
    public xAppThread thread;
    public xPanel[] panels = new xPanel[0];

    // delta time
    public float dt, APP_SPEED = 30;
    private double prevTime;

    public xApp(Context context) {
        super(context);
        layout = new RelativeLayout(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        layout.addView(this);

        setFocusable(true);
        thread = new xAppThread(this, surfaceHolder);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // if this ain't the first time that the surface is created.
        // meaning: the user switched activity and went back to this activity(or this game)
        if (resources != null) {
            surfaceHolder = getHolder();
            thread = new xAppThread(this, surfaceHolder);
            prevTime = System.currentTimeMillis();
            thread.startLoop();
            return;
        }

        // initialize public variables
        resources = getResources();
        context = getContext();
        bounds = getClipBounds();
        rect = new xRect(0, 0, getWidth(), getHeight());

        // initialize sprites
        Sprites.initialize();

        // initialize panels
        for (xPanel panel : panels) {
            panel.onCreate();
        }

        // start loop
        prevTime = System.currentTimeMillis();
        thread.startLoop();

        Log.d("debugNow", "I AM CREATED");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }


    public void changePanel(String panelName) {
        for (xPanel panel : panels) {
            if (panel.active && panel.visible) {
                panel.active = false;
                panel.visible = false;
            }
            else if (panel.name.equals(panelName)) {
                panel.active = true;
                panel.visible = true;
            }
        }
    }


    public void update() {
        // calculate delta time
        dt = (float)(System.currentTimeMillis() - prevTime) / 1000f;
        dt *= APP_SPEED;
        prevTime = System.currentTimeMillis();

        Log.d("FPS: ", "" + thread.getAverageFPS());

        // update panels
        for (xPanel panel : panels) {
            if (!panel.active) continue;
            panel.update();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (xPanel p : panels) p.handler.updateEvent(event);
        switch (event.getActionMasked()) {
            // events that are handled by my custom event handler
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // draw panels
        for (xPanel panel : panels) {
            if (!panel.visible) continue;
            panel.draw(canvas);
        }
    }


    public interface Callback {
        void call();
    }


    public static class Math {

        // geometry
        public static float dist(float[] a, float[] b) {
            return (float) java.lang.Math.sqrt(java.lang.Math.pow(b[0] - a[0], 2) + java.lang.Math.pow(b[1] - a[1], 2));
        }

        public static float dot(float[] a, float[] b) {
            if (a.length != 2 || b.length != 2) throw new IllegalArgumentException("vector size must be 2");
            return a[0]*b[0] + a[1]*b[1];
        }

        public static float magnitude(float[] v) {
            if (v.length != 2) throw new IllegalArgumentException("vector size must be 2");
            return (float) java.lang.Math.sqrt(v[0]*v[0] + v[1]*v[1]);
        }

        public static float angleBetweenVectors(float[] a, float[] b) {
            // b is the base vector relative to the x axis
            // angle 0 starts at 6 o'clock whereas angle 45 is at 10:30 o'clock
            // and angle -45 is at 7:30 o'clock
            return (float) java.lang.Math.atan2(b[1] - a[1], b[0] - a[0]);
        }

        public static float normalize(float value) {
            // returns the direction/sign of the value
            return value / java.lang.Math.abs(value);
        }


        // misc methods
        public static int randInt(int max) {
        /*
        Returns a pseudo random integer from 0 to max.
        Max is exclusive
        */
            Random rand = new Random();
            return rand.nextInt(max);
        }

        public static int randInt(int min, int max) {
        /*
        Returns a pseudo random integer from min to max.
        Max is exclusive
        */
            Random rand = new Random();
            return min + rand.nextInt(max-min);
        }

        public static int[] pos1Dto2D(int pos1D, int colSize2D) {

            int row = pos1D / colSize2D; // 5 <-- col
            int col = pos1D - row*colSize2D; // 5 <-- col

            return new int[] {row, col};
        }

        public static float[] pos1Dto2D(float pos1D, float colSize2D) {
            int[] temp = pos1Dto2D((int)pos1D, (int)colSize2D);

            return new float[] {temp[0], temp[1]};
        }

        public static int pos2Dto1D(int rowPos, int colPos, int colSize2D) {
            // this equation is derived from the equation above
            return colPos + rowPos * colSize2D;
        }

        public static float max(float a, float b) {
            return a > b ? a : b;
        }

        public static float min(float a, float b) {
            return a < b ? a : b;
        }
    }

    public static class Utils {
        public static String[] append(String item, String[] array) {
            String[] out = new String[array.length+1];

            for (int i = 0; i < array.length; i++) {
                out[i] = array[i];
            }

            out[out.length-1] = item;

            return out;
        }

        public static int[] append(int item, int[] array) {
            int[] out = new int[array.length+1];

            for (int i = 0; i < array.length; i++) {
                out[i] = array[i];
            }

            out[out.length-1] = item;

            return out;
        }

        public static float[] append(float item, float[] array) {
            float[] out = new float[array.length+1];

            for (int i = 0; i < array.length; i++) {
                out[i] = array[i];
            }

            out[out.length-1] = item;

            return out;
        }

        public static double[] append(double item, double[] array) {
            double[] out = new double[array.length+1];

            System.arraycopy(array, 0, out, 0, array.length);

            out[out.length-1] = item;

            return out;
        }
    }
}
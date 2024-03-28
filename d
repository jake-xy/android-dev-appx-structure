[33mcommit 0f2792c58c72e25680b1d160c1312fb6ffeff4b4[m[33m ([m[1;36mHEAD -> [m[1;32mmaster[m[33m, [m[1;31morigin/master[m[33m)[m
Author: jake-xy <fxyrus@gmail.com>
Date:   Thu Mar 28 11:12:18 2024 +0800

    xMessageBox
    added a gui compenent called message box
    shows custom pop up message
    can be closed by clicking outside the box

[1mdiff --git a/appx/gui/TouchHandler.java b/appx/gui/TouchHandler.java[m
[1mindex 709189c..4c14017 100644[m
[1m--- a/appx/gui/TouchHandler.java[m
[1m+++ b/appx/gui/TouchHandler.java[m
[36m@@ -56,7 +56,6 @@[m [mpublic class TouchHandler {[m
 [m
             case MotionEvent.ACTION_UP:[m
             case MotionEvent.ACTION_POINTER_UP:[m
[31m-                if (this.pointerID == pointerID) pointerID = -1;[m
                 prevPos = null;[m
                 break;[m
         }[m
[1mdiff --git a/appx/gui/xMessageBox.java b/appx/gui/xMessageBox.java[m
[1mnew file mode 100644[m
[1mindex 0000000..056604a[m
[1m--- /dev/null[m
[1m+++ b/appx/gui/xMessageBox.java[m
[36m@@ -0,0 +1,157 @@[m
[32m+[m[32mpackage com.xcorp.appx.gui;[m
[32m+[m
[32m+[m[32mimport android.annotation.SuppressLint;[m
[32m+[m[32mimport android.graphics.Bitmap;[m
[32m+[m[32mimport android.graphics.Canvas;[m
[32m+[m[32mimport android.graphics.Color;[m
[32m+[m[32mimport android.graphics.Paint;[m
[32m+[m[32mimport android.graphics.text.LineBreaker;[m
[32m+[m[32mimport android.text.method.ScrollingMovementMethod;[m
[32m+[m[32mimport android.util.Log;[m
[32m+[m[32mimport android.view.MotionEvent;[m
[32m+[m[32mimport android.view.View;[m
[32m+[m[32mimport android.widget.TextView;[m
[32m+[m
[32m+[m[32mimport com.xcorp.appx.objects.xFont;[m
[32m+[m[32mimport com.xcorp.appx.objects.xRect;[m
[32m+[m[32mimport com.xcorp.appxsample.R;[m
[32m+[m
[32m+[m[32m@SuppressLint("AppCompatCustomView")[m
[32m+[m[32mpublic class xMessageBox {[m
[32m+[m
[32m+[m[32m    private xPanel panel;[m
[32m+[m[32m    //general[m
[32m+[m[32m    private TextView textView;[m
[32m+[m[32m    public xRect rect;[m
[32m+[m[32m    public boolean active = true;[m
[32m+[m[32m    // ui[m
[32m+[m[32m    private String title;[m
[32m+[m[32m    public float padding = 0.11f;[m
[32m+[m[32m    public int bgColor, textColor;[m
[32m+[m[32m    private xFont titleFont;[m
[32m+[m[32m    private Bitmap titleBitmap;[m
[32m+[m[32m    private int touchID = -1;[m
[32m+[m
[32m+[m[32m    public xMessageBox(xPanel parentPanel, String title, float y, float w, float h) {[m
[32m+[m[32m        // constructor[m
[32m+[m[32m        this.title = title;[m
[32m+[m[32m        panel = parentPanel;[m
[32m+[m[32m        float x = panel.app.rect.centerX - w/2;[m
[32m+[m[32m        rect = new xRect(x, y, w, h);[m
[32m+[m[32m        // initializations[m
[32m+[m[32m        textView = new TextView(parentPanel.app.context);[m
[32m+[m[32m        bgColor = Color.rgb(255, 255, 255);[m
[32m+[m[32m        if (title != null) {[m
[32m+[m[32m            titleFont = new xFont(panel.app, R.font.arial, textView.getTextSize()*2f);[m
[32m+[m[32m            titleBitmap = titleFont.render(title, Color.BLACK);[m
[32m+[m[32m        }[m
[32m+[m[32m        // update textview pos and size[m
[32m+[m[32m        textView.setX(x + w*padding/2);[m
[32m+[m[32m        textView.setY(y + h*padding/2);[m
[32m+[m[32m        if (title != null) textView.setY(textView.getY() + titleBitmap.getHeight() + h*padding/2);[m
[32m+[m[32m        textView.setWidth( (int) (w - w*padding) );[m
[32m+[m[32m        textView.setHeight( (int) (rect.bot - textView.getY() - h*padding/2) );[m
[32m+[m[32m        // more textview initializations[m
[32m+[m[32m        textView.setFocusable(true);[m
[32m+[m[32m        textView.setMovementMethod(new ScrollingMovementMethod());[m
[32m+[m[32m        textView.setBreakStrategy(LineBreaker.BREAK_STRATEGY_BALANCED);[m
[32m+[m[32m        textView.setVisibility(panel.visible ? View.VISIBLE : View.INVISIBLE);[m
[32m+[m[32m        textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);[m
[32m+[m[32m        textView.setTypeface(panel.app.resources.getFont(R.font.arial));[m
[32m+[m[32m        panel.app.layout.addView(textView);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public xMessageBox(xPanel parentPanel, float y, float w, float h) {[m
[32m+[m[32m        this(parentPanel, null, y, w, h);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public xMessageBox(xPanel parentPanel, xRect rect) {[m
[32m+[m[32m        this(parentPanel, rect.y, rect.w, rect.h);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void setText(String text) {[m
[32m+[m[32m        textView.setText(text);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void setTextSize(float size) {[m
[32m+[m[32m        textView.setTextSize(size);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void setTextAlignment(int alignment) {[m
[32m+[m[32m        panel.app.mainActivity.runOnUiThread(new Runnable() {[m
[32m+[m[32m            @Override[m
[32m+[m[32m            public void run() {[m
[32m+[m[32m                textView.setTextAlignment(alignment);[m
[32m+[m[32m                textView.setWidth(textView.getWidth());[m
[32m+[m[32m            }[m
[32m+[m[32m        });[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void draw(Canvas canvas) {[m
[32m+[m[32m        if (!active) return;[m
[32m+[m[32m        Paint paint;[m
[32m+[m
[32m+[m[32m        // dim the background[m
[32m+[m[32m        paint = new Paint();[m
[32m+[m[32m        paint.setColor(Color.argb(100, 0, 0, 0));[m
[32m+[m[32m        panel.app.rect.draw(canvas, paint);[m
[32m+[m
[32m+[m[32m        // draw the container/background of the message box[m
[32m+[m[32m        paint = new Paint();[m
[32m+[m[32m        paint.setColor(bgColor);[m
[32m+[m[32m        canvas.drawRoundRect(rect.left, rect.top, rect.right, rect.bot, 25, 25, paint);[m
[32m+[m
[32m+[m[32m        // render the title[m
[32m+[m[32m        if (title != null) {[m
[32m+[m[32m            canvas.drawBitmap([m
[32m+[m[32m                titleBitmap,[m
[32m+[m[32m                rect.centerX - titleBitmap.getWidth()/2,[m
[32m+[m[32m                rect.top + rect.h*padding/2, null[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        // ensures that the textView's visibility is changed only once (save process time)[m
[32m+[m[32m        if (panel.visible && textView.getVisibility() == View.VISIBLE) return;[m
[32m+[m[32m        if (!panel.visible && textView.getVisibility() != View.VISIBLE) return;[m
[32m+[m
[32m+[m[32m        setTextViewVisibility(panel.visible ? true : false);[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void update(TouchHandler handler) {[m
[32m+[m[32m        if (!active) return;[m
[32m+[m
[32m+[m[32m        switch (handler.type) {[m
[32m+[m[32m            case MotionEvent.ACTION_DOWN:[m
[32m+[m[32m            case MotionEvent.ACTION_POINTER_DOWN:[m
[32m+[m[32m                if (touchID != -1) break;[m
[32m+[m[32m                touchID = handler.pointerID;[m
[32m+[m[32m                break;[m
[32m+[m
[32m+[m[32m            case MotionEvent.ACTION_UP:[m
[32m+[m[32m            case MotionEvent.ACTION_POINTER_UP:[m
[32m+[m[32m                if (touchID == handler.pointerID) {[m
[32m+[m[32m                    if (!rect.collides(handler.getPos(touchID))) {[m
[32m+[m[32m                        setActive(false);[m
[32m+[m[32m                    }[m
[32m+[m[32m                }[m
[32m+[m[32m                touchID = -1;[m
[32m+[m[32m                break;[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void setActive(boolean flag) {[m
[32m+[m[32m        setTextViewVisibility(flag);[m
[32m+[m[32m        this.active = flag;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    private void setTextViewVisibility(boolean flag) {[m
[32m+[m[32m        panel.app.mainActivity.runOnUiThread(new Runnable() {[m
[32m+[m[32m            @Override[m
[32m+[m[32m            public void run() {[m
[32m+[m[32m                textView.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);[m
[32m+[m[32m                textView.setWidth(textView.getWidth());[m
[32m+[m[32m            }[m
[32m+[m[32m        });[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
[32m+[m
[1mdiff --git a/appx/gui/xPanel.java b/appx/gui/xPanel.java[m
[1mindex fbd462e..afbc2dd 100644[m
[1m--- a/appx/gui/xPanel.java[m
[1m+++ b/appx/gui/xPanel.java[m
[36m@@ -3,6 +3,7 @@[m [mpackage com.xcorp.appx.gui;[m
 import android.content.Context;[m
 import android.graphics.Canvas;[m
 import android.view.View;[m
[32m+[m[32mimport android.widget.RelativeLayout;[m
 [m
 import com.xcorp.appx.objects.xRect;[m
 import com.xcorp.appx.xApp;[m
[36m@@ -30,6 +31,7 @@[m [mpublic abstract class xPanel extends View {[m
 [m
     // overrideable methods[m
     public void onCreate() {[m
[32m+[m[32m        // event handler for this panel[m
         handler = new TouchHandler(this);[m
     }[m
 [m
[1mdiff --git a/appx/objects/Sprites.java b/appx/objects/Sprites.java[m
[1mindex 58bbcbc..5a6fbc0 100644[m
[1m--- a/appx/objects/Sprites.java[m
[1m+++ b/appx/objects/Sprites.java[m
[36m@@ -1,9 +1,16 @@[m
 package com.xcorp.appx.objects;[m
 [m
[32m+[m[32mimport android.content.Context;[m
 import android.graphics.Bitmap;[m
 import android.graphics.BitmapFactory;[m
[32m+[m[32mimport android.graphics.Canvas;[m
 import android.graphics.Color;[m
 import android.graphics.Paint;[m
[32m+[m[32mimport android.graphics.drawable.BitmapDrawable;[m
[32m+[m[32mimport android.graphics.drawable.Drawable;[m
[32m+[m[32mimport android.graphics.drawable.VectorDrawable;[m
[32m+[m
[32m+[m[32mimport androidx.core.content.ContextCompat;[m
 [m
 import com.xcorp.appxsample.MainActivity;[m
 import com.xcorp.appxsample.R;[m
[36m@@ -59,7 +66,6 @@[m [mpublic class Sprites {[m
     }[m
 [m
 [m
[31m-[m
     // getters[m
     public static Bitmap getBitmap(int id) {[m
         for (int i = 0; i < bitmaps.length; i++) {[m
[1mdiff --git a/appx/objects/xRect.java b/appx/objects/xRect.java[m
[1mindex 244d1ac..c9fd63f 100644[m
[1m--- a/appx/objects/xRect.java[m
[1m+++ b/appx/objects/xRect.java[m
[36m@@ -18,11 +18,11 @@[m [mpublic class xRect extends View {[m
     public float left, top, right, bot; // square-like attributes[m
     public float[] topLeft = new float[2], botRight = new float[2];[m
     public float centerX, centerY;[m
[31m-    private xPanel parentPanel;[m
[32m+[m[32m    private xPanel panel;[m
 [m
[31m-    public xRect(float x, float y, float w, float h, xPanel panel) {[m
[32m+[m[32m    public xRect(float x, float y, float w, float h, xPanel parentPanel) {[m
         this(x, y, w, h);[m
[31m-        this.parentPanel = panel;[m
[32m+[m[32m        this.panel = parentPanel;[m
     }[m
 [m
     public xRect(float x, float y, float w, float h) {[m
[36m@@ -213,7 +213,7 @@[m [mpublic class xRect extends View {[m
     }[m
 [m
     public xRect copy() {[m
[31m-        return new xRect(this.x, this.y, this.w, this.h, parentPanel);[m
[32m+[m[32m        return new xRect(this.x, this.y, this.w, this.h, panel);[m
     }[m
 [m
     public static xRect[] append(xRect item, xRect[] array) {[m
[1mdiff --git a/appx/xApp.java b/appx/xApp.java[m
[1mindex 73dada2..befe01e 100644[m
[1m--- a/appx/xApp.java[m
[1m+++ b/appx/xApp.java[m
[36m@@ -11,6 +11,7 @@[m [mimport android.view.SurfaceView;[m
 import android.widget.RelativeLayout;[m
 [m
 import androidx.annotation.NonNull;[m
[32m+[m[32mimport androidx.appcompat.app.AppCompatActivity;[m
 [m
 import com.xcorp.appx.gui.xPanel;[m
 import com.xcorp.appx.objects.Sprites;[m
[36m@@ -21,6 +22,7 @@[m [mimport java.util.Random;[m
 public class xApp extends SurfaceView implements SurfaceHolder.Callback {[m
 [m
     // public attributes[m
[32m+[m[32m    public AppCompatActivity mainActivity;[m
     public RelativeLayout layout;[m
     public Resources resources;[m
     public Context context;[m
[36m@@ -33,9 +35,11 @@[m [mpublic class xApp extends SurfaceView implements SurfaceHolder.Callback {[m
     public float dt, APP_SPEED = 30;[m
     private double prevTime;[m
 [m
[31m-    public xApp(Context context) {[m
[31m-        super(context);[m
[31m-        layout = new RelativeLayout(context);[m
[32m+[m[32m    public xApp(AppCompatActivity mainActivity) {[m
[32m+[m[32m        super(mainActivity);[m
[32m+[m[32m        this.mainActivity = mainActivity;[m
[32m+[m
[32m+[m[32m        layout = new RelativeLayout(mainActivity);[m
 [m
         SurfaceHolder surfaceHolder = getHolder();[m
         surfaceHolder.addCallback(this);[m
[1mdiff --git a/appxsample/MainPanel.java b/appxsample/MainPanel.java[m
[1mindex dadb4df..2dc0056 100644[m
[1m--- a/appxsample/MainPanel.java[m
[1m+++ b/appxsample/MainPanel.java[m
[36m@@ -55,12 +55,12 @@[m [mpublic class MainPanel extends xPanel {[m
 [m
         // background[m
         Paint paint = new Paint();[m
[31m-        paint.setColor(Color.BLUE);[m
[32m+[m[32m        paint.setColor(Color.GREEN);[m
[32m+[m[32m        app.rect.draw(canvas, paint);[m
[32m+[m
         paint.setStyle(Paint.Style.STROKE);[m
         paint.setStrokeWidth(20);[m
[31m-[m
[31m-        if (rect.topLeft == app.rect.topLeft) paint.setColor(Color.RED);[m
[31m-[m
[32m+[m[32m        paint.setColor(Color.BLUE);[m
         canvas.drawRect(rect.left, rect.top, rect.right, rect.bot, paint);[m
     }[m
 }[m
[1mdiff --git a/appxsample/SubPanel.java b/appxsample/SubPanel.java[m
[1mindex ae5952d..84e241e 100644[m
[1m--- a/appxsample/SubPanel.java[m
[1m+++ b/appxsample/SubPanel.java[m
[36m@@ -5,15 +5,13 @@[m [mimport android.graphics.Bitmap;[m
 import android.graphics.Canvas;[m
 import android.graphics.Color;[m
 import android.graphics.Paint;[m
[31m-import android.graphics.Rect;[m
[31m-import android.graphics.text.LineBreaker;[m
[31m-import android.text.method.MovementMethod;[m
[31m-import android.text.method.ScrollingMovementMethod;[m
 import android.util.Log;[m
[32m+[m[32mimport android.view.KeyEvent;[m
 import android.view.MotionEvent;[m
[31m-import android.widget.TextView;[m
[32m+[m[32mimport android.view.View;[m
 [m
 import com.xcorp.appx.gui.xPanel;[m
[32m+[m[32mimport com.xcorp.appx.gui.xMessageBox;[m
 import com.xcorp.appx.objects.Sprites;[m
 import com.xcorp.appx.objects.xFont;[m
 import com.xcorp.appx.objects.xRect;[m
[36m@@ -24,7 +22,7 @@[m [mpublic class SubPanel extends xPanel {[m
     int pointerID = -1;[m
     xRect rect;[m
     xFont font;[m
[31m-    TextView textView;[m
[32m+[m[32m    xMessageBox messageBox;[m
 [m
     public SubPanel(xApp app, Context context) {[m
         super("subPanel", app, context);[m
[36m@@ -36,25 +34,17 @@[m [mpublic class SubPanel extends xPanel {[m
         rect = new xRect(20, 20, 100, 100, this);[m
         rect.x = app.rect.centerX - rect.w/2;[m
         font = new xFont(app, R.font.arial, 90);[m
[31m-        textView = new TextView(app.context);[m
[31m-        textView.setFocusable(true);[m
[32m+[m[32m        messageBox = new xMessageBox(this,"Hi love", app.rect.h*0.15f, 600, 1200);[m
[32m+[m[32m        messageBox.setTextSize(24);[m
         String t = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like
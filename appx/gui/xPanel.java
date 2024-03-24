package com.xcorp.appx.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.xcorp.appx.objects.xRect;
import com.xcorp.appx.xApp;

public abstract class xPanel extends View {

    public xApp app;
    public String name;
    public TouchHandler handler;

    public boolean active = false, visible = false;

    public xPanel(String name, xApp app, Context context) {
        super(context);
        this.name = name;
        this.app = app;

        xPanel[] out = new xPanel[app.panels.length+1];

        System.arraycopy(app.panels, 0, out, 0, app.panels.length);
        out[out.length-1] = this;

        app.panels = out;
    }

    // overrideable methods
    public void onCreate() {
        handler = new TouchHandler(this);
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        super.draw(canvas);


    }



}

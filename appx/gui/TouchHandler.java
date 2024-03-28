package com.xcorp.appx.gui;

import android.view.MotionEvent;

public class TouchHandler {

    public int type = -1, pointerID;
    public MotionEvent event;

    public float[] prevPos = null;
    public float scrollVel = 0;

    public TouchHandler(xPanel parentPanel) {
        // add this handler to the parent panel
        parentPanel.handler = this;
    }

    // for positions with pointer ids
    public float getX(int pointerID) {
        return event.getX(pointerID);
    }

    public float getY(int pointerID) {
        return event.getY(pointerID);
    }

    public float[] getPos(int pointerID) {
        return new float[] {getX(pointerID), getY(pointerID)};
    }

    // for positions without pointer ids (i.e., for ACTION_MOVE events)
    public float getX() { return event.getX(); }
    public float getY() { return event.getY(); }
    public float[] getPos() { return new float[] {getX(), getY()}; }

    // setters
    public void updateEvent(MotionEvent event) {
        this.event = event;
        type = event.getActionMasked();
        pointerID = event.getPointerId(event.getActionIndex());
    }

    public void updateScrollListener() {
        switch (this.type) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                prevPos = this.getPos();
                scrollVel = this.getY() - prevPos[1];
                break;

            case MotionEvent.ACTION_MOVE:
                if (prevPos == null) prevPos = this.getPos();
                scrollVel = this.getY() - prevPos[1];
                prevPos = this.getPos();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                prevPos = null;
                break;
        }

        if (prevPos == null && (int)scrollVel != 0) {
            scrollVel *= 0.95;
        }
    }
}

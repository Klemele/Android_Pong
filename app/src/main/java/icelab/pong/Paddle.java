package icelab.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Paddle {

    private float right;
    private float top;
    private float left;
    private float bottom;

    private float width;
    private float height;

    private Paint paint;

    public Paddle() {}

    public Paddle(float r, float t, float l, float b, float w, float h, Paint p) {
        setRight(r);
        setTop(t);
        setLeft(l);
        setBottom(b);
        setHeight(h);
        setWidth(w);
        setPaint(p);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), getPaint());
    }

    public void move(float x, float y) {
        setRight(x + getWidth());
        setTop(y - getHeight());
        setLeft(x - getWidth());
        setBottom(y + getHeight());
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}

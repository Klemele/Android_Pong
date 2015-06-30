package icelab.pong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Ball {
    private float circleX;
    private float circleY;

    private float radius;

    private Paint paint;

    private Bitmap image;

    private boolean right;
    private boolean top;

    public Ball() {}

    public Ball(float x, float y, float r, Paint p) {
        setCircleX(x);
        setCircleY(y);
        setRadius(r);
        setPaint(p);
    }

    public Ball(float x, float y, float r, Paint p, Bitmap img) {
        setCircleX(x);
        setCircleY(y);
        setRadius(r);
        setPaint(p);
        setImage(img);
    }

    public void draw(Canvas canvas) {
        if (getImage() != null) {
            canvas.drawBitmap(getImage(), getCircleX(), getCircleY(), getPaint());
        }
        else {
            canvas.drawCircle(getCircleX(), getCircleY(), getRadius(), getPaint());
        }
    }

    public void move(float speedX, float speedY, int maxX, int maxY) {
        if (getImage() != null) {
            // movement on X axis
            if (isRight() && getCircleX() + getRadius() < maxX) {
                setCircleX(getCircleX() + speedX);
            } else {
                setRight(false);
            }
            if (!isRight() && getCircleX() > 0) {
                setCircleX(getCircleX() - speedX);
            } else {
                setRight(true);
            }

            // movement on Y axis
            if (!isTop()) {
                setCircleY(getCircleY() + speedY);
            } else {
                setTop(true);
            }
            if (isTop() && getCircleY() > 0) {
                setCircleY(getCircleY() - speedY);
            } else {
                setTop(false);
            }
        }
        else {
            // movement on X axis
            if (isRight() && getCircleX() + getRadius() < maxX) {
                setCircleX(getCircleX() + speedX);
            } else {
                setRight(false);
            }
            if (!isRight() && getCircleX() > getRadius()) {
                Log.d("ball radius", String.valueOf(getRadius()));
                Log.d("ball x pos ", String.valueOf(getCircleX()));
                setCircleX(getCircleX() - speedX);
            } else {
                setRight(true);
            }

            // movement on Y axis
            if (!isTop()) {
                setCircleY(getCircleY() + speedY);
            } else {
                setTop(true);
            }
            if (isTop() && getCircleY() > getRadius()) {
                setCircleY(getCircleY() - speedY);
            } else {
                setTop(false);
            }
        }
    }

    public float getCircleX() {
        return circleX;
    }

    public void setCircleX(float circleX) {
        this.circleX = circleX;
    }

    public float getCircleY() {
        return circleY;
    }

    public void setCircleY(float circleY) {
        this.circleY = circleY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

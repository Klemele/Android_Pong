package icelab.pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;


public class PongView extends View implements View.OnTouchListener {
    private final int SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
    private final int SCREEN_HEIGHT = getContext().getResources().getDisplayMetrics().heightPixels;
    private int NAVBAR_HEIGHT;

    private Ball ball;
    private Paddle paddle;

    private float speedX;
    private float speedY;

    private float fingerX;

    private Bitmap background;

    private boolean gameOver;
    private long startCountdown;

    public PongView(Context context) {
        super(context);

        int nav_id = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        NAVBAR_HEIGHT = nav_id == 0 ? 0 : getResources().getDimensionPixelSize(nav_id);

        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.tenniscourt);
        background = Bitmap.createScaledBitmap(bg, SCREEN_WIDTH, SCREEN_HEIGHT, true);

        speedX = 10;
        speedY = 10;

        this.setOnTouchListener(this);

        init();
    }

    public void init() {
        gameOver = false;

//        Paint paintBall = new Paint();
//        paintBall.setColor(Color.RED);
        Paint paintBall = new Paint();
        paintBall.setStyle(Paint.Style.STROKE);
        paintBall.setStrokeWidth(10);
        paintBall.setColor(Color.RED);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.tennisball_small, options);
        int imageHeight = options.outHeight;

        Paint paintPaddle = new Paint();
        paintPaddle.setStyle(Paint.Style.STROKE);
        paintPaddle.setStrokeWidth(10);
        paintPaddle.setColor(Color.BLUE);

        ball = new Ball((float) SCREEN_WIDTH / 2, (float) SCREEN_HEIGHT / 2, imageHeight, paintBall, img);
        paddle = new Paddle(fingerX + 100, (SCREEN_HEIGHT - NAVBAR_HEIGHT) - 100, fingerX - 100, (SCREEN_HEIGHT - NAVBAR_HEIGHT) + 50, 100, 20, paintPaddle);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, new Paint());

        ball.draw(canvas);
        ball.move(speedX, speedY, SCREEN_WIDTH, SCREEN_HEIGHT);

        paddle.draw(canvas);
        paddle.move(fingerX, (SCREEN_HEIGHT - NAVBAR_HEIGHT));

        if (!gameOver) {
            try {
                collision(canvas, getContext(), ball, paddle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (System.currentTimeMillis() - startCountdown > 2000) {
                try {
                    gameOver(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        invalidate();
    }

    public void collision(Canvas canvas, Context context, Ball ball, Paddle paddle) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {
        if (ball.getCircleY() + ball.getRadius() >= paddle.getTop()) {
            if (ball.getCircleX() + ball.getRadius() >= paddle.getLeft() &&
                    ball.getCircleX() - ball.getRadius() <= paddle.getRight()) {
                ball.setTop(true);
                ball.setRight(!ball.isRight());
            }
            else {
                startCountdown = System.currentTimeMillis();
                gameOver = true;
            }
        }
    }

    public void gameOver(Context context) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {
        //Make sound
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(context, soundUri);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }

        //Vibrate
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        //Reset
        init();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        fingerX = event.getX();

        return true;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }
}

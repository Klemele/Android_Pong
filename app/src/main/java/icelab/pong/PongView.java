package icelab.pong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
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

    private Bitmap imgBackground;

    private boolean gameOver;
    private long startCountdown;

    private int score;

    private int lastAmplitude;
    private int thresholdAmplitude;

    private  SoundMeter soundMeter;

    public PongView(Context context) {
        super(context);

        // Get Navigation bar height if exist in order to subtract it from the screen size
        int nav_id = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        NAVBAR_HEIGHT = nav_id == 0 ? 0 : getResources().getDimensionPixelSize(nav_id);

        // Get background image and resize it to fit screen
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.tenniscourt);
        setImgBackground(Bitmap.createScaledBitmap(bg, SCREEN_WIDTH, SCREEN_HEIGHT, true));
        setBackground(new BitmapDrawable(getResources(), getImgBackground()) );

        // Set touch listener
        this.setOnTouchListener(this);

        setThresholdAmplitude(5000);

        // Launch init method
        init();
    }

    public void init() {
        // Initialize boolean for game over method
        setGameOver(false);

        //init soundMeter
        setSoundMeter(new SoundMeter());
        getSoundMeter().start();

        // Initialize score
        setScore(0);

        // Set speed of ball
        setSpeedX(10);
        setSpeedY(10);

       /* If you do not want to use image, uncomment line 96 and comment line 85-87, 97 */
        Paint paintBall = new Paint();
        paintBall.setColor(Color.RED);
        paintBall.setStyle(Paint.Style.STROKE);
        paintBall.setStrokeWidth(10);
        paintBall.setColor(Color.RED);

        // Get ball image
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.tennisball_small, options);
        int imageHeight = options.outHeight;

        // Design of the paddle
        Paint paintPaddle = new Paint();
        paintPaddle.setStyle(Paint.Style.STROKE);
        paintPaddle.setStrokeWidth(10);
        paintPaddle.setColor(Color.BLUE);

        // Create Ball object
        /*setBall(new Ball((float) SCREEN_WIDTH / 2, (float) SCREEN_HEIGHT / 2, 50, paintBall));*/
        setBall(new Ball((float) SCREEN_WIDTH / 2, (float) SCREEN_HEIGHT / 2, imageHeight, paintBall, img));
        // Create Paddle object
        setPaddle(new Paddle(getFingerX() + 100, (SCREEN_HEIGHT - NAVBAR_HEIGHT) - 100, getFingerX() - 100, (SCREEN_HEIGHT - NAVBAR_HEIGHT) + 50, 100, 20, paintPaddle));

        setFingerX(0);
    }

    public void stop() {
        // Initialize boolean for game over method
        setGameOver(false);

        // Set speed of ball
        setSpeedX(0);
        setSpeedY(0);

        //Stop soundMeter
        getSoundMeter().stop();
    }

    public void onDraw(Canvas canvas) {
        int currentAmplitude = getSoundMeter().getAmplitude();

        Log.d("currentAmplitude", String.valueOf(currentAmplitude));
        Log.d("lasttAmplitude", String.valueOf(getLastAmplitude()));

        if(currentAmplitude > getThresholdAmplitude()) {
            if(currentAmplitude > getLastAmplitude()) {
                setFingerX(getFingerX() + 50);
            }
            else {
                setFingerX(getFingerX() - 50);
            }
        }
        else {
            setFingerX(getFingerX() - 50);
        }
        setLastAmplitude(currentAmplitude);

        //Max value of fingerX
        if (getFingerX() < 0) {
            setFingerX(0);
        }
        else if (getFingerX() > SCREEN_WIDTH) {
            setFingerX(SCREEN_WIDTH);
        }
        Log.d("getFingerX()", String.valueOf(getFingerX()));


        // Draw and move the ball
        getBall().draw(canvas);
        getBall().move(getSpeedX(), getSpeedY(), SCREEN_WIDTH, SCREEN_HEIGHT);

        // Draw and move the paddle
        getPaddle().draw(canvas);
        getPaddle().move(getFingerX(), (SCREEN_HEIGHT - NAVBAR_HEIGHT));

        if (!isGameOver()) {
            try {
                // Launch collision method
                collision(getBall(), getPaddle());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            //Show game over message
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("GAME OVER !", (float) SCREEN_WIDTH / 10, (float) SCREEN_HEIGHT / 2, paint);

            // Launch game over method after 2 seconds
            if (System.currentTimeMillis() - getStartCountdown() > 2000) {
                try {
                    gameOver(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        invalidate();
    }

    public void collision(Ball ball, Paddle paddle) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {
        if (ball.getCircleY() + ball.getRadius() >= paddle.getTop()) {
            if (ball.getCircleX() + ball.getRadius() >= paddle.getLeft() &&
                    ball.getCircleX() - ball.getRadius() <= paddle.getRight()) {
                // Ball touch the paddle, rebound
                ball.setTop(true);
                ball.setRight(!ball.isRight());
                setScore(getScore() + 1);
            }
            else {
                // Ball didn't touch the paddle, game over
                setStartCountdown(System.currentTimeMillis());
                setGameOver(true);
            }
        }
    }

    public void gameOver(Context context) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {
        // Make sound
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

        // Vibrate
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        // Stop the game
        stop();

        // Call Highscore activity
        Intent next = new Intent(context, Highscore.class);
        next.putExtra("SCORE", getScore());
        context.startActivity(next);

        // Reset
        init();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Set X value of the finger
        //setFingerX(event.getX());

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

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public float getFingerX() {
        return fingerX;
    }

    public void setFingerX(float fingerX) {
        this.fingerX = fingerX;
    }

    public Bitmap getImgBackground() {
        return imgBackground;
    }

    public void setImgBackground(Bitmap imgBackground) {
        this.imgBackground = imgBackground;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public long getStartCountdown() {
        return startCountdown;
    }

    public void setStartCountdown(long startCountdown) {
        this.startCountdown = startCountdown;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLastAmplitude() {
        return lastAmplitude;
    }

    public void setLastAmplitude(int lastAmplitude) {
        this.lastAmplitude = lastAmplitude;
    }

    public SoundMeter getSoundMeter() {
        return soundMeter;
    }

    public void setSoundMeter(SoundMeter soundMeter) {
        this.soundMeter = soundMeter;
    }

    public int getThresholdAmplitude() {
        return thresholdAmplitude;
    }

    public void setThresholdAmplitude(int thresholdAmplitude) {
        this.thresholdAmplitude = thresholdAmplitude;
    }
}

package io.github.alexeychurchill.raycast.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Joystick widget
 */

public class JoystickView extends View {
    // Default size constraints
    private static final int DEFAULT_WIDTH = 150; // in dp
    private static final int DEFAULT_HEIGHT = 150; // in dp

    private OnJoystickStartTrackingListener mStartTrackingListener;
    private OnJoystickChangeListener mChangeListener;
    private OnJoystickStopTrackingListener mStopTrackingListener;

    private Paint mPaint = new Paint();
    // Colors
    private int mBaseColor = Color.argb(63, 31, 31, 31);
    private int mStrokeColor = Color.argb(191, 31, 31, 31);
    private int mStrokeForceColor = Color.argb(191, 0, 63, 15);
    private int mThumbColor = Color.argb(191, 191, 191, 191);

    // Size constraints
    private float mWorkingArea = 0.8f;
    private float mThumbSize = 0.4f;

    private boolean mActive = false;
    private boolean mForced = false;
    private float mX = 0.0f;
    private float mY = 0.0f;

    public JoystickView(Context context) {
        super(context);
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStartTrackingListener(OnJoystickStartTrackingListener startTrackingListener) {
        this.mStartTrackingListener = startTrackingListener;
    }

    public void setChangeListener(OnJoystickChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public void setStopTrackingListener(OnJoystickStopTrackingListener stopTrackingListener) {
        this.mStopTrackingListener = stopTrackingListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTracking(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                move(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                stopTracking(event.getX(), event.getY());
                invalidate();
                break;
        }
        return true; //super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int width = dpToPx(DEFAULT_WIDTH);
        int height = dpToPx(DEFAULT_HEIGHT);
        // Width measurement
        if (widthMeasureMode == MeasureSpec.EXACTLY) {
            width = measureWidth;
        } else {
            if (widthMeasureMode == MeasureSpec.AT_MOST) {
                width = Math.min(measureWidth, width);
            }
        }
        // Height measurement
        if (heightMeasureMode == MeasureSpec.EXACTLY) {
            height = measureHeight;
        } else {
            if (heightMeasureMode == MeasureSpec.AT_MOST) {
                height = Math.min(measureHeight, width);
            }
        }
        // Measured :)
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBase(canvas);
        if (mActive) {
            drawThumb(canvas);
        }
    }

    private void drawBase(Canvas canvas) {
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        // Shapes' calculations
        float totalRadius = size() / 2.0f;
        float strokeWidth = totalRadius * (1.0f - mWorkingArea);
        // Stroke
        mPaint.setColor((mForced) ? mStrokeForceColor : mStrokeColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(width / 2.0f, height / 2.0f, totalRadius - strokeWidth / 2.0f, mPaint);
        // Base
        mPaint.setColor(mBaseColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2.0f, height / 2.0f, totalRadius - strokeWidth, mPaint);
    }

    private void drawThumb(Canvas canvas) {
        float totalRadius = size() / 2.0f;
        float thumbRadius = totalRadius * mWorkingArea * mThumbSize;
        float posX = (mX + 1.0f) / 2.0f * (getMeasuredWidth() - thumbRadius * 2.0f) + thumbRadius;
        float posY = (mY + 1.0f) / 2.0f * (getMeasuredHeight() - thumbRadius * 2.0f) + thumbRadius;
        mPaint.setColor(mThumbColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(posX, posY, thumbRadius, mPaint);
    }

    private int size() {
        return Math.min(getMeasuredWidth(), getMeasuredHeight());
    }

    private void startTracking(float x, float y) {
        mActive = true;
        if (mStartTrackingListener != null) {
            mStartTrackingListener.onJoystickStartTracking();
        }
        setPosition(x, y);
    }

    private void move(float x, float y) {
        setPosition(x, y);
    }

    private void stopTracking(float x, float y) {
        mActive = false;
        setPosition(x, y);
        mForced = false;
        if (mStopTrackingListener != null) {
            mStopTrackingListener.onJoystickStopTracking();
        }
    }

    private void setPosition(float x, float y) {
        mX = 2.0f * x / getMeasuredWidth() - 1.0f;
        mY = 2.0f * y / getMeasuredHeight() - 1.0f;
        double radius = Math.sqrt(mX * mX + mY * mY);
        double cos = mX / radius;
        double sin = mY / radius;
        mForced = (radius >= 1.3);
        if (radius > 1.0) {
            radius = 1.0;
        }
        mX = (float) (cos * radius);
        mY = (float) (sin * radius);
        if (mChangeListener != null) {
            mChangeListener.onJoystickChange(mX, -mY);
        }
    }

    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public interface OnJoystickStartTrackingListener {
        void onJoystickStartTracking();
    }

    public interface OnJoystickChangeListener {
        void onJoystickChange(float xValue, float yValue);
    }

    public interface OnJoystickStopTrackingListener {
        void onJoystickStopTracking();
    }


}

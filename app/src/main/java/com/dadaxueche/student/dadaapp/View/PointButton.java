package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.dadaxueche.student.dadaapp.R;

/**
 * Created by wpf on 8-28-0028.
 */
public class PointButton extends View {
    private Paint mCenterPaint;
    private Paint mProgressPaint;
    private Paint mProgressPaintOther;
    private Paint mProgressPaint2;
    private Paint mProgressPaintOther2;
    private Paint mPointPaint;
    private Paint mPointPaint2;
    private TextPaint mTextPaint;

    private int mCenterColor = Color.WHITE;
    private int mProgressColor = Color.RED;
    private int mProgressColorOther = Color.GRAY;
    private int mProgressColor2 = Color.RED;
    private int mProgressColorOther2 = Color.GRAY;

    private int mTextColor = getResources().getColor(R.color.Blue);

    private int mMinRadius = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private double mProgress = 0;
    private double mProgress2 = 0;
    private int mProgressRadius = 0;
    private int mTextSize = 32;
    private CharSequence mMessage = "";
    private boolean isProgress1;
    private boolean isProgress2;

    public PointButton(Context context) {
        super(context);
    }

    public PointButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mCenterPaint = new Paint();
        mCenterPaint.setColor(mCenterColor);
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setTextSize(32);
        mCenterPaint.setStyle(Paint.Style.FILL);

        mPointPaint = new Paint();
        mPointPaint.setColor(mProgressColor);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setTextSize(32);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeWidth(mProgressRadius);

        mPointPaint2 = new Paint();
        mPointPaint2.setColor(mProgressColor2);
        mPointPaint2.setAntiAlias(true);
        mPointPaint2.setTextSize(32);
        mPointPaint2.setStyle(Paint.Style.FILL);
        mPointPaint2.setStrokeWidth(mProgressRadius);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeWidth(2);

        mProgressPaintOther = new Paint();
        mProgressPaintOther.setColor(mProgressColorOther);
        mProgressPaintOther.setAntiAlias(true);
        mProgressPaintOther.setStyle(Paint.Style.FILL);
        mProgressPaintOther.setStrokeWidth(2);

        mProgressPaint2 = new Paint();
        mProgressPaint2.setColor(mProgressColor2);
        mProgressPaint2.setAntiAlias(true);
        mProgressPaint2.setStyle(Paint.Style.FILL);
        mProgressPaint2.setStrokeWidth(2);

        mProgressPaintOther2 = new Paint();
        mProgressPaintOther2.setColor(mProgressColorOther2);
        mProgressPaintOther2.setAntiAlias(true);
        mProgressPaintOther2.setStyle(Paint.Style.FILL);
        mProgressPaintOther2.setStrokeWidth(2);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMinRadius = (widthMeasureSpec <= heightMeasureSpec) ? widthMeasureSpec : heightMeasureSpec;
        super.onMeasure(mMinRadius, mMinRadius);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mMinRadius = mWidth /10*9/2;
        mProgressRadius = mMinRadius / 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isProgress1)
            drawProgress(canvas);
        if(isProgress2)
            drawProgress2(canvas);
        drawCenterCircle(canvas);
        drawCenterText(canvas);
    }

    private void drawProgress(Canvas canvas) {
        RectF rectF = new RectF(mWidth/2-mMinRadius,
                mHeight/2-mMinRadius,
                mWidth/2+mMinRadius,
                mHeight/2+mMinRadius);
        canvas.drawArc(rectF, (int) (mProgress * 360), 360, true, mProgressPaintOther);
        canvas.drawArc(rectF, -90, (int) (mProgress * 360), true, mProgressPaint);
        drawTwoPoint(canvas);
    }

    private void drawProgress2(Canvas canvas) {
        RectF rectF = new RectF(mWidth/2-mMinRadius,
                mHeight/2-mMinRadius,
                mWidth/2+mMinRadius,
                mHeight/2+mMinRadius);
        canvas.drawArc(rectF, (int) (mProgress2 * 360), 360, true, mProgressPaintOther2);
        canvas.drawArc(rectF, -90, (int) (mProgress2 * 360), true, mProgressPaint2);
        drawTwoPoint2(canvas);
    }

    private void drawTwoPoint(Canvas canvas) {
        Point point = new Point(mWidth / 2,
                mHeight / 2 - mMinRadius + mProgressRadius / 2);
        if(mProgress != 0 && isProgress1) {
            canvas.drawCircle(point.x, point.y, mProgressRadius / 2, mPointPaint);
            getRotatePoint(point, (int) ((1 - mProgress) * 360));
            canvas.drawCircle(point.x, point.y, mProgressRadius / 2, mPointPaint);
        }
    }

    private void drawTwoPoint2(Canvas canvas) {
        Point point = new Point(mWidth / 2,
                mHeight / 2 - mMinRadius + mProgressRadius / 2);
        if(mProgress2 != 0 && isProgress2) {
            canvas.drawCircle(point.x, point.y, mProgressRadius / 2, mPointPaint2);
            getRotatePoint(point, (int) ((1 - mProgress2) * 360));
            canvas.drawCircle(point.x, point.y, mProgressRadius / 2, mPointPaint2);
        }
    }
    private void drawCenterText(Canvas canvas) {
        //StaticLayout layout = new StaticLayout(mMessage,mTextPaint,240, Layout.Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
        canvas.drawText(mMessage.toString(), mWidth / 2, mHeight / 2 + mTextSize / 2, mTextPaint);
    }

    private void drawCenterCircle(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, mMinRadius - mProgressRadius, mCenterPaint);
    }

    public void setProgress1(double mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    public void setProgress2(double mProgress) {
        this.mProgress2 = mProgress;
        invalidate();
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        initPaint();
    }

    public CharSequence getmMessage() {
        return mMessage;
    }

    public void setMinRadius(int minRadius) {
        mMinRadius = minRadius;
        invalidate();
    }

    private void getRotatePoint(Point point,int degress) {
        point.set(
                (int) ((point.x - mWidth / 2) * Math.cos(Math.toRadians(degress)) + (point.y - mHeight / 2) * Math.sin(Math.toRadians(degress)) + mWidth / 2),
                (int) ((point.x - mWidth / 2) * Math.sin(Math.toRadians(degress)) + (point.y - mHeight / 2) * Math.cos(Math.toRadians(degress)) + mHeight / 2)
        );
    }

    public void setmMessage(CharSequence mMessage) {
        this.mMessage = mMessage;
        invalidate();
    }

    public void setmCenterColor(int mCenterColor) {
        this.mCenterColor = mCenterColor;
        initPaint();
    }

    public void setIsProgress(boolean isProgress) {
        this.isProgress1 = isProgress;
        invalidate();
    }

    public void setIsProgress2(boolean isProgress) {
        this.isProgress2 = isProgress;
        invalidate();
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        initPaint();
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
        initPaint();
    }

    public void setProgressColor2(int mProgressColor) {
        this.mProgressColor2 = mProgressColor;
        initPaint();
    }
}

package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.dadaxueche.student.dadaapp.R;

/**
 * Created by wpf on 10-6-0006.
 */
public class MyProgress extends View {

    private Paint mPaint_ProgressBar = new Paint();
    private Paint mPaint_ProgressBar_No = new Paint();
    private int mProgress = 0;
    private int mProgressMax = 100;
    private int mHeight_ProgressBar = 50;
    private int mColor_ProgressBar = getResources().getColor(R.color.colorProgressBar);
    private int mColor_ProgressBar_No = getResources().getColor(R.color.colorProgressBar_No);


    public MyProgress(Context context) {
        super(context);
        initPaint();
    }

    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint_ProgressBar.setColor(mColor_ProgressBar);
        mPaint_ProgressBar.setAntiAlias(true);
        mPaint_ProgressBar.setStyle(Paint.Style.FILL);

        mPaint_ProgressBar_No.setColor(mColor_ProgressBar_No);
        mPaint_ProgressBar_No.setAntiAlias(true);
        mPaint_ProgressBar_No.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getLayoutParams().height = mHeight_ProgressBar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 0, 0, mPaint_ProgressBar_No);
            canvas.drawRoundRect(0, 0, getWidth() * ((float) mProgress / mProgressMax), getHeight(), 0, 0, mPaint_ProgressBar);
        } else {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint_ProgressBar_No);
            canvas.drawRect(0, 0, getWidth() * ((float) mProgress / mProgressMax), getHeight(), mPaint_ProgressBar);
        }
    }

    public void setmColor_ProgressBar(int mColor_ProgressBar) {
        this.mColor_ProgressBar = mColor_ProgressBar;
    }

    public void setmHeight_ProgressBar(int mHeight_ProgressBar) {
        this.mHeight_ProgressBar = mHeight_ProgressBar;
    }

    public void setmPaint_ProgressBar(Paint mPaint_ProgressBar) {
        this.mPaint_ProgressBar = mPaint_ProgressBar;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    public void setmProgressMax(int mProgressMax) {
        this.mProgressMax = mProgressMax;
    }
}

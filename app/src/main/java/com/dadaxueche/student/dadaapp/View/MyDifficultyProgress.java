package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.dadaxueche.student.dadaapp.R;

/**
 * Created by wpf on 9-23-0023.
 */
public class MyDifficultyProgress extends View {

    private Paint mPaint_Fill;
    private Paint mPaint_Empty;

    private int mDifficultyProgress = 0;
    private float scale = 0.9f;
    private int mWidth = 0,mHeight = 0;
    private int mColor = getResources().getColor(R.color.DeepOrange);
    private float mRadius = 0;

    public MyDifficultyProgress(Context context) {
        super(context);
    }

    public MyDifficultyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint_Fill = new Paint();
        mPaint_Fill.setAntiAlias(true);
        mPaint_Fill.setColor(mColor);
        mPaint_Fill.setStyle(Paint.Style.FILL);
        mPaint_Fill.setStrokeWidth(1);

        mPaint_Empty = new Paint();
        mPaint_Empty.setAntiAlias(true);
        mPaint_Empty.setColor(mColor);
        mPaint_Empty.setStyle(Paint.Style.STROKE);
        mPaint_Empty.setStrokeWidth(1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mWidth == 0) {
            mWidth = (int)(getWidth()*scale);
            mHeight = (int)(getHeight()*scale);
            mRadius = mHeight/2*scale;
        }
        drawCircleProgress(canvas);
    }



    private void drawFillCircleProgress(int i,Canvas canvas) {
        canvas.drawCircle((i*2 + 1) * (mWidth / 10),
                mHeight / 2,
                mRadius,
                mPaint_Fill);
    }

    private void drawEmptyCircleProgress(int i,Canvas canvas) {
        canvas.drawCircle((i*2 + 1) * (mWidth / 10),
                mHeight / 2,
                mRadius,
                mPaint_Empty);
    }

    private void drawCircleProgress(Canvas canvas) {
        for(int i=0;i<5;++i) {
            if(i<mDifficultyProgress)
                drawFillCircleProgress(i,canvas);
            else {
                drawEmptyCircleProgress(i,canvas);
            }
        }
    }

    public void setDifficultyProgress(int difficultyProgress) {
        this.mDifficultyProgress = difficultyProgress;
    }
}

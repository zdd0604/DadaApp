package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.dadaxueche.student.dadaapp.R;


/**
 * Created by wpf on 8-7-0007.
 */
public class MyView extends View {

    private Paint mPaint;
    private int mLength = 0;
    private int mWidth,mHeight;
    private int mRaius = 0;
    private int mCurrent = 0;
    private int minRadius = 10;
    private int mCurrentRadius = 15;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.Orange));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        int portion = 3;

        for(int i=0;i<mLength;++i) {
            if(i == mCurrent) {
                mRaius = mCurrentRadius;
            } else {
                mRaius = minRadius;
            }
            canvas.drawCircle((i*2 + 1) * (mWidth / portion / mLength / 2)+ mWidth / portion,
                    mHeight / 2,
                    mRaius,
                    mPaint);
        }
    }

    public void setLength(int mLength) {
        this.mLength = mLength;
        invalidate();
    }

    public void setCurrent(int mCurrent) {
        this.mCurrent = mCurrent;
        invalidate();
    }
}

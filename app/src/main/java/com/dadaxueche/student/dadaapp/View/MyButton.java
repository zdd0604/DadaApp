package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wpf on 9-10-0010.
 */
public class MyButton extends LinearLayout {

    private ImageView mImageView;
    private TextView mTextView;
    private int mTextSize = 16;
    private int mOrientation = VERTICAL;
    private boolean mIsSquare;

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageView = new ImageView(context);
        mTextView = new TextView(context);
        mTextView.setSingleLine();
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mIsSquare) {
            int min = widthMeasureSpec <= heightMeasureSpec ? widthMeasureSpec : heightMeasureSpec;
            super.onMeasure(min, min);
        }
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView() {
        removeAllViews();
        addView(mImageView);
        mTextView.setTextSize(mTextSize);
        addView(mTextView);
        setOrientation(mOrientation);
        setGravity(Gravity.CENTER);
    }

    public void setImageView(int id) {
        this.mImageView.setImageResource(id);
    }

    public void setText(String title) {
        this.mTextView.setText(title);
    }

    public void setTextViewColor(int color) {
        mTextView.setTextColor(color);
    }

    public String getText() {
        return this.mTextView.getText().toString();
    }

    public void setLayoutParams(LayoutParams mLayoutParams) {
        mImageView.setLayoutParams(mLayoutParams);
    }

    public void setIsSquare(boolean mIsSquare) {
        this.mIsSquare = mIsSquare;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        initView();
    }
}

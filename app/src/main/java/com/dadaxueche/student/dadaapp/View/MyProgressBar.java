package com.dadaxueche.student.dadaapp.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by wpf on 10-7-0007.
 */
public class MyProgressBar extends LinearLayout {


    private int mProgress = 0;
    private int mProgressMax = 100;
    private int mHeight_ProgressBar = 50;
    private TextView mTextView_Percent = new TextView(getContext());
    private TextView mTextView_Size = new TextView(getContext());
    private MyProgress myProgress = new MyProgress(getContext());
    private DecimalFormat df_Progress = (DecimalFormat) NumberFormat.getInstance();
    private DecimalFormat df_Size = (DecimalFormat) NumberFormat.getInstance();
    private String[] DownloadSizeUnit = new String[]{"B", "KB", "MB"};

    public MyProgressBar(Context context) {
        super(context);

    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        df_Progress.applyPattern("#%");
        df_Size.applyPattern("#.##");
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 32;
        layoutParams.rightMargin = 32;
        layoutParams.topMargin = 16;
        setLayoutParams(layoutParams);
        myProgress.setLayoutParams(layoutParams);
        setOrientation(VERTICAL);

        addView(myProgress);

        mTextView_Percent.setText(df_Progress.format(mProgress));
        setmTextView_Size();
        mTextView_Size.setGravity(Gravity.RIGHT);

        mTextView_Size.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(HORIZONTAL);

        linearLayout.addView(mTextView_Percent);
        linearLayout.addView(mTextView_Size);
        addView(linearLayout);
    }

    private int getSizeUnit(double DownSize) {
        double size = DownSize;
        int i = 1;
        while ((size /= 1024) > 1024) {
            i++;
        }
        return i;
    }

    private double getUnitSize(double DownSize) {
        double size = DownSize;
        while ((size /= 1024) > 1024) ;
        return size;
    }

    @SuppressLint("SetTextI18n")
    private void setmTextView_Size() {
        mTextView_Size.setText(df_Size.format(getUnitSize(mProgress)) + DownloadSizeUnit[getSizeUnit(mProgress)] + " / " +
                df_Size.format(getUnitSize(mProgressMax)) + DownloadSizeUnit[getSizeUnit(mProgressMax)]);
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        myProgress.setmProgress(mProgress);
        mTextView_Percent.setText(df_Progress.format(((double) mProgress / mProgressMax)));
        setmTextView_Size();
    }

    public void setmProgressMax(int mProgressMax) {
        this.mProgressMax = mProgressMax;
        myProgress.setmProgressMax(mProgressMax);

    }

    public void setmHeight_ProgressBar(int mHeight_ProgressBar) {
        this.mHeight_ProgressBar = mHeight_ProgressBar;
    }

}

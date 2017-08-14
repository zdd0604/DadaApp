package com.dadaxueche.student.dadaapp.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class FullScreenVideoView extends VideoView {
    int oldwidtht, oldheight;

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        width = height * oldwidtht / oldheight;
        setMeasuredDimension(width, height);
    }

    public void setWidthandHeight(int a, int b) {
        this.oldwidtht = a;
        this.oldheight = b;
    }

}

package com.dadaxueche.student.dadaapp.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2015-10-09
 * Time: 16:06
 */
public class BaseGridView extends GridView {
    public BaseGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseGridView(Context context) {
        super(context);
    }

    public BaseGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
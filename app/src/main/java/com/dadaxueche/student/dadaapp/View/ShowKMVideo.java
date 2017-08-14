package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dadaxueche.student.dadaapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by wpf on 9-21-0021.
 */
public class ShowKMVideo extends LinearLayout implements
        View.OnClickListener {

    //设置一行显示几个
    private int lineNumber = 2;

    private RoundedImageView mImageView;
    private TextView mTextView_VideoName;
    private TextView mTextView_VideoInfo;
    private TextView mTextView_Hot;

    private OnItemClick onItemClick;

    public ShowKMVideo(Context context) {
        super(context);
    }

    public ShowKMVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    /*
    String[] 0:标题 1:描述信息 2:图片网址 3:热点
     */
    public void setVideoList(List<String[]> videoList) {
        int  i = 0;
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        for(String[] strings : videoList) {
            if(i%(lineNumber)<=lineNumber) {
                linearLayout.addView(getKMVideoView(i,strings));
            }
            if(i>0 && (i+1)%2 == 0) {
                addView(linearLayout);
                linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
            }
            if(i == videoList.size() - 1) {
                addView(linearLayout);
            }
            i++;
        }
    }

    private View getKMVideoView(int position,String[] strings) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.km2video,null,false);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        mImageView = (RoundedImageView) view.findViewById(R.id.imageView_km2Video);
        mTextView_VideoName = (TextView) view.findViewById(R.id.textView_VideoName);
        mTextView_VideoInfo = (TextView) view.findViewById(R.id.textView_VideoInfo);
        mTextView_Hot = (TextView) view.findViewById(R.id.textView_hot);

        mTextView_VideoName.setText(strings[0]);
        mTextView_VideoInfo.setText(strings[1]);
        //mImageView.setImageURI(Uri.parse(strings[2]));
        mImageView.setImageResource(R.mipmap.km2_qxxs);
        if("热门".equals(strings[3])) {
            mTextView_Hot.setVisibility(VISIBLE);
        } else {
            mTextView_Hot.setVisibility(GONE);
        }
        view.setTag(R.id.item_id,position);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        onItemClick.onItemClick((int)v.getTag(R.id.item_id));
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }
}

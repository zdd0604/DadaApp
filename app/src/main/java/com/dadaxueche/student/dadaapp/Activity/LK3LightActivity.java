package com.dadaxueche.student.dadaapp.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dadaxueche.student.dadaapp.Adapter.KM3LightViewPagerAdapter;
import com.dadaxueche.student.dadaapp.Fragment.LightFragment;
import com.dadaxueche.student.dadaapp.Fragment.VoiceFragment;
import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class LK3LightActivity extends FragmentActivity implements
        ViewPager.OnPageChangeListener ,
        View.OnClickListener {

    private TextView mTextView_Title;
    private Button mButton_light,mButton_voice;
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lk3_light);

        mTextView_Title = (TextView) findViewById(R.id.actionbar_Title);
        findViewById(R.id.imageView_Logo).setVisibility(View.GONE);
        mTextView_Title.setText("灯光语音");

        mViewPager = (ViewPager) findViewById(R.id.viewpager_light);

        mButton_light = (Button) findViewById(R.id.light);
        mButton_voice = (Button) findViewById(R.id.voice);

        mButton_light.setOnClickListener(this);
        mButton_voice.setOnClickListener(this);

        findViewById(R.id.voice).setOnClickListener(this);
        findViewById(R.id.imageButton_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragments.add(new LightFragment());
        fragments.add(new VoiceFragment());

        mViewPager.setAdapter(new KM3LightViewPagerAdapter(getSupportFragmentManager(), fragments));
        buttonSelect(mButton_light);
        mViewPager.addOnPageChangeListener(this);

    }

    private void buttonSelect(Button button) {
        switch (button.getId()) {
            case R.id.light:
                button.setTextColor(Color.WHITE);
                button.setBackgroundColor(getResources().getColor(R.color.color_exam_main_beiti));
                mButton_voice.setTextColor(getResources().getColor(R.color.color_exam_main_beiti));
                mButton_voice.setBackgroundColor(Color.WHITE);
                break;
            case R.id.voice:
                button.setTextColor(Color.WHITE);
                button.setBackgroundColor(getResources().getColor(R.color.color_exam_main_beiti));
                mButton_light.setTextColor(getResources().getColor(R.color.color_exam_main_beiti));
                mButton_light.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.light:
                mViewPager.setCurrentItem(0);
                buttonSelect(mButton_light);
                break;
            case R.id.voice:
                mViewPager.setCurrentItem(1);
                buttonSelect(mButton_voice);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0) {
            buttonSelect(mButton_light);
        } else if(position == 1) {
            buttonSelect(mButton_voice);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
package com.dadaxueche.student.dadaapp.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdongdong on 2015/9/10.
 */
public class FragmentExam extends Fragment  implements
        View.OnClickListener ,
        ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private List<CharSequence> Titles = new ArrayList<>();
    private List<Button> buttons = new ArrayList<>();
    private int buttonDefaultColor = Color.GRAY;
    private int buttonSelectColor;
    private MyDataBase mDataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataBase = new MyDataBase(getActivity());
        mDataBase.open();
        buttonSelectColor = getResources().getColor(R.color.DeepOrange);

        Titles.add("科一");
        Titles.add("科二");
        Titles.add("科三");
        Titles.add("科四");
        Titles.add("拿本");

        buttons.add((Button) view.findViewById(R.id.button_KM1));
        buttons.add((Button) view.findViewById(R.id.button_KM2));
        buttons.add((Button) view.findViewById(R.id.button_KM3));
        buttons.add((Button) view.findViewById(R.id.button_KM4));
        buttons.add((Button) view.findViewById(R.id.button_NB));
        initButtons();
        buttons.get(0).setTextColor(buttonSelectColor);
        fragments.add(new KM1Fragment());
        fragments.add(new KM2Fragment());
        fragments.add(new KM3Fragment());
        fragments.add(new KM4Fragment());
        fragments.add(new NBFragment());

        mViewPager = (ViewPager) view.findViewById(R.id.viewPager_Main);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager()));
        mViewPager.addOnPageChangeListener(this);
    }

    private void initButtons() {
        for(int i = 0;i<buttons.size();++i) {
            buttons.get(i).setOnClickListener(this);
            buttons.get(i).setText(Titles.get(i));
            buttons.get(i).setTextColor(buttonDefaultColor);
        }
    }

    public void setPosition(int position){
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<buttons.size();++i) {
            if(i==position) {
                buttons.get(i).setTextColor(buttonSelectColor);
            } else {
                buttons.get(i).setTextColor(buttonDefaultColor);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles.get(position);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_KM1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.button_KM2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.button_KM3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.button_KM4:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.button_NB:
                mViewPager.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragments.get(0).onActivityResult(requestCode,resultCode,data);
        fragments.get(3).onActivityResult(requestCode,resultCode,data);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("exam"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("exam");
    }
}
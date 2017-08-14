package com.dadaxueche.student.dadaapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dadaxueche.student.dadaapp.Fragment.MainViewPagerImageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdongdong on 2015/10/10.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<MainViewPagerImageFragment> imagePagerList = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm,List<MainViewPagerImageFragment> urlList) {
        super(fm);
        this.imagePagerList = urlList;
    }

    @Override
    public Fragment getItem(int position) {
        return imagePagerList.get(position);
    }

    @Override
    public int getCount() {
        return imagePagerList.size();
    }
}

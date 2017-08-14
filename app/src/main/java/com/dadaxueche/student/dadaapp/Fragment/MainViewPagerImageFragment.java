package com.dadaxueche.student.dadaapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dadaxueche.student.dadaapp.Activity.Dada_User_falvTk;
import com.dadaxueche.student.dadaapp.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zhangdongdong on 2015/10/10.
 */
public class MainViewPagerImageFragment extends Fragment implements View.OnClickListener {


    private String url;
    private String imageUrl;
    private String titleName;

    private SimpleDraweeView simpleDraweeView;

    public MainViewPagerImageFragment() {
    }

    @SuppressLint("ValidFragment")
    public MainViewPagerImageFragment(String imageUrl, String url, String titleName) {
        this.imageUrl = imageUrl;
        this.url = url;
        this.titleName = titleName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mainimagepager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.image_pager);
        if (imageUrl != null && imageUrl.trim().length() > 0) {
            simpleDraweeView.setImageURI(Uri.parse(imageUrl));
        } else {
            simpleDraweeView.setImageResource(R.mipmap.a2);
        }
        simpleDraweeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_pager:
                if (titleName != null && titleName.trim().length() > 0 && url != null && url.trim().length() > 0) {
                    intentActivity(Dada_User_falvTk.class, url, titleName);
                }
                break;
        }
    }

    private void intentActivity(Class c, String webUrl, String toobarbname) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("webUrl", webUrl);
        intent.putExtra("toobarbname", toobarbname);
        startActivity(intent);
    }
}

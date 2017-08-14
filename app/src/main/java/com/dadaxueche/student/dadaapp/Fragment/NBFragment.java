package com.dadaxueche.student.dadaapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dadaxueche.student.dadaapp.Activity.NbPublicActivity;
import com.dadaxueche.student.dadaapp.R;


public class NBFragment extends Fragment implements View.OnClickListener {

    private String str = "http://www.dadaxueche.com/m/nb/";
    private Intent intent;
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nb, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.nb1).setOnClickListener(this);
        view.findViewById(R.id.nb2).setOnClickListener(this);
        view.findViewById(R.id.nb3).setOnClickListener(this);
        view.findViewById(R.id.nb4).setOnClickListener(this);
        view.findViewById(R.id.nb5).setOnClickListener(this);
        view.findViewById(R.id.nb6).setOnClickListener(this);
        view.findViewById(R.id.nb7).setOnClickListener(this);
        view.findViewById(R.id.nb8).setOnClickListener(this);
        view.findViewById(R.id.nb9).setOnClickListener(this);
        view.findViewById(R.id.nb10).setOnClickListener(this);
        view.findViewById(R.id.nb11).setOnClickListener(this);
        view.findViewById(R.id.nb12).setOnClickListener(this);
        view.findViewById(R.id.nb13).setOnClickListener(this);
        view.findViewById(R.id.nb14).setOnClickListener(this);
        view.findViewById(R.id.nb15).setOnClickListener(this);
        view.findViewById(R.id.nb16).setOnClickListener(this);
        view.findViewById(R.id.nb17).setOnClickListener(this);
        intent = new Intent(getActivity(), NbPublicActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nb1:
                path = str + "nj.html";
                startIntent(path, "驾照年审、年检");
                break;
            case R.id.nb2:
                path = str + "gh.html";
                startIntent(path, "驾照过期更换");
                break;
            case R.id.nb3:
                path = str + "gs.html";
                startIntent(path, "驾照遗失");
                break;
            case R.id.nb4:
                path = str + "gs-1.html";
                startIntent(path, "驾照挂失");
                break;
            case R.id.nb5:
                path = str + "czdq.html";
                startIntent(path, "车辆操作大全");
                break;
            case R.id.nb6:
                path = str + "scjq.html";
                startIntent(path, "刹车技巧");
                break;
            case R.id.nb7:
                path = str + "cljq.html";
                startIntent(path, "事故处理技巧");
                break;
            case R.id.nb8:
                path = str + "jxbd.html";
                startIntent(path, "特殊天气驾驶宝典");
                break;
            case R.id.nb9:
                path = str + "yjxs.html";
                startIntent(path, "夜间行驶必备");
                break;
            case R.id.nb10:
                path = str + "jbtc.html";
                startIntent(path, "基本停车");
                break;
            case R.id.nb11:
                path = str + "cztc.html";
                startIntent(path, "垂直式停车位");
                break;
            case R.id.nb12:
                path = str + "cftc.html";
                startIntent(path, "侧方停车位");
                break;
            case R.id.nb13:
                path = str + "xxtc.html";
                startIntent(path, "斜线停车位");
                break;
            case R.id.nb14:
                path = str + "clstc.html";
                startIntent(path, "串联式停车位");
                break;
            case R.id.nb15:
                path = str + "blstc.html";
                startIntent(path, "并联式停车位");
                break;
            case R.id.nb16:
                path = str + "dxtcktc.html";
                startIntent(path, "地下停车库停车位");
                break;
            case R.id.nb17:
                path = str + "fzxtc.html";
                startIntent(path, "非字形车位");
                break;
        }
    }

    void startIntent(String str, String content) {
        intent.putExtra("url", str);
        intent.putExtra("content", content);
        startActivity(intent);
    }
}

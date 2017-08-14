package com.dadaxueche.student.dadaapp.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dadaxueche.student.dadaapp.Adapter.Light_Voice_Bean;
import com.dadaxueche.student.dadaapp.Adapter.VoiceAdapter;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.MyMediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class VoiceFragment extends Fragment implements VoiceAdapter.OnItemClick {
    private String[] str = new String[]{"上车准备","起步","变更车道","超越车辆","加减档操作","会车项目","前方路口调头","路口右转","路口左转","通过汽车站","通过人行道","通过学校","直行路口","直线行驶","靠边停车"};
    private int[] raw_voice = new int[]{R.raw.vsc, R.raw.vqd,R.raw.vbgcd, R.raw.lcc, R.raw.vjjd, R.raw.vhcxm, R.raw.vlkdt, R.raw.vlkyz, R.raw.vlkzz, R.raw.vtgqcz, R.raw.vtgrxd, R.raw.vtgxx,  R.raw.vzxlk, R.raw.vzxxs, R.raw.vkbtc};
    private int[] img_voice = new int[]{R.mipmap.shangche,R.mipmap.qidong,R.mipmap.genbianchedao,R.mipmap.chaoche,R.mipmap.jiajian,R.mipmap.huiche,R.mipmap.diaotou,R.mipmap.youzhuan,R.mipmap.zuozhuan,R.mipmap.tongguogongjiaozhan,R.mipmap.tongguorenxinhengdao,
            R.mipmap.tongguoxuexiao,R.mipmap.zhixin,R.mipmap.zhixin,R.mipmap.kaobiantingche};

    private RecyclerView mRecyclerView;
    private List<Light_Voice_Bean> list_voice = new ArrayList<>();
    private MyMediaPlayer mediaPlayer;
    private boolean[] select = new boolean[str.length];
    private VoiceAdapter voiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voice,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_voice);

        int i =0 ;
        for(String s : str) {
            Light_Voice_Bean bean = new Light_Voice_Bean();
            bean.setImg(img_voice[i]);
            bean.setText(s);
            i++;
            list_voice.add(bean);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        voiceAdapter = new VoiceAdapter(list_voice,select);
        voiceAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(voiceAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }


    private void setSelect(int[] positions) {
        clearSelect();
        for(int position : positions) {
            select[position] = true;
        }
    }

    private void clearSelect() {
        select = new boolean[str.length];
    }

    @Override
    public void onItemClick(int position) {
        setSelect(new int[]{position});
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        mediaPlayer = MyMediaPlayer.create(getActivity(), raw_voice[position]);
        mediaPlayer.start();
        MyMediaPlayer.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                clearSelect();
                voiceAdapter.setSelects(select);
                voiceAdapter.notifyDataSetChanged();
                mp.release();
                mediaPlayer = null;
            }
        });
        voiceAdapter.setSelects(select);
        voiceAdapter.notifyDataSetChanged();
    }
}

package com.dadaxueche.student.dadaapp.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dadaxueche.student.dadaapp.Adapter.LightAdapter;
import com.dadaxueche.student.dadaapp.Adapter.Light_Voice_Bean;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.MyMediaPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LightFragment extends Fragment implements LightAdapter.OnItemClick {

    private RecyclerView mRecyclerView;
    private LightAdapter lightAdapter;
    private ArrayList<Light_Voice_Bean> list_light = new ArrayList<>();
    private String[] str = new String[]{"照明不良的道路驾驶", "调头", "路口转弯", "通过公交站", "拱桥", "通过急弯", "无信号灯控制的路口", "坡路", "通过人行横道", "通过学校区域", "打开近光灯", "打开远光灯", "雾天行驶", "夜间发生故障", "超车", "会车","模拟考试"};
    private int[] raw_light = new int[]{R.raw.lzmbl, R.raw.ldt, R.raw.lkzw, R.raw.lgjz, R.raw.lgq, R.raw.ljw, R.raw.lxhd, R.raw.lpl, R.raw.lrxhd, R.raw.lxxqy, R.raw.ljgd, R.raw.lygd, R.raw.lwtxs, R.raw.lyjsz, R.raw.lcc, R.raw.vhcxm,R.raw.lkaitou};
    private int[] img_light = new int[]{R.mipmap.yejiandengguang, R.mipmap.diaotou, R.mipmap.zuozhuan, R.mipmap.tongguogongjiaozhan, R.mipmap.gongqiao, R.mipmap.jiwan,
            R.mipmap.wuxinhaodengkongzhilukou, R.mipmap.podao, R.mipmap.tongguorenxinhengdao, R.mipmap.tongguoxuexiao, R.mipmap.jinguang, R.mipmap.yuanguang, R.mipmap.wutianxingshi, R.mipmap.yejianfashengguzhang, R.mipmap.chaoche, R.mipmap.huiche,R.mipmap.suijilianxi0};
    private MyMediaPlayer mediaPlayer;
    private List<Integer> list = new ArrayList<>();
    private boolean[] select = new boolean[str.length];

    private boolean isPlay,isExam, isReturn;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1 && !isReturn) {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                mediaPlayer = MyMediaPlayer.create(getActivity(), msg.arg1);
                mediaPlayer.start();
                final int num = msg.arg2;
//                if(isExam)
//                    setSelect(new int[]{img_light.length -1,num});
//                lightAdapter.setSelects(select);
//                mRecyclerView.scrollToPosition(num);
//                lightAdapter.notifyDataSetChanged();
                MyMediaPlayer.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(!isExam)
                            clearSelect();
//                        else
//                            setSelect(new int[]{img_light.length -1});
                        lightAdapter.setSelects(select);
                        lightAdapter.notifyDataSetChanged();
                        mp.release();
                        mediaPlayer = null;
                        isPlay = false;
                    }
                });
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        isReturn = true;
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_light);
        int i =0 ;
        for(String s : str) {
            Light_Voice_Bean bean = new Light_Voice_Bean();
            bean.setImg(img_light[i]);
            bean.setText(s);
            i++;
            list_light.add(bean);
            if(i < raw_light.length -1)
                list.add(i);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lightAdapter = new LightAdapter(list_light,select);

        lightAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(lightAdapter);
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
    public void onItemClick(View v,int position) {
        setSelect(new int[]{position});
        if(position == str.length -1) {
            isExam = true;
            Collections.shuffle(list);
            list = list.subList(0,4);
            list.add(0,position);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    for(final Integer integer : list) {
                        if(!isExam)
                            break;
                        //setSelect(new int[]{list.get(0),integer});
                        //setSelect(new int[]{list.get(0)});
                        if(i==4) {
                            isExam = false;
                        }
                        Message message2 = handler.obtainMessage();
                        message2.what = 1;
                        message2.arg1 = raw_light[integer];
                        message2.arg2 = integer;
                        handler.sendMessage(message2);
                        isPlay = true;
                        long time1 = System.currentTimeMillis();
                        while (isExam && isPlay && (System.currentTimeMillis() - time1) < 5000) {
                            if(!isExam)
                                break;
                        }
                        if(!isExam)
                            break;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;

                    }
                }
            }).start();
        } else {
            Message message = handler.obtainMessage();
            message.what = 1;
            message.arg1 = raw_light[position];
            message.arg2 = position;
            handler.sendMessage(message);
            isExam = false;
        }
        lightAdapter.setSelects(select);
        lightAdapter.notifyDataSetChanged();
    }

}

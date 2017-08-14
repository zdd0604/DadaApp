package com.dadaxueche.student.dadaapp.Fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.GetData;
import com.dada.mylibrary.Gson.K2K3VideoList;
import com.dada.mylibrary.Gson.KM3ContentList;
import com.dada.mylibrary.Gson.KM3Video;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.Activity.DownKmVideoActivity;
import com.dadaxueche.student.dadaapp.Activity.H5Activity;
import com.dadaxueche.student.dadaapp.Activity.LK3LightActivity;
import com.dadaxueche.student.dadaapp.Adapter.K2K3VideoListAdapter;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.BaseGridView;
import com.dadaxueche.student.dadaapp.Util.ReceiveMsgService;
import com.dadaxueche.student.dadaapp.Util.ResultEvent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class KM3Fragment extends Fragment implements View.OnClickListener,
        GetData.GetResultCallBack {

    private WebView webView_kmjj;
    private KM3Video km3Video;
    private KM3ContentList km3list;
    private TextView title1, time1, title2, time2, title3, time3;
    private GetData mGetData = new GetData();
    private ArrayList<K2K3VideoList> KMVideoData;
    private String[] url = new String[3];
    private BaseGridView gridView;
    private K2K3VideoListAdapter adapter;
    private K2K3VideoList k2k3list;
    private SharedPreferences down_state;
    private Check check;
    private MyDataBase dataBase = new MyDataBase();
    private Cursor cursor;
    private ImageView neterror_img;
    private ScrollView sc;
    private ReceiveMsgService receiveMsgService;
    private boolean conncetState = true; // 记录当前连接状态，因为广播会接收所有的网络状态改变wifi/3g等等，所以需要一个标志记录当前状态

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 已连接
                    update();
                    break;
                case 2:// 未连接
                    neterror_img.setVisibility(View.VISIBLE);
                    webView_kmjj.setVisibility(View.GONE);
                    setDataByDatabase();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_km3, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        down_state = getActivity().getSharedPreferences("downstate",
                Activity.MODE_PRIVATE);
        check = new Check(getActivity());
        KMVideoData = new ArrayList<>();
        cursor = dataBase.queryVideoList("3");
        EventBus.getDefault().register(this);
        bind();
        neterror_img = (ImageView) view.findViewById(R.id.neterror_img);
        neterror_img.setOnClickListener(this);
        gridView = (BaseGridView) view.findViewById(R.id.k3gridview);
        gridView.setOnItemClickListener(onItemClickListener);
        webView_kmjj = (WebView) view.findViewById(R.id.webView_kmjj3);
        sc = (ScrollView) view.findViewById(R.id.scrollView_km3);
        update();
        view.findViewById(R.id.showallBtn).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_1).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_2).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_3).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_4).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_5).setOnClickListener(this);
        view.findViewById(R.id.k3content1).setOnClickListener(this);
        view.findViewById(R.id.k3content2).setOnClickListener(this);
        view.findViewById(R.id.k3content3).setOnClickListener(this);
        title1 = (TextView) view.findViewById(R.id.textView_km_kskq_1_title1);
        title2 = (TextView) view.findViewById(R.id.textView_km_kskq_2_title2);
        title3 = (TextView) view.findViewById(R.id.textView_km_kskq_3_title3);
        time1 = (TextView) view.findViewById(R.id.textView_km_kskq_1_time1);
        time2 = (TextView) view.findViewById(R.id.textView_km_kskq_2_time2);
        time3 = (TextView) view.findViewById(R.id.textView_km_kskq_3_time3);

    }

    void update() {
        if (check.isConnectingToInternet()) {
            mGetData.setGetResultCallBack(this);
            mGetData.getKM3ContentList();
            neterror_img.setVisibility(View.GONE);
            webView_kmjj.setVisibility(View.VISIBLE);
            webView_kmjj.loadUrl(DadaUrlPath.KESAN_N);
            webView_kmjj.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP)
                        sc.requestDisallowInterceptTouchEvent(false);
                    else
                        sc.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        } else {
            neterror_img.setVisibility(View.VISIBLE);
            webView_kmjj.setVisibility(View.GONE);
            setDataByDatabase();
        }
    }

    private void bind() {
        Intent intent = new Intent(getActivity(), ReceiveMsgService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            receiveMsgService = ((ReceiveMsgService.MyBinder) service)
                    .getService();
            receiveMsgService.setOnGetConnectState(new ReceiveMsgService.GetConnectState() { // 添加接口实例获取连接状态
                @Override// 添加接口实例获取连接状态
                public void GetState(boolean isConnected, boolean isWifi) {
                    if (conncetState != isConnected) { // 如果当前连接状态与广播服务返回的状态不同才进行通知显示
                        conncetState = isConnected;
                        if (conncetState) {// 已连接
                            handler.sendEmptyMessage(1);
                        } else {// 未连接
                            handler.sendEmptyMessage(2);
                        }
                    }
                }
            });
        }
    };

    private void unbind() {
        if (receiveMsgService != null) {
            getActivity().unbindService(serviceConnection);
        }
    }

    /**
     * 没网情况下查询数据库
     */
    private void setDataByDatabase() {
        KMVideoData.clear();
        cursor = dataBase.queryVideoList("3");
        while (cursor.moveToNext()) {
            k2k3list = new K2K3VideoList();
            k2k3list.setName(cursor.getString(1));
            k2k3list.setSize(cursor.getString(2));
            k2k3list.setUrl(cursor.getString(3));
            k2k3list.setIshot(cursor.getString(4));
            k2k3list.setPhotourl(cursor.getString(5));
            k2k3list.setDetail(cursor.getString(6));
            String[] ps = cursor.getString(3).split("/");
            String fileName = ps[ps.length - 1];
            if (Check.CheckFile(DadaUrlPath.SDCARD_VIDEO, fileName)) {
                if (down_state.getBoolean(fileName, false)) {
                    k2k3list.setIsdown(true);
                } else {
                    k2k3list.setIsdown(false);
                }
            } else {
                k2k3list.setIsdown(false);
            }
            KMVideoData.add(k2k3list);
        }
        cursor.close();
        updateUI();
    }

    private void getKMVideoData() {
        if (cursor.getCount() > 0) {
            if (dataBase.deleteVideoList("3")) {
                insertDataBase();
            }
        } else {
            insertDataBase();
        }
    }

    /**
     * 插入数据库
     */
    private void insertDataBase() {
        KMVideoData.clear();
        for (KM3Video.RowsEntity km3row : this.km3Video.getRows()) {
            k2k3list = new K2K3VideoList();
            k2k3list.setName(km3row.getName());
            k2k3list.setSize(km3row.getTime() + "  " + km3row.getSize() + "MB");
            k2k3list.setUrl(km3row.getUrl());
            if (km3row.getIsHot() == 1)
                k2k3list.setIshot("热门");
            else
                k2k3list.setIshot("");
            k2k3list.setPhotourl(km3row.getPhoto());
            k2k3list.setDetail(km3row.getDetail());
            String[] ps = km3row.getUrl().split("/");
            String fileName = ps[ps.length - 1];
            if (Check.CheckFile(DadaUrlPath.SDCARD_VIDEO, fileName)) {
                if (down_state.getBoolean(fileName, false)) {
                    k2k3list.setIsdown(true);
                } else {
                    k2k3list.setIsdown(false);
                }
            } else {
                k2k3list.setIsdown(false);
            }
            KMVideoData.add(k2k3list);
            dataBase.insertVideoList("3", k2k3list.getName(), k2k3list.getSize()
                    , k2k3list.getUrl(), k2k3list.getIshot(), k2k3list.getPhotourl()
                    , k2k3list.getDetail());
        }
        updateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showallBtn:
                startActivity(DadaUrlPath.KESAN_JJ, "科三简介");
                break;
            case R.id.layout_km_ksjq_1:
                startActivity(DadaUrlPath.KESAN_NR, "考试内容");
                break;
            case R.id.layout_km_ksjq_2:
                startActivity(DadaUrlPath.KESAN_XJ, "科目详解");
                break;
            case R.id.layout_km_ksjq_3:
                startActivity(DadaUrlPath.KESAN_KF, "扣分评判");
                break;
            case R.id.layout_km_ksjq_4:
                startActivity(DadaUrlPath.KESAN_MJ, "考试秘籍");
                break;
            case R.id.layout_km_ksjq_5:
                Intent light_voice = new Intent(getActivity(), LK3LightActivity.class);
                startActivity(light_voice);
                break;
            case R.id.k3content1:
                startActivity(url[0], "驾照科目三考试基本注意事项");
                break;
            case R.id.k3content2:
                startActivity(url[1], "科三考试技巧大汇总");
                break;
            case R.id.k3content3:
                startActivity(url[2], "夜考注意事项");
                break;
            case R.id.neterror_img:
                update();
                break;
        }
    }

    /**
     * 文章列表
     *
     * @param url
     * @param content
     */
    void startActivity(String url, String content) {
        Intent intent_phb = new Intent(getActivity(), H5Activity.class);
        intent_phb.putExtra("url", url);
        intent_phb.putExtra("content", content);
        startActivity(intent_phb);
    }

    @Override
    public void getResultCallBack(String ID, Object object, String Message) {
        if (Message.contains("成功")) {
            switch (ID) {
                case "video3":
                    km3Video = (KM3Video) object;
                    getKMVideoData();
                    break;
                case "km3contentlist":
                    km3list = (KM3ContentList) object;
                    setContentList();
                    mGetData.getKM3Video();
                    break;
            }
        }
    }

    private void setContentList() {
        int i = 0;
        for (KM3ContentList.RowsEntity km3row : this.km3list.getRows()) {
            if (i == 0) {
                title1.setText(km3row.getTitle());
                time1.setText(km3row.getTime());
                url[0] = km3row.getUrl();
            }
            if (i == 1) {
                title2.setText(km3row.getTitle());
                time2.setText(km3row.getTime());
                url[1] = km3row.getUrl();
            }
            if (i == 2) {
                title3.setText(km3row.getTitle());
                time3.setText(km3row.getTime());
                url[2] = km3row.getUrl();
            }
            i++;
        }
    }

    @Override
    public void extractSuccess(String ID, String Message) {

    }

    BaseGridView.OnItemClickListener onItemClickListener = new BaseGridView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String size = null, url = null, photo = null, path = null, name = KMVideoData.get(position).getName();
            int index = position;
            Cursor cursor = dataBase.queryVideoList2("3", KMVideoData.get(position).getName());
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    size = cursor.getString(2);
                    path = cursor.getString(3);
                    photo = cursor.getString(5);
                    url = cursor.getString(6);
                }
            }
            Intent intent = new Intent(getActivity(), DownKmVideoActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("photo", photo);
            intent.putExtra("index", index);
            intent.putExtra("path", path);
            intent.putExtra("size", size);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    };

    void updateUI() {
        adapter = new K2K3VideoListAdapter(getActivity(), KMVideoData);
        gridView.setAdapter(adapter);
    }

    public void onEventMainThread(ResultEvent event) {
        KMVideoData.get(event.itemindex).setIsdown(event.downflag);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind();
    }

}

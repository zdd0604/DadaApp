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
import com.dada.mylibrary.Gson.KM2ContentList;
import com.dada.mylibrary.Gson.KM2Video;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.Activity.DownKmVideoActivity;
import com.dadaxueche.student.dadaapp.Activity.H5Activity;
import com.dadaxueche.student.dadaapp.Adapter.K2K3VideoListAdapter;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.BaseGridView;
import com.dadaxueche.student.dadaapp.Util.ReceiveMsgService;
import com.dadaxueche.student.dadaapp.Util.ResultEvent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class KM2Fragment extends Fragment implements
        View.OnClickListener,
        GetData.GetResultCallBack {
    private WebView webView_kmjj;
    private KM2Video km2Video = new KM2Video();
    private KM2ContentList km2list = new KM2ContentList();
    private TextView title1, time1, title2, time2, title3, time3;
    private ArrayList<K2K3VideoList> KMVideoData = new ArrayList<>();
    private K2K3VideoList k2k3list = new K2K3VideoList();
    private GetData mGetData = new GetData();
    private String[] url = new String[3];
    private BaseGridView gridView;
    private K2K3VideoListAdapter adapter;
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
        return inflater.inflate(R.layout.fragment_km2, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        down_state = getActivity().getSharedPreferences("downstate",
                Activity.MODE_PRIVATE);
        cursor = dataBase.queryVideoList("2");
        check = new Check(getActivity());
        EventBus.getDefault().register(this);
        bind();
        gridView = (BaseGridView) view.findViewById(R.id.k2gridview);
        gridView.setOnItemClickListener(onItemClickListener);
        neterror_img = (ImageView) view.findViewById(R.id.neterror_img);
        neterror_img.setOnClickListener(this);
        webView_kmjj = (WebView) view.findViewById(R.id.webView_kmjj);
        sc = (ScrollView) view.findViewById(R.id.scrollView_km2);
        update();
        view.findViewById(R.id.showallBtn).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_1).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_2).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_3).setOnClickListener(this);
        view.findViewById(R.id.layout_km_ksjq_4).setOnClickListener(this);
        view.findViewById(R.id.k2content1).setOnClickListener(this);
        view.findViewById(R.id.k2content2).setOnClickListener(this);
        view.findViewById(R.id.k2content3).setOnClickListener(this);
        title1 = (TextView) view.findViewById(R.id.textView_km_kskq_1_title);
        title2 = (TextView) view.findViewById(R.id.textView_km_kskq_2_title);
        title3 = (TextView) view.findViewById(R.id.textView_km_kskq_3_title);
        time1 = (TextView) view.findViewById(R.id.textView_km_kskq_1_time);
        time2 = (TextView) view.findViewById(R.id.textView_km_kskq_2_time);
        time3 = (TextView) view.findViewById(R.id.textView_km_kskq_3_time);
    }

    void update() {
        if (check.isConnectingToInternet()) {
            mGetData.setGetResultCallBack(this);
            mGetData.getKM2ContentList();
            neterror_img.setVisibility(View.GONE);
            webView_kmjj.setVisibility(View.VISIBLE);
            webView_kmjj.loadUrl(DadaUrlPath.KEER_N);
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
            receiveMsgService.setOnGetConnectState(new ReceiveMsgService.GetConnectState() {
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
        cursor = dataBase.queryVideoList("2");
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
            if (dataBase.deleteVideoList("2")) {
                insertDataBase();
            }
        } else {
            insertDataBase();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showallBtn:
                startActivity(DadaUrlPath.KEER_JJ, "科二简介");
                break;
            case R.id.layout_km_ksjq_1:
                startActivity(DadaUrlPath.KEER_NR, "考试内容");
                break;
            case R.id.layout_km_ksjq_2:
                startActivity(DadaUrlPath.KEER_XJ, "科目详解");
                break;
            case R.id.layout_km_ksjq_3:
                startActivity(DadaUrlPath.KEER_KF, "扣分评判");
                break;
            case R.id.layout_km_ksjq_4:
                startActivity(DadaUrlPath.KEER_MJ, "考试秘籍");
                break;
            case R.id.k2content1:
                startActivity(url[0], "科二必过小技巧");
                break;
            case R.id.k2content2:
                startActivity(url[1], "科二考试经典技巧");
                break;
            case R.id.k2content3:
                startActivity(url[2], "科二图文详解");
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
                case "video2":
                    km2Video = (KM2Video) object;
                    getKMVideoData();
                    break;
                case "km2contentlist":
                    km2list = (KM2ContentList) object;
                    setContentList();
                    mGetData.getKM2Video();
                    break;
            }
        }
    }

    /**
     * 插入数据库并更新UI
     */
    private void insertDataBase() {
        KMVideoData.clear();
        for (KM2Video.RowsEntity km2row : this.km2Video.getRows()) {
            k2k3list = new K2K3VideoList();
            k2k3list.setName(km2row.getName());
            k2k3list.setSize(km2row.getTime() + "  " + km2row.getSize() + "MB");
            k2k3list.setUrl(km2row.getUrl());
            if (km2row.getIsHot() == 1) {
                k2k3list.setIshot("热门");
            } else {
                k2k3list.setIshot("");
            }
            k2k3list.setPhotourl(km2row.getPhoto());
            k2k3list.setDetail(km2row.getDetail());
            String[] ps = km2row.getUrl().split("/");
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
            dataBase.insertVideoList("2", k2k3list.getName(), k2k3list.getSize()
                    , k2k3list.getUrl(), k2k3list.getIshot(), k2k3list.getPhotourl()
                    , k2k3list.getDetail());
        }
        updateUI();
    }

    /**
     * 给文章列表赋值
     */
    private void setContentList() {
        int i = 0;
        for (KM2ContentList.RowsEntity km2row : km2list.getRows()) {
            if (i == 0) {
                title1.setText(km2row.getTitle());
                time1.setText(km2row.getTime());
                url[0] = km2row.getUrl();
            }
            if (i == 1) {
                title2.setText(km2row.getTitle());
                time2.setText(km2row.getTime());
                url[1] = km2row.getUrl();
            }
            if (i == 2) {
                title3.setText(km2row.getTitle());
                time3.setText(km2row.getTime());
                url[2] = km2row.getUrl();
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
            Cursor cursor = dataBase.queryVideoList2("2", KMVideoData.get(position).getName());
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    size = cursor.getString(2);
                    path = cursor.getString(3);
                    photo = cursor.getString(5);
                    url = cursor.getString(6);
                }
            }
            Intent intent = new Intent(getActivity(), DownKmVideoActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("url", url);
            intent.putExtra("photo", photo);
            intent.putExtra("path", path);
            intent.putExtra("size", size);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    };

    /**
     * 更新UI
     */
    void updateUI() {
        adapter = new K2K3VideoListAdapter(getActivity(), KMVideoData);
        gridView.setAdapter(adapter);
    }

    /**
     * 获取视频详情页面的视频下载状态
     *
     * @param event
     */
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

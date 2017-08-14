package com.dadaxueche.student.dadaapp.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dada.mylibrary.Util.CHttpClient;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.DownloadInfo;
import com.dadaxueche.student.dadaapp.Util.DownloadManager;
import com.dadaxueche.student.dadaapp.Util.DownloadService;
import com.dadaxueche.student.dadaapp.Util.ReceiveMsgService;
import com.dadaxueche.student.dadaapp.Util.ResultEvent;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import de.greenrobot.event.EventBus;

public class DownKmVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private String size, fileName, name, path, nativepath, url, photo;
    private TextView size_time, content, bfb;
    private ImageView back, download;
    private RelativeLayout quanping;
    private RelativeLayout down_rela;
    private VideoView videoview;
    private WebView mwebview;
    private static ProgressBar progressBar;
    private boolean down_flag;
    private Bitmap backimg;
    private File file;
    private Message message = new Message();
    private int itemindex;
    private SharedPreferences down_state;
    private SharedPreferences.Editor editor;
    private int currentposition;
    private DownloadManager downloadManager;
    private DownloadInfo downloadInfo;
    private HttpHandler.State state;
    private ImageView neterror_img;
    private int k = 0;
    private Check check;
    private boolean isWifiState = false;
    private ProgressDialog dialogloading;
    private ReceiveMsgService receiveMsgService;
    private boolean conncetState = true; // 记录当前连接状态，因为广播会接收所有的网络状态改变wifi/3g等等，所以需要一个标志记录当前状态
    Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 已连接
                    update();
                    break;
                case 2:// 未连接
                    break;
                case 4:
                    down_rela.setBackground(new BitmapDrawable(backimg));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_km_video);
        final Intent serviceIntent = new Intent(this, DownloadService.class);
        startService(serviceIntent);
        downloadManager = DownloadService.getDownloadManager(this);
        down_state = getSharedPreferences("downstate",
                Activity.MODE_PRIVATE);
        check = new Check(this);
        bind();
        init();
    }

    void init() {
        itemindex = getIntent().getIntExtra("index", 0);
        photo = getIntent().getStringExtra("photo");//图片路径
        url = getIntent().getStringExtra("url");//webview路径
        path = getIntent().getStringExtra("path");//视频路径
        size = getIntent().getStringExtra("size");
        name = getIntent().getStringExtra("name");
        neterror_img = (ImageView) findViewById(R.id.neterror_img);
        neterror_img.setOnClickListener(this);
        quanping = (RelativeLayout) findViewById(R.id.quanping_imageview);
        quanping.setVisibility(View.GONE);
        size_time = (TextView) findViewById(R.id.downloadkmvideosize);
        content = (TextView) findViewById(R.id.download_content);
        back = (ImageView) findViewById(R.id.download_back);
        download = (ImageView) findViewById(R.id.downloadkmvideoimg);
        down_rela = (RelativeLayout) findViewById(R.id.down_rela);
        videoview = (VideoView) findViewById(R.id.surfaceview);
        progressBar = (ProgressBar) findViewById(R.id.downloadkmvideoprogress);
        bfb = (TextView) findViewById(R.id.download_bfb);
        mwebview = (WebView) findViewById(R.id.video_datails_h5);
        content.setText(name);
        size_time.setText(size);
        check();
    }

    private void check() {
        back.setOnClickListener(this);
        download.setOnClickListener(this);
        quanping.setOnClickListener(this);
        MediaController mediaController = new MediaController(this);
        mediaController.setEnabled(false);
        videoview.setMediaController(new MediaController(this));
        update();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(DadaUrlPath.SDCARD_VIDEO);
            if (file.exists()) {
                String[] ps = path.split("/");
                fileName = ps[ps.length - 1];
                checkFile();
            } else {
                file.mkdirs();
            }
        } else {
            Toast.makeText(getApplicationContext(), "SDcard不存在", Toast.LENGTH_SHORT).show();
        }
    }

    void update() {
        neterror_img.setVisibility(View.GONE);
        mwebview.setVisibility(View.VISIBLE);
        WebSettings webSettings = mwebview.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        if (isConnectNet()) {
            if (!"".equals(url)) {
                mwebview.loadUrl(url);
                mwebview.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        dialogloading.dismiss();
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        dialogloading = ProgressDialog.show(DownKmVideoActivity.this, "提示", "正在拼命加载中...");
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        dialogloading.dismiss();
                    }
                });
            }
        } else {
            neterror_img.setVisibility(View.VISIBLE);
        }
        if (!"".equals(photo)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    backimg = CHttpClient.getHttpBitmap(photo);
                    handler.sendEmptyMessage(4);
                }
            }).start();
        }
    }


    public void checkFile() {
        if (down_state.getBoolean(fileName, false)) {
            if (Check.CheckFile(DadaUrlPath.SDCARD_VIDEO, fileName)) {
                down_rela.setVisibility(View.GONE);
                videoview.setVisibility(View.VISIBLE);
                nativepath = DadaUrlPath.SDCARD_VIDEO + fileName;
                Uri videoUri = Uri.parse(nativepath);
                videoview.setVideoURI(videoUri);
                quanping.setVisibility(View.VISIBLE);
                videoview.start();
                videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        if (what == 1) {
                            File file = new File(nativepath);
                            if (file.exists()) {
                                file.delete();
                            }
                            EventBus.getDefault().post(new ResultEvent(itemindex, false));
                            editor = down_state.edit();
                            editor.putBoolean(fileName, false);
                            editor.commit();
                        }

                        return false;
                    }
                });
            }
        } else {
            videoview.setVisibility(View.GONE);
            down_rela.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_back:
                finish();
                break;
            case R.id.downloadkmvideoimg:
                if (isConnectNet()) {
                    if (isWifiConnected(this)) {
                        down_pause();
                    } else {
                        dialog("非Wi-Fi网络环境，下载此视频可能会产生流量，要继续下载吗？", "下载", "取消");
                    }
                } else {
                    dialog("网络异常", "", "");
                }
                break;
            case R.id.quanping_imageview:
                currentposition = videoview.getCurrentPosition();
                Intent itent = new Intent(DownKmVideoActivity.this, FullPlayActivity.class);
                itent.putExtra("nativepath", nativepath);
                itent.putExtra("currentposition", currentposition);
                itent.putExtra("width", videoview.getMeasuredWidth());
                itent.putExtra("height", videoview.getMeasuredHeight());
                startActivityForResult(itent, 123);
                break;
            case R.id.neterror_img:
                if (isConnectNet()) {
                    update();
                }
                break;
        }
    }

    /**
     * 下载暂停判断
     */
    private void down_pause() {
        if (k == 0) {
            k = 1;
            editor = down_state.edit();
            editor.putBoolean(fileName, false);
            editor.commit();
            downloadFile(path, fileName, DadaUrlPath.SDCARD_VIDEO);
            download.setBackgroundResource(R.mipmap.pausexiao);
            progressBar.setVisibility(View.VISIBLE);
            bfb.setVisibility(View.VISIBLE);
        } else {
            state = downloadInfo.getState();
            if (!down_flag) {
                download.setBackgroundResource(R.mipmap.xiazai);
                progressBar.setVisibility(View.GONE);
                down_flag = true;
                switch (state) {
                    case LOADING:
                        try {
                            downloadManager.stopDownload(downloadInfo);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            } else {
                down_flag = false;
                download.setBackgroundResource(R.mipmap.pausexiao);
                progressBar.setVisibility(View.VISIBLE);
                bfb.setVisibility(View.VISIBLE);
                switch (state) {
                    case CANCELLED:
                        try {
                            downloadManager.resumeDownload(downloadInfo, new DownloadRequestCallBack());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        }


    }

    private void downloadFile(String path, String fileName, String saveUrl) {
        try {
            downloadManager.addNewDownload(path, fileName, saveUrl + fileName, true, false, new DownloadRequestCallBack());
            downloadInfo = downloadManager.getDownloadInfo(0);
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                RequestCallBack callBack = handler.getRequestCallBack();
                if (callBack instanceof DownloadManager.ManagerCallBack) {
                    DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
                    if (managerCallBack.getBaseCallBack() == null) {
                        managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private class DownloadRequestCallBack extends RequestCallBack<File> {

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            if (total > 0) {
                int prolength = (int) (current * 100 / total);
                bfb.setText(prolength + "%");
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            downloadInfo.setProgress(downloadInfo.getFileLength());
            editor = down_state.edit();
            editor.putBoolean(fileName, true);
            editor.commit();
            quanping.setVisibility(View.VISIBLE);
            EventBus.getDefault().post(new ResultEvent(itemindex, true));
            checkFile();
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            try {
                downloadManager.stopAllDownload();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void bind() {
        Intent intent = new Intent(DownKmVideoActivity.this, ReceiveMsgService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            receiveMsgService = ((ReceiveMsgService.MyBinder) service)
                    .getService();
//            receiveMsgService.setOnGetConnectState(new ReceiveMsgService.GetConnectState // 添加接口实例获取连接状态(                @Override
//                public void GetState(boolean isConnected) {
//                    if (conncetState != isConnected) { // 如果当前连接状态与广播服务返回的状态不同才进行通知显示
//                        conncetState = isConnected;
//                        if (conncetState) {// 已连接
//                            handler.sendEmptyMessage(1);
//                        } else {// 未连接
//                            handler.sendEmptyMessage(2);
//                        }
//                    }
//                }
//}
//                }
//            });
            receiveMsgService.setOnGetConnectState(new ReceiveMsgService.GetConnectState() {
                // 添加接口实例获取连接状态
                @Override
                public void GetState(boolean isConnected, boolean isWifi) {
                    if (conncetState != isConnected) { // 如果当前连接状态与广播服务返回的状态不同才进行通知显示
                        conncetState = isConnected;
                        if (conncetState) {// 已连接
                            handler.sendEmptyMessage(1);
                            if (isWifi) {
                                isWifiState = true;
                            }
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
            unbindService(serviceConnection);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        try {
            downloadManager.stopAllDownload();
        } catch (DbException e) {
            e.printStackTrace();
        }
        videoview.stopPlayback();
        super.onDestroy();
        unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 123) {
            currentposition = data.getIntExtra("currentposition", 0);
            videoview.seekTo(currentposition);
            videoview.start();
        }
    }

    /**
     * 判断网络
     */
    public boolean isConnectNet() {
        if (check.isConnectingToInternet()) {
            return true;
        }
        return false;
    }

    protected void dialog(String tishi, String queren, String quxiao) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DownKmVideoActivity.this);
        builder.setMessage(tishi);
        builder.setTitle("提示");
        builder.setPositiveButton(queren, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
//                startActivity(intent);
                down_pause();
            }
        });
        builder.setNegativeButton(quxiao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}

package com.dadaxueche.student.dadaapp.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.dadaxueche.student.dadaapp.Util.ReceiveMsgService;
import com.umeng.analytics.MobclickAgent;

public class H5Activity extends AppCompatActivity implements View.OnClickListener {
    private WebView mWebView;
    private String path, str;
    private TextView content, logintextview;
    private Button loginBtn;
    private RelativeLayout view_bottom;
    private SharedPreferences LoginID;
    private int islogin;
    private GlobalData mGlobalData;
    private String phone;
    private String KM;
    private Check check;
    private ImageView error_img;
    private ReceiveMsgService receiveMsgService;
    private boolean conncetState = true; // 记录当前连接状态，因为广播会接收所有的网络状态改变wifi/3g等等，所以需要一个标志记录当前状态
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 已连接
                    update();
                    break;
                case 2:// 未连接
                    error_img.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.GONE);
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
        setContentView(R.layout.activity_h5);
        check = new Check(this);
        mGlobalData = (GlobalData) getApplication();
        LoginID = getSharedPreferences("isLogin", 0);
        islogin = LoginID.getInt("islonginId", 0);
        phone = LoginID.getString("user_mobile", "");
        path = getIntent().getStringExtra("url");
        str = getIntent().getStringExtra("content");
        KM = getIntent().getStringExtra("KM");
        error_img = (ImageView) findViewById(R.id.neterror_img);
        error_img.setOnClickListener(this);
        findViewById(R.id.toobar_back).setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.webView_nb);
        bind();
        init();
    }

    void init() {
        content = (TextView) findViewById(R.id.toolbar_content);
        view_bottom = (RelativeLayout) findViewById(R.id.view_bottom);
        logintextview = (TextView) findViewById(R.id.logintextview);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        view_bottom.setVisibility(View.GONE);
        update();
    }

    void update() {
        if (check.isConnectingToInternet()) {
            error_img.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
            // 开启 DOM storage API 功能
            webSettings.setDomStorageEnabled(true);
            //开启 database storage API 功能
            webSettings.setDatabaseEnabled(true);
            String[] ps = path.split("/");
            String fileName = ps[ps.length - 1];
            String cacheDirPath = getFilesDir().getAbsolutePath() + fileName;
//      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
            //设置数据库缓存路径
            webSettings.setDatabasePath(cacheDirPath);
            //设置  Application Caches 缓存目录
            webSettings.setAppCachePath(cacheDirPath);
            //开启 Application Caches 功能
            webSettings.setAppCacheEnabled(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(false);
            webSettings.setDefaultTextEncodingName("UTF-8");
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            check();
        } else {
            error_img.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }
    }


    void check() {
        if ("".equals(str)) {
            str = "";
        }
        content.setText(str);
        if ("排行榜".equals(str)) {
//            view_bottom.setVisibility(View.VISIBLE);
            if ("1".equals(KM)) {
                mWebView.loadUrl(path + "?phoneId=" + mGlobalData.mDEVICE_ID + "&" + "mobile=" + phone + "&" + "stage=0");
            }
            if ("4".equals(KM)) {
                mWebView.loadUrl(path + "?phoneId=" + mGlobalData.mDEVICE_ID + "&" + "mobile=" + phone + "&" + "stage=1");
            }
            if (islogin == 0) {
                logintextview.setText("登录后可查看自己排行哟");
                loginBtn.setText("登录");
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(H5Activity.this, Dada_User_LoginCode.class));
                    }
                });
            }
            if (islogin == 1) {
                logintextview.setText("您的排名是" + "未确定");
                loginBtn.setVisibility(View.GONE);
            }
            return;
        }
        mWebView.loadUrl(path);
    }

    private void bind() {
        Intent intent = new Intent(H5Activity.this, ReceiveMsgService.class);
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
        super.onDestroy();
        unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.neterror_img:
                update();
                break;
            case R.id.toobar_back:
                finish();
                break;
        }
    }
}

package com.dadaxueche.student.dadaapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dadaxueche.student.dadaapp.Fragment.FragmentHome;
import com.dadaxueche.student.dadaapp.MainActivity;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.View.Tools;
import com.umeng.analytics.MobclickAgent;

public class Dada_User_falvTk extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    private ImageView web_error;
    private TextView content;
    private WebView dada_webview;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user_falv_tk);
        init();
    }

    private void init() {
        dialog = ProgressDialog.show(Dada_User_falvTk.this, "提示", "正在拼命加载中...");
        dialog.setCanceledOnTouchOutside(true);
        back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        content = (TextView) findViewById(R.id.toolbar_lift_content);
        dada_webview = (WebView) findViewById(R.id.dada_webview);
        web_error = (ImageView) findViewById(R.id.web_error);
        Intent intent = getIntent();
        String webUrl = intent.getStringExtra("webUrl");
        String name = intent.getStringExtra("toobarbname");
        content.setText(name);
        back.setOnClickListener(this);
        dada_webview.loadUrl(webUrl);
        dada_webview.getSettings().setJavaScriptEnabled(true);
        dada_webview.getSettings().setDomStorageEnabled(true);
        dada_webview.getSettings().setGeolocationEnabled(true);
        dada_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        dada_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        dada_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if(MainActivity.dialog!=null){
                    MainActivity.dialog.dismiss();
                }
                if(FragmentHome.dialog != null){
                    FragmentHome.dialog.dismiss();
                }
                dada_webview.setVisibility(View.GONE);
                web_error.setVisibility(View.VISIBLE);
                web_error.setImageResource(R.mipmap.meiyouwangluo);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            dialog.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobar_lift_back:
                finish();
                break;
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
}

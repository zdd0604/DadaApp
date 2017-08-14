package com.dadaxueche.student.dadaapp.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

public class Dada_InFo extends AppCompatActivity implements View.OnClickListener{
    private WebView user_dada;
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private ImageView web_error;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__in_fo);
        init();
    }

    private void init(){
        dialog = ProgressDialog.show(Dada_InFo.this, "提示", "正在拼命加载中...");
        user_dada = (WebView) findViewById(R.id.user_dada);
        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toobar_back.setOnClickListener(this);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        toolbar_content.setText("关于嗒嗒");
        web_error = (ImageView) findViewById(R.id.web_error);

        user_dada.loadUrl(DadaUrlPath.DADA_INFO);
        user_dada.reload();
        user_dada.getSettings().setJavaScriptEnabled(true);
        user_dada.setWebChromeClient(new WebChromeClient());
        user_dada.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        user_dada.setWebViewClient(new WebViewClient() {
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
                user_dada.setVisibility(View.GONE);
                web_error.setVisibility(View.VISIBLE);
                web_error.setImageResource(R.mipmap.meiyouwangluo);
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

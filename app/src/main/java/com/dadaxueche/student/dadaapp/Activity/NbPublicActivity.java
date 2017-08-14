package com.dadaxueche.student.dadaapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dada.mylibrary.Util.Check;
import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

public class NbPublicActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView mWebView;
    private String path, str;
    private TextView content;
    private Check check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check = new Check(this);
        if (check.isConnectingToInternet()) {
            setContentView(R.layout.activity_nb_public);
            init();
        } else {
            setContentView(R.layout.neterror);
        }
        findViewById(R.id.toobar_back).setOnClickListener(this);
    }

    private void init() {
        path = getIntent().getStringExtra("url");
        str = getIntent().getStringExtra("content");
        content = (TextView) findViewById(R.id.toolbar_content);
        content.setText(str);
        mWebView = (WebView) findViewById(R.id.webView_nb);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setDefaultTextEncodingName("UTF_8");
        mWebView.loadUrl(path);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobar_back:
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

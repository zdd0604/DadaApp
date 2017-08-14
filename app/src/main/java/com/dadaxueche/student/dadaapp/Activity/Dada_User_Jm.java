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

public class Dada_User_Jm extends AppCompatActivity implements View.OnClickListener {
    private WebView dada_jiaxiao_jiameng;
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private ImageView web_error;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user__jm);
        init();
    }

    private void init() {
        dialog = ProgressDialog.show(Dada_User_Jm.this, "提示", "正在拼命加载中...");
        dada_jiaxiao_jiameng = (WebView) findViewById(R.id.dada_jiaxiao_jiameng);
        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toobar_back.setOnClickListener(this);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        web_error = (ImageView) findViewById(R.id.web_error);
        toolbar_content.setText("驾校加盟");
        dada_jiaxiao_jiameng.loadUrl(DadaUrlPath.DADA_JIAMENG);
        dada_jiaxiao_jiameng.reload();
        dada_jiaxiao_jiameng.getSettings().setJavaScriptEnabled(true);
        dada_jiaxiao_jiameng.setWebChromeClient(new WebChromeClient());
        dada_jiaxiao_jiameng.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        dada_jiaxiao_jiameng.setWebViewClient(new WebViewClient() {
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
                dada_jiaxiao_jiameng.setVisibility(View.GONE);
                web_error.setVisibility(View.VISIBLE);
                web_error.setImageResource(R.mipmap.meiyouwangluo);
            }
        });

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

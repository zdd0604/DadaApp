package com.dadaxueche.student.dadaapp.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.umeng.analytics.MobclickAgent;

public class Dada_User_SheQu extends AppCompatActivity implements View.OnClickListener {
    private WebView user_shequ;
    private TextView toolbar_content;
    private LinearLayout toobar_back;
    private ImageView web_error;
    private ProgressDialog dialog;
    private GlobalData mGlobalData;
    private SharedPreferences LoginID;
    private String user_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada__user__she_qu);
        mGlobalData = (GlobalData) getApplication();
        LoginID = getSharedPreferences("isLogin", 0);
        user_mobile = LoginID.getString("user_mobile","");
        init(user_mobile, mGlobalData.mDEVICE_ID);
    }

    private void init(String mobile,String phoneId) {
        dialog = ProgressDialog.show(Dada_User_SheQu.this, "提示", "正在拼命加载中...");
        user_shequ = (WebView) findViewById(R.id.user_shequ);
        toobar_back = (LinearLayout) findViewById(R.id.toobar_lift_back);
        toobar_back.setOnClickListener(this);
        toolbar_content = (TextView) findViewById(R.id.toolbar_lift_content);
        web_error = (ImageView) findViewById(R.id.web_error);
        toolbar_content.setText("嗒嗒社区");
        String path = DadaUrlPath.DADA_SHEQU + "?mobile=" + mobile + "&phoneId=" + phoneId;
        user_shequ.loadUrl(path);
        user_shequ.reload();
        user_shequ.getSettings().setJavaScriptEnabled(true);
        user_shequ.setWebChromeClient(new WebChromeClient());
        user_shequ.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        user_shequ.setWebViewClient(new WebViewClient() {
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
                user_shequ.setVisibility(View.GONE);
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

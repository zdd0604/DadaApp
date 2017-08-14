package com.dadaxueche.student.dadaapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangdongdong on 2015/9/10.
 */
public class FragmentYuYue extends Fragment{
    private View view;
    private WebView webView_yuyue;
    private ImageView web_error;
    private GlobalData mGlobalData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yuyue, container, false);
        mGlobalData = (GlobalData) getActivity().getApplication();
        init(DadaUrlPath.YU_YUE);
        return view;
    }

    private void init(String webUrl){
        webView_yuyue = (WebView) view.findViewById(R.id.webView_yuyue);
        web_error = (ImageView) view.findViewById(R.id.web_error);
        webView_yuyue.getSettings().setJavaScriptEnabled(true);
        webView_yuyue.setWebChromeClient(new WebChromeClient());
        webView_yuyue.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView_yuyue.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView_yuyue.setVisibility(View.GONE);
                web_error.setVisibility(View.VISIBLE);
                web_error.setImageResource(R.mipmap.meiyouwangluo);
            }
        });
        webView_yuyue.loadUrl(webUrl);
        webView_yuyue.reload();
    }

    public void init() {
        webView_yuyue.loadUrl(DadaUrlPath.YU_YUE);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("yuyue"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("yuyue");
    }
}
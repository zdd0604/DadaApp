package com.dadaxueche.student.dadaapp.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.MainActivity;
import com.dadaxueche.student.dadaapp.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangdongdong on 2015/9/10.
 */
public class FragmentZhiNan extends Fragment implements View.OnClickListener{
    private WebView dada_zhinan;
    private ProgressDialog dialog = null;
    private ImageView web_error;
    private Check check;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zhinan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        check = new Check(getActivity());
        dada_zhinan = (WebView) view.findViewById(R.id.dada_zhinan);
        web_error = (ImageView) view.findViewById(R.id.web_error);
        web_error.setOnClickListener(this);
        dada_zhinan.getSettings().setJavaScriptEnabled(true);
        dada_zhinan.getSettings().setDomStorageEnabled(true);
        dada_zhinan.getSettings().setGeolocationEnabled(true);
        dada_zhinan.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        dada_zhinan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        dada_zhinan.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(MainActivity.dialog!=null){
                    MainActivity.dialog.dismiss();
                }
                if(dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                dada_zhinan.setVisibility(View.GONE);
                web_error.setVisibility(View.VISIBLE);
                web_error.setImageResource(R.mipmap.meiyouwangluo);
            }
        });
        dada_zhinan.loadUrl(DadaUrlPath.ZHI_NAN);
    }

    public void init() {
        dada_zhinan.loadUrl(DadaUrlPath.ZHI_NAN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.web_error:
                dialog = ProgressDialog.show(getActivity(), "提示", "正在拼命加载中...");
                dialog.setCanceledOnTouchOutside(true);
                if(check.isConnectingToInternet()){
                    dada_zhinan.setVisibility(View.VISIBLE);
                    web_error.setVisibility(View.GONE);
                    dada_zhinan.loadUrl(DadaUrlPath.ZHI_NAN);
                }else{
                    dialog.dismiss();
                    dada_zhinan.setVisibility(View.GONE);
                    web_error.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("zhinan"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("zhinan");
    }
}

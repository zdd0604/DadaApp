package com.dadaxueche.student.dadaapp.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dada.mylibrary.Util.Check;
import com.dada.mylibrary.Util.DadaUrlPath;
import com.dadaxueche.student.dadaapp.MainActivity;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.GetLocation;
import com.dadaxueche.student.dadaapp.Util.GlobalData;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangdongdong on 2015/9/10.
 */
public class FragmentBM extends Fragment implements View.OnClickListener {
    private View view;
    private WebView bm_web_zjx;
    public SharedPreferences LoginID;
    public SharedPreferences.Editor editor;
    private ImageView web_error;
    private String phoneNumber, mlongitude, mlatitude;
    private MyDataBase myDataBase = new MyDataBase();
    private GlobalData mGlobalData;
    private Check check;
    private ProgressDialog dialog = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bm, container, false);
        check = new Check(getActivity());
        mGlobalData = (GlobalData) getActivity().getApplication();
        LoginID = getActivity().getSharedPreferences("isLogin", 0);
        editor = LoginID.edit();
        bm_web_zjx = (WebView) view.findViewById(R.id.bm_webView_zjx);
        web_error = (ImageView) view.findViewById(R.id.web_error);
        web_error.setOnClickListener(this);
        init();
        return view;
    }

    public void setWebUrl(String url) {
        bm_web_zjx.loadUrl(url);
    }

    private void init() {
        bm_web_zjx.getSettings().setJavaScriptEnabled(true);
        bm_web_zjx.setWebChromeClient(new WebChromeClient());
        bm_web_zjx.getSettings().setBuiltInZoomControls(true);
        bm_web_zjx.getSettings().setDomStorageEnabled(true);
        bm_web_zjx.getSettings().setDefaultTextEncodingName("utf-8");
        bm_web_zjx.addJavascriptInterface(getActivity(), "android");
        bm_web_zjx.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        bm_web_zjx.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (MainActivity.dialog != null) {
                    MainActivity.dialog.dismiss();
                }
                if (FragmentHome.dialog != null) {
                    FragmentHome.dialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                bm_web_zjx.setVisibility(View.GONE);
                web_error.setVisibility(View.VISIBLE);
                web_error.setImageResource(R.mipmap.meiyouwangluo);
            }
        });
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("bm"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("bm");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_error:
                dialog = ProgressDialog.show(getActivity(), "提示", "正在拼命加载中...");
                dialog.setCanceledOnTouchOutside(true);
                if (check.isConnectingToInternet()) {
                    bm_web_zjx.setVisibility(View.VISIBLE);
                    web_error.setVisibility(View.GONE);
                    isLoaction();
                } else {
                    dialog.dismiss();
                    bm_web_zjx.setVisibility(View.GONE);
                    web_error.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 传送地理位置
     */
    private void isLoaction() {
        phoneNumber = LoginID.getString("user_mobile", "");
        if (!phoneNumber.isEmpty()) {
            try {
                Cursor cursor = myDataBase.queryUserInfo(phoneNumber);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        mlongitude = cursor.getString(cursor.getColumnIndex("UserInfo_longitude"));
                        mlatitude = cursor.getString(cursor.getColumnIndex("UserInfo_latitude"));
                    }
                }
            } catch (NullPointerException n) {
                Log.v("show", "数据为空");
            }
        }
        if (mlatitude != null && mlongitude != null) {
            bm_web_zjx.loadUrl(DadaUrlPath.ZHAO_JX_JL + "?phoneId="
                    + mGlobalData.mDEVICE_ID
                    + "&mobile=" + phoneNumber
                    + "&type=" + 0
                    + "&latitude=" + mlatitude
                    + "&longitude=" + mlongitude);
        } else {
            double[] location = GetLocation.getGPS(getActivity());
            bm_web_zjx.loadUrl(DadaUrlPath.ZHAO_JX_JL + "?phoneId="
                    + mGlobalData.mDEVICE_ID
                    + "&mobile=" + phoneNumber
                    + "&type=" + 0
                    + "&latitude=" + location[0]
                    + "&longitude=" + location[1]);
        }
    }

}

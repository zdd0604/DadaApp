package com.dadaxueche.student.dadaapp.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.dada.mylibrary.Util.DadaUrlPath;
import com.dada.mylibrary.Util.Get;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.WeiXin.simcpux.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler ,
	View.OnClickListener {
	
    private IWXAPI api;
	private WebView webView_weixin_back;
	private Button button_return_main;
	private String url = DadaUrlPath.Pulic_URL + "paysuccess.html";
	public static String orderNo;
	public static String phone;
	private int errorCode = 0;
	private static WeiXinBack weiXinBack;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

		webView_weixin_back = (WebView) findViewById(R.id.webView_weixin_back);
		button_return_main = (Button) findViewById(R.id.button_retuen_main);

		findViewById(R.id.actionbar_Title).setVisibility(View.GONE);

    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
		findViewById(R.id.imageButton_return).setOnClickListener(this);
		button_return_main.setOnClickListener(this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			errorCode = resp.errCode;
			if(resp.errCode == 0) {
				webView_weixin_back.loadUrl(url + "?mobile=" + phone + "&phoneId=" + Get.getPhoneID(this) + "&orderNo=" + orderNo);
				webView_weixin_back.getSettings().setJavaScriptEnabled(true);
				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
				SharedPreferences.Editor editor = getSharedPreferences("isLogin", 0).edit();
				editor.putString("user_mobile", phone);
				editor.commit();
				weiXinBack.weixinBack(errorCode);
			} else if (resp.errCode == -1) {
				finish();
			} else if (resp.errCode == -2) {
				finish();
			}
		}
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onConsoleMessage(ConsoleMessage cm) {
			Log.d("test", cm.message() + " -- From line "
					+ cm.lineNumber() + " of "
					+ cm.sourceId());
			return true;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			Toast.makeText(WXPayEntryActivity.this, message, Toast.LENGTH_LONG
			).show();
			return true;
		}
	}

	private void init() {
		webView_weixin_back.loadUrl(url + "?orderNo=" + orderNo + "&mobile=" + phone + "&phoneId=" + Get.getPhoneID(this));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imageButton_return:
				finish();
				break;
			case R.id.button_retuen_main:
				finish();
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		weiXinBack.weixinBack(errorCode);
	}

	public static void setWeiXinBack(WeiXinBack weiXinBack) {
		WXPayEntryActivity.weiXinBack = weiXinBack;
	}

	public interface WeiXinBack {
		void weixinBack(int errorCode);
	}
}
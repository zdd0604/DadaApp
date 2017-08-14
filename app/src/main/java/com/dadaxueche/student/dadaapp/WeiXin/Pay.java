package com.dadaxueche.student.dadaapp.WeiXin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Xml;

import com.dada.mylibrary.Gson.Money;
import com.dada.mylibrary.Util.CHttpRequestClass;
import com.dadaxueche.student.dadaapp.WeiXin.simcpux.Constants;
import com.dadaxueche.student.dadaapp.WeiXin.simcpux.MD5;
import com.dadaxueche.student.dadaapp.WeiXin.simcpux.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by wpf on 10-15-0015.
 */
public class Pay {
    private PayReq req = new PayReq();
    private Map<String,String> resultunifiedorder;
    private StringBuffer sb = new StringBuffer();
    private final IWXAPI msgApi;
    private Context context;
    private String orderNo;
    private String mUrl_GetMoney = "http://www.dadaxueche.com/m/orderMoney.do";
    private ProgressDialog dialog;
    private SharedPreferences LoginID;

    public Pay(Context context,SharedPreferences LoginID) {
        this.context = context;
        msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(Constants.APP_ID);
        this.LoginID = LoginID;
    }

    public void getPrePay_id(String orderNo) {
        this.orderNo = orderNo;
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");
            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(context, "提示", "正在获取预支付订单...");
        }

        @Override
        protected void onPostExecute(Map<String,String> result) {
            if(dialog != null) {
                dialog.dismiss();
            }
            if(result !=null && result.get("prepay_id") != null) {
                resultunifiedorder = result;
                genPayReq();
            }
        }

        @Override
        protected Map<String,String>  doInBackground(Void... params) {
//            String phone = LoginID.getString("user_mobile","");
//            if(!phone.isEmpty())
//                mUrl_GetMoney += ("?mobile=" + phone);
            String result = CHttpRequestClass.sendGet(mUrl_GetMoney, "orderNo=" + orderNo);
            Money money;
            if (!result.isEmpty()) {
                money = new Gson().fromJson(result, new TypeToken<Money>() {}.getType());
                if(money.getSuccess()) {
                    String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
                    String entity = genProductArgs(money.getName(), String.valueOf((int)(money.getMoney() * 100)));
                    byte[] buf = Util.httpPost(url, entity);
                    String content = new String(buf);
                    Map<String, String> xml = decodeXml(content);
                    return xml;
                }
            }
            return null;
        }
    }

    public Map<String,String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(!"xml".equals(nodeName)) {
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
        }
        return null;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genOutTradNo() {
        return orderNo;
    }

    private String genProductArgs(String Name,String money) {
        StringBuffer xml = new StringBuffer();
        try {
            String	nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", Name));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://www.dadaxueche.com/m/payreturn.do"));
            packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip","123.57.28.214"));
            packageParams.add(new BasicNameValuePair("total_fee", money));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring =toXml(packageParams);
            return new String(xmlstring.getBytes(), "ISO8859-1");
        } catch (Exception e) {
            return null;
        }
    }

    private void genPayReq() {
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        sendPayReq();
    }

    private void sendPayReq() {
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }
}

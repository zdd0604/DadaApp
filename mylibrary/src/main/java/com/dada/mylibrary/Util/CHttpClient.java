package com.dada.mylibrary.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by zhangdongdong on 2015/9/23.
 */
public class CHttpClient {
    private static HttpClient httpClient = new DefaultHttpClient();

    public static String doGet(String url) {
        //将URL与参数拼接
        HttpGet getMethod = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(getMethod); //发起GET请求
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String content = reader.readLine();
                return content;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String doGet3(String url, String param) {
        //将URL与参数拼接
        HttpGet getMethod = new HttpGet(url + "?" + param);
        try {
            HttpResponse response = httpClient.execute(getMethod); //发起GET请求
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String content = reader.readLine();
                return content;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap doGet2(String url) {
        //将URL与参数拼接
        HttpGet getMethod = new HttpGet(url);
        try {
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 5); // 链接超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000 * 5); // 读取超时
            HttpResponse response = httpClient.execute(getMethod); //发起GET请求
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                //如果是返回得字符串，可以直接用 EntityUtils来处理
                //EntityUtils.toString(response.getEntity());
                Bitmap img = BitmapFactory.decodeStream(is);
                return img;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String url, List<BasicNameValuePair> params) {
        try {
            HttpPost postMethod = new HttpPost(url);
            postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //将参数填入POST Entity中
            postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            HttpResponse response = httpClient.execute(postMethod); //执行POST方法
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            Log.v("show",content+"评价返回的数据");
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从服务器取图片
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

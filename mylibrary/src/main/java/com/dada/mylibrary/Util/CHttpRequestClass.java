package com.dada.mylibrary.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class CHttpRequestClass {

    public static int downSize = 0;
    public static int Size = 0;
    public static boolean cando = true;
    public static String filePath;
    public static String fileName;

    public static boolean getData(String url) {
        try {
            URL realUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) realUrl.openConnection();
            urlConnection.setReadTimeout(30000);
            InputStream stream = urlConnection.getInputStream();
            FileOutputStream fos = new FileOutputStream(new File(filePath, fileName));
            byte[] buffer = new byte[1024];
            int len;
            downSize = 0;
            Size = urlConnection.getContentLength();
            while (cando && (len = stream.read(buffer, 0, buffer.length)) != -1) {
                downSize += len;
                fos.write(buffer, 0, len);
            }
            stream.close();
            fos.close();
            urlConnection.disconnect();
            return cando;
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！----->" + e);
            e.printStackTrace();
            return false;
        }
    }

    public static String sendGet(String url) {
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) realUrl.openConnection();
            urlConnection.setReadTimeout(30000);
            InputStream stream = urlConnection.getInputStream();//得到读取的内容
            InputStreamReader in = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(in);    //为输出创建BufferedReader
            String line;
            while (cando && (line = buffer.readLine()) != null) {//使用while循环来取得获取的数据
                result += line;
            }
            in.close();//关闭InputStreamReader
            urlConnection.disconnect();//关闭http连接
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！----->" + e);
            e.printStackTrace();
            result = "";
        }
        return result;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        try {
            URL realUrl = new URL(url + "?" + param);
            HttpURLConnection urlConnection = (HttpURLConnection) realUrl.openConnection();
            urlConnection.setReadTimeout(30000);
            InputStream stream = urlConnection.getInputStream();//得到读取的内容
            InputStreamReader in = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(in);    //为输出创建BufferedReader
            String line;
            while (cando && (line = buffer.readLine()) != null) {//使用while循环来取得获取的数据
                result += line;
            }
            in.close();//关闭InputStreamReader
            urlConnection.disconnect();//关闭http连接
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！----->" + e);
            e.printStackTrace();
            result = "";
        }
        return result;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setReadTimeout(5000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while (cando && (line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
            result = "";
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}

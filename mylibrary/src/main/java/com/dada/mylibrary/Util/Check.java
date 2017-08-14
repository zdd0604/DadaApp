package com.dada.mylibrary.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;

public class Check {

    private Context _context;

    public Check(Context context) {
        this._context = context;
    }

    public static boolean CheckDir(String filePath) {
        File file = new File(filePath);
        return file.exists() || file.mkdirs();
    }

    public static boolean CheckDirNoMkdir(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean CheckFile(String filePath,String fileName) {
        File file = new File(filePath+fileName);
        return file.exists();
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    //检查用户是否连接网络
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}

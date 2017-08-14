package com.dadaxueche.student.dadaapp.Util;

import android.app.Application;

import com.dada.mylibrary.Gson.DrivingSchool;

/**
 * Created by wpf on 8-20-0020.
 */
public class GlobalData extends Application {
    public String mMobile;
    public boolean flag;
    public static GlobalData globalData;
    public DrivingSchool mDrivingSchool;
    public String mDEVICE_ID;
    public String mUser_Mobile;

    public static GlobalData getInstance() {
        if(globalData == null){
            globalData = new GlobalData();
            return globalData;
        }
        return globalData;
    }
}

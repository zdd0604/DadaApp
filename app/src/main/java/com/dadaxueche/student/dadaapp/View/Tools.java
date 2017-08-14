package com.dadaxueche.student.dadaapp.View;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangdongdong on 2015/9/14.
 */
public class Tools{

    //提示
    public static void showToast(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

}
